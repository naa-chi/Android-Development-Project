"""
Start all Juntas Vecinos microservices in order.
Loads .env from the project root and passes its variables to each service.

Usage:
    python run_all.py        # auto-detects Server/ folder
    python run_all.py --root D:\\path\\to\\Server

Press Ctrl+C to stop everything.
"""

import argparse
import os
import re
import socket
import subprocess
import sys
import time
from datetime import datetime
from pathlib import Path

# ─── CONFIG ──────────────────────────────────────────────────────────
SCRIPT_DIR = Path(__file__).resolve().parent

# (display name, directory name, port, grace seconds after port opens)
SERVICES = [
    ("Discovery",     "zDiscovery",    8761, 15),
    ("Gateway",       "zGateway",      8111,  5),
    ("Neighborhoods", "neighborhood",  8387,  5),
    ("Venues",        "venue",         8386,  5),
    ("Events",        "event",         8389,  5),
    ("Expenses",      "expenses",      8390,  5),
    ("Users",         "users",         8391,  5),
]

PORT_OPEN_TIMEOUT = 90
TAIL_LINES_ON_FAILURE = 100
# ─────────────────────────────────────────────────────────────────────

IS_WINDOWS = sys.platform == "win32"
MVN = "mvnw.cmd" if IS_WINDOWS else "./mvnw"

# Folders every Server/ root must contain (used to auto-detect it).
_SERVER_MARKERS = ("event", "expenses", "neighborhood", "users", "venue")


def _find_server_root(start: Path) -> Path | None:
    """Walk a few likely places looking for the Server/ directory."""
    candidates = [
        start,
        start / "Server",
        start.parent,
        start.parent / "Server",
    ]
    for c in candidates:
        if c.is_dir() and all((c / m).is_dir() for m in _SERVER_MARKERS):
            return c
    return None


# ─── Utilities ───────────────────────────────────────────────────────

def ts() -> str:
    return datetime.now().strftime("%H:%M:%S")


def log(msg: str, indent: int = 0) -> None:
    print(f"[{ts()}] {'  ' * indent}{msg}", flush=True)


def port_is_free(port: int, host: str = "127.0.0.1") -> bool:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        try:
            s.bind((host, port))
            return True
        except OSError:
            return False


def port_is_open(port: int, host: str = "127.0.0.1", timeout: float = 0.5) -> bool:
    try:
        with socket.create_connection((host, port), timeout=timeout):
            return True
    except OSError:
        return False


def tail(path: Path, n: int) -> str:
    if not path.exists():
        return ""
    try:
        with path.open("r", encoding="utf-8", errors="replace") as f:
            lines = f.readlines()
        return "".join(lines[-n:])
    except Exception as e:
        return f"<could not read log: {e}>"


def scan_log_for_markers(path: Path) -> dict:
    markers = {
        "started": False,
        "tomcat": False,
        "eureka_registered": False,
        "db_ok": False,
        "errors": [],
    }
    if not path.exists():
        return markers
    try:
        with path.open("r", encoding="utf-8", errors="replace") as f:
            for raw in f:
                line = raw.rstrip()
                if re.search(r"Started \w+ in [\d.]+ seconds", line):
                    markers["started"] = True
                if "Tomcat started on port" in line:
                    markers["tomcat"] = True
                if "DiscoveryClient_" in line and "registering service" in line:
                    markers["eureka_registered"] = True
                if "HikariPool" in line and "Start completed" in line:
                    markers["db_ok"] = True
                if re.search(r"\b(ERROR|FATAL)\b", line) or "Caused by:" in line:
                    markers["errors"].append(line)
    except Exception:
        pass
    return markers


def load_env(env_file: Path) -> None:
    """Read .env from the project root and set each key in os.environ."""
    if not env_file.exists():
        log(f"No .env at {env_file} — using inherited environment only.", indent=1)
        return

    loaded = []
    for raw in env_file.read_text(encoding="utf-8").splitlines():
        line = raw.strip()
        if not line or line.startswith("#"):
            continue
        if line.startswith("export "):
            line = line[len("export "):].strip()
        key, sep, value = line.partition("=")
        if not sep:
            continue
        key = key.strip()
        value = value.strip().strip('"').strip("'")
        if not key:
            continue
        os.environ[key] = value
        loaded.append(key)

    if loaded:
        log(f"Loaded {len(loaded)} var(s) from .env: {', '.join(loaded)}", indent=1)
    else:
        log(".env exists but no variables were read.", indent=1)


# ─── Preflight ───────────────────────────────────────────────────────

def preflight(base_dir: Path, env_file: Path) -> list[str]:
    warnings = []

    try:
        r = subprocess.run(["java", "-version"], capture_output=True, text=True)
        out = (r.stderr or "") + (r.stdout or "")
        m = re.search(r'version "([^"]+)"', out)
        log(f"Java version: {m.group(1)}" if m else "Java version: unknown")
    except FileNotFoundError:
        warnings.append("`java` not found on PATH. Spring Boot services won't start.")
        log("! java not found on PATH", indent=1)

    if env_file.exists():
        log(f".env found at {env_file}")
    else:
        log(f"! .env missing at {env_file} — JWT_SECRET may be undefined", indent=1)

    if "JWT_SECRET" not in os.environ:
        warnings.append(
            "JWT_SECRET not set (neither in .env nor in the shell environment). "
            "Services using ${JWT_SECRET} without a fallback will fail to start."
        )
        log("! JWT_SECRET is not set", indent=1)
    else:
        log(f"JWT_SECRET: set ({len(os.environ['JWT_SECRET'])} chars)")

    for name, directory, port, _ in SERVICES:
        d = base_dir / directory
        if not d.exists():
            warnings.append(f"{name}: directory missing ({d})")
            log(f"! {name}: directory missing: {d}", indent=1)
            continue
        if not (d / MVN).exists() and not (d / "mvnw").exists():
            warnings.append(f"{name}: no maven wrapper in {d} (expected {MVN} or mvnw)")
            log(f"! {name}: no maven wrapper in {d}", indent=1)

    for name, _, port, _ in SERVICES:
        if not port_is_free(port):
            warnings.append(
                f"{name}: port {port} already in use — "
                f"kill the previous process before running this script"
            )
            log(f"! {name}: port {port} already in use", indent=1)

    return warnings


# ─── Launching and waiting ───────────────────────────────────────────

def launch(name: str, base_dir: Path, log_dir: Path, directory: str, port: int,
           grace: int, procs: list, log_handles: list) -> bool:
    d = base_dir / directory
    if not d.exists():
        log(f"SKIP  {name} — {d} not found", indent=1)
        return False

    log_path = log_dir / f"{name.lower()}.log"
    fh = log_path.open("w", encoding="utf-8", errors="replace")
    fh.write(f"===== {name} — started {datetime.now().isoformat()} =====\n")
    fh.flush()
    log_handles.append(fh)

    cmd = f"{MVN} spring-boot:run"
    kwargs = {
        "cwd": str(d),
        "shell": True,
        "stdout": fh,
        "stderr": subprocess.STDOUT,
        "env": os.environ.copy(),
    }
    if IS_WINDOWS:
        kwargs["creationflags"] = subprocess.CREATE_NEW_PROCESS_GROUP

    log(f"Launching {name}  (port {port})  →  {log_path.name}")
    proc = subprocess.Popen(cmd, **kwargs)
    procs.append({"name": name, "proc": proc, "port": port, "log": log_path})

    start = time.time()
    while time.time() - start < PORT_OPEN_TIMEOUT:
        if proc.poll() is not None:
            log(f"✗ {name} exited early with code {proc.returncode}", indent=1)
            return False
        if port_is_open(port):
            log(f"✓ {name} is listening on {port}", indent=1)
            if grace:
                log(f"  giving {name} {grace}s to finish startup", indent=1)
                time.sleep(grace)
            return True
        time.sleep(1)

    log(f"✗ {name} did not open port {port} within {PORT_OPEN_TIMEOUT}s", indent=1)
    return False


# ─── Diagnostics ─────────────────────────────────────────────────────

def print_failure_report(failed_name: str, procs: list, env_warnings: list,
                         base_dir: Path, log_dir: Path, env_file: Path) -> None:
    bar = "=" * 72
    print(f"\n{bar}")
    print(f"FAILURE REPORT — {failed_name}")
    print(f"generated at {datetime.now().isoformat()}")
    print(bar)

    print("\n--- Environment ---")
    print(f"  OS:          {sys.platform}")
    print(f"  Python:      {sys.version.split()[0]}")
    print(f"  BASE_DIR:    {base_dir}")
    print(f"  LOG_DIR:     {log_dir}")
    print(f"  .env:        {env_file} (exists: {env_file.exists()})")
    if "JWT_SECRET" in os.environ:
        print(f"  JWT_SECRET:  set ({len(os.environ['JWT_SECRET'])} chars)")
    else:
        print(f"  JWT_SECRET:  NOT SET")
    if env_warnings:
        print("  Warnings:")
        for w in env_warnings:
            print(f"    - {w}")
    else:
        print("  (no preflight warnings)")

    entry = next((p for p in procs if p["name"] == failed_name), None)

    print("\n--- Status of all services ---")
    for p in procs:
        status = "RUNNING" if p["proc"].poll() is None else f"EXITED ({p['proc'].returncode})"
        listening = "listening" if port_is_open(p["port"]) else "NOT listening"
        markers = scan_log_for_markers(p["log"])
        eureka = "eureka-ok" if markers["eureka_registered"] else "NO-EUREKA"
        db = "db-ok" if markers["db_ok"] else "no-db"
        print(f"  {p['name']:<14} port {p['port']:<5} {status:<16} {listening:<14} {eureka:<12} {db}")

    if entry is None:
        print("\n(no log available for failed service)")
        print(bar + "\n")
        return

    print(f"\n--- Last {TAIL_LINES_ON_FAILURE} lines of {entry['log'].name} ---")
    print(tail(entry["log"], TAIL_LINES_ON_FAILURE))

    markers = scan_log_for_markers(entry["log"])
    if markers["errors"]:
        print(f"\n--- Detected {len(markers['errors'])} error/stack lines (last 20) ---")
        for line in markers["errors"][-20:]:
            print("  " + line)

    print("\n--- Suggested next steps ---")
    recent = tail(entry["log"], 2000)
    if "Caused by: java.net.BindException" in recent:
        print("  • Port already in use. Kill the old process or change the port.")
    if "Communications link failure" in recent or "Access denied for user" in recent:
        print("  • Database problem. Check application.properties credentials and that MySQL is up.")
    if "No instances available" in recent:
        print("  • A Feign call couldn't find the target. Is the target service registered in Eureka?")
    if "Cannot load environment" in recent or "NoClassDefFoundError" in recent:
        print("  • Spring Boot / Spring Cloud version mismatch. Check the compatibility matrix.")
    if "WeakKeyException" in recent or "key byte array is" in recent:
        print("  • JWT secret is too short. HS256 requires at least 32 bytes.")
    if "Could not resolve placeholder 'JWT_SECRET'" in recent:
        print("  • JWT_SECRET is not set. Add it to .env at the project root.")
    print("\nPaste this whole block when asking for help.\n")
    print(bar + "\n")


# ─── Shutdown ────────────────────────────────────────────────────────

def shutdown(procs: list, log_handles: list, log_dir: Path) -> None:
    print()
    log("Shutting down all services...")
    for entry in procs:
        proc = entry["proc"]
        if proc.poll() is not None:
            continue
        log(f"  Stopping {entry['name']} (pid {proc.pid})")
        try:
            if IS_WINDOWS:
                subprocess.run(
                    ["taskkill", "/F", "/T", "/PID", str(proc.pid)],
                    stdout=subprocess.DEVNULL,
                    stderr=subprocess.DEVNULL,
                )
            else:
                proc.terminate()
        except Exception as e:
            log(f"    failed to stop {entry['name']}: {e}")

    time.sleep(2)
    for entry in procs:
        p = entry["proc"]
        if p.poll() is None:
            try:
                p.kill()
            except Exception:
                pass

    for fh in log_handles:
        try:
            fh.flush()
            fh.close()
        except Exception:
            pass

    log("All services stopped.")
    log(f"Per-service logs kept in: {log_dir}")


# ─── Main ────────────────────────────────────────────────────────────

def main() -> None:
    ap = argparse.ArgumentParser(description="Start all Juntas Vecinos microservices")
    ap.add_argument("--root", default=None,
                    help="Path to Server/ (default: auto-detect from this script)")
    args = ap.parse_args()

    if args.root:
        base_dir = Path(args.root).resolve()
        if not base_dir.is_dir():
            print(f"error: {base_dir} is not a directory", file=sys.stderr)
            sys.exit(2)
    else:
        detected = _find_server_root(SCRIPT_DIR)
        if detected is None:
            print("error: could not auto-detect Server/ folder.\n"
                  f"       Tried: {SCRIPT_DIR}, {SCRIPT_DIR / 'Server'}, "
                  f"{SCRIPT_DIR.parent}, {SCRIPT_DIR.parent / 'Server'}\n"
                  "       Pass --root PATH to override.", file=sys.stderr)
            sys.exit(2)
        base_dir = detected

    log_dir = base_dir / "logs"
    env_file = base_dir / ".env"
    log_dir.mkdir(parents=True, exist_ok=True)

    print("=" * 72)
    print(f"Juntas Vecinos — backend launcher — {datetime.now().isoformat()}")
    print(f"Server root: {base_dir}")
    print("=" * 72)

    log("Loading .env...")
    load_env(env_file)
    print()

    log("Preflight checks...")
    warnings = preflight(base_dir, env_file)
    if warnings:
        log(f"{len(warnings)} warning(s) — see above. Continuing anyway.", indent=1)
    else:
        log("All preflight checks passed.", indent=1)
    print()

    procs: list = []
    log_handles: list = []

    try:
        for name, directory, port, grace in SERVICES:
            print(f"───── {name} ─────")
            ok = launch(name, base_dir, log_dir, directory, port, grace, procs, log_handles)
            if not ok:
                print_failure_report(name, procs, warnings, base_dir, log_dir, env_file)
                log("Aborting — stopping services already started.")
                shutdown(procs, log_handles, log_dir)
                sys.exit(1)
            print()

        print("=" * 72)
        print("All services started successfully.")
        print(f"  Per-service logs: {log_dir}")
        print("  Press Ctrl+C to stop everything.")
        print()

        while True:
            time.sleep(1)

    except KeyboardInterrupt:
        shutdown(procs, log_handles, log_dir)


if __name__ == "__main__":
    main()
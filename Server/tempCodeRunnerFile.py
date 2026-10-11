import os
import re
import socket
import subprocess
import sys
import time
from datetime import datetime
from pathlib import Path

# ─── CONFIG ──────────────────────────────────────────────────────────
SCRIPT_DIR = Path(__file__).resolve().parent #DeepSeek V-4.1 generated runner, CBA to do this by hand
BASE_DIR = SCRIPT_DIR
LOG_DIR = SCRIPT_DIR / "zz_logs"

# (display name, directory name, port, grace seconds after port opens)
SERVICES = [
    ("Discovery",     "zDiscovery",         8761, 15),
    #("Gateway",       "zGateway",               8080,  5),
    ("Neighborhoods", "neighborhood",  8387,  5),
    ("Venues",        "venue",         8386,  5)# ,
    #("Events",        "event-service",         8084,  5),
    #("Costs",         "cost-service",          8085,  5),
    #("Users",         "user-service",          8083,  5),
]

PORT_OPEN_TIMEOUT = 90       # seconds to wait for a port to open
TAIL_LINES_ON_FAILURE = 100  # how many log lines to print on failure
# ─────────────────────────────────────────────────────────────────────

IS_WINDOWS = sys.platform == "win32"
MVN = "mvnw.cmd" if IS_WINDOWS else "./mvnw"


# ─── Utilities ───────────────────────────────────────────────────────

def ts() -> str:
    return datetime.now().strftime("%H:%M:%S")


def log(msg: str, indent: int = 0) -> None:
    print(f"[{ts()}] {'  ' * indent}{msg}", flush=True)


def port_is_free(port: int, host: str = "127.0.0.1") -> bool:
    """True if nothing is currently bound to the port."""
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        try:
            s.bind((host, port))
            return True
        except OSError:
            return False


def port_is_open(port: int, host: str = "127.0.0.1", timeout: float = 0.5) -> bool:
    """True if something is listening on the port."""
    try:
        with socket.create_connection((host, port), timeout=timeout):
            return True
    except OSError:
        return False


def tail(path: Path, n: int) -> str:
    """Return the last n lines of a file, or '' if it doesn't exist."""
    if not path.exists():
        return ""
    try:
        with path.open("r", encoding="utf-8", errors="replace") as f:
            lines = f.readlines()
        return "".join(lines[-n:])
    except Exception as e:
        return f"<could not read log: {e}>"


def scan_log_for_markers(path: Path) -> dict:
    """Look for interesting lines in a service's log so we can report status."""
    markers = {
        "started": False,          # Spring Boot finished starting
        "tomcat": False,           # HTTP server is listening
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


# ─── Preflight ───────────────────────────────────────────────────────

def preflight() -> list[str]:
    """Return a list of warnings about the environment. Empty = all good."""
    warnings = []

    # Java
    try:
        r = subprocess.run(["java", "-version"], capture_output=True, text=True)
        out = (r.stderr or "") + (r.stdout or "")
        m = re.search(r'version "([^"]+)"', out)
        if m:
            log(f"Java version: {m.group(1)}")
        else:
            log("Java version: unknown")
    except FileNotFoundError:
        warnings.append("`java` not found on PATH. Spring Boot services won't start.")
        log("! java not found on PATH", indent=1)

    # Base directory
    if not BASE_DIR.exists():
        warnings.append(f"BASE_DIR does not exist: {BASE_DIR}")
        log(f"! BASE_DIR missing: {BASE_DIR}", indent=1)
        return warnings

    # Each service directory + maven wrapper
    for name, directory, port, _ in SERVICES:
        d = BASE_DIR / directory
        if not d.exists():
            warnings.append(f"{name}: directory missing ({d})")
            log(f"! {name}: directory missing: {d}", indent=1)
            continue
        if not (d / MVN).exists() and not (d / "mvnw").exists():
            warnings.append(
                f"{name}: no maven wrapper in {d} (expected {MVN} or mvnw)"
            )
            log(f"! {name}: no maven wrapper in {d}", indent=1)

    # Ports
    for name, _, port, _ in SERVICES:
        if not port_is_free(port):
            warnings.append(
                f"{name}: port {port} already in use — "
                f"kill the previous process before running this script"
            )
            log(f"! {name}: port {port} already in use", indent=1)

    return warnings


# ─── Launching and waiting ───────────────────────────────────────────

def launch(name: str, directory: str, port: int, grace: int,
           procs: list, log_handles: list) -> bool:
    d = BASE_DIR / directory
    if not d.exists():
        log(f"SKIP  {name} — {d} not found", indent=1)
        return False

    log_path = LOG_DIR / f"{name.lower()}.log"
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
    }
    if IS_WINDOWS:
        kwargs["creationflags"] = subprocess.CREATE_NEW_PROCESS_GROUP

    log(f"Launching {name}  (port {port})  →  {log_path.name}")
    proc = subprocess.Popen(cmd, **kwargs)
    procs.append({"name": name, "proc": proc, "port": port, "log": log_path})

    # Wait for the port, while checking the process is still alive.
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

def print_failure_report(failed_name: str, procs: list,
                         env_warnings: list) -> None:
    bar = "=" * 72
    print(f"\n{bar}")
    print(f"FAILURE REPORT — {failed_name}")
    print(f"generated at {datetime.now().isoformat()}")
    print(bar)

    print("\n--- Environment ---")
    print(f"  OS:          {sys.platform}")
    print(f"  Python:      {sys.version.split()[0]}")
    print(f"  BASE_DIR:    {BASE_DIR}")
    print(f"  LOG_DIR:     {LOG_DIR}")
    if env_warnings:
        print("  Warnings:")
        for w in env_warnings:
            print(f"    - {w}")
    else:
        print("  (no preflight warnings)")

    # Find the failed process entry
    entry = next((p for p in procs if p["name"] == failed_name), None)

    print("\n--- Status of all services ---")
    for p in procs:
        status = "RUNNING" if p["proc"].poll() is None else f"EXITED ({p['proc'].returncode})"
        listening = "listening" if port_is_open(p["port"]) else "NOT listening"
        markers = scan_log_for_markers(p["log"])
        eureka = "eureka-ok" if markers["eureka_registered"] else "NO-EUREKA"
        db = "db-ok" if markers["db_ok"] else ("no-db" if markers["db_ok"] is False else "?")
        print(f"  {p['name']:<14} port {p['port']:<5} {status:<16} {listening:<14} {eureka:<12} {db}")

    if entry is None:
        print("\n(no log available for failed service)")
        print(bar + "\n")
        return

    print(f"\n--- Last {TAIL_LINES_ON_FAILURE} lines of {entry['log'].name} ---")
    print(tail(entry["log"], TAIL_LINES_ON_FAILURE))

    # Highlight likely culprit
    markers = scan_log_for_markers(entry["log"])
    if markers["errors"]:
        print(f"\n--- Detected {len(markers['errors'])} error/stack lines (last 20) ---")
        for line in markers["errors"][-20:]:
            print("  " + line)

    print("\n--- Suggested next steps ---")
    if "Caused by: java.net.BindException" in tail(entry["log"], 2000):
        print("  • Port already in use. Kill the old process or change the port.")
    if "Communications link failure" in tail(entry["log"], 2000) or \
       "Access denied for user" in tail(entry["log"], 2000):
        print("  • Database problem. Check application.properties credentials and that MySQL is up.")
    if "No instances available" in tail(entry["log"], 2000):
        print("  • A Feign call couldn't find the target. Is the target service registered in Eureka?")
    if "Cannot load environment" in tail(entry["log"], 2000) or \
       "NoClassDefFoundError" in tail(entry["log"], 2000):
        print("  • Spring Boot / Spring Cloud version mismatch. Check the compatibility matrix.")
    print("\nPaste this whole block when asking for help.\n")
    print(bar + "\n")


# ─── Shutdown ────────────────────────────────────────────────────────

def shutdown(procs: list, log_handles: list) -> None:
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
    log(f"Per-service logs kept in: {LOG_DIR}")


# ─── Main ────────────────────────────────────────────────────────────

def main() -> None:
    LOG_DIR.mkdir(parents=True, exist_ok=True)

    print("=" * 72)
    print(f"Juntas Vecinos — backend launcher — {datetime.now().isoformat()}")
    print("=" * 72)

    log("Preflight checks...")
    warnings = preflight()
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
            ok = launch(name, directory, port, grace, procs, log_handles)
            if not ok:
                print_failure_report(name, procs, warnings)
                log("Aborting — stopping services already started.")
                shutdown(procs, log_handles)
                sys.exit(1)
            print()

        print("=" * 72)
        print("All services started successfully.")
        print("=" * 72)
        print("  Eureka:        http://localhost:8761")
        print("  Gateway:       http://localhost:8080")
        print("  Venues:        http://localhost:8386/api/v1/venues")
        print("  Neighborhoods: http://localhost:8385/api/v1/neighborhoods")
        #print("  Users:         http://localhost:8083/api/v1/users")
        #print("  Events:        http://localhost:8084/api/v1/events")
        #print("  Costs:         http://localhost:8085/api/v1/costs")
        print()
        print(f"  Per-service logs: {LOG_DIR}")
        print("  Press Ctrl+C to stop everything.")
        print()

        while True:
            time.sleep(1)

    except KeyboardInterrupt:
        shutdown(procs, log_handles)


if __name__ == "__main__":
    main()
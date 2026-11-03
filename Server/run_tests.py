#!/usr/bin/env python3
"""
run_tests.py — runs `./mvnw test` for every microservice, captures output,
and writes a detailed log per service plus a summary.

Usage:
    python run_tests.py --root ./Server
    python run_tests.py --root ./Server --services event users
    python run_tests.py --root ./Server --fail-fast
"""

from __future__ import annotations
import argparse
import re
import subprocess
import sys
from datetime import datetime
from pathlib import Path

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------

SERVICES = [
    ("event", "event"),
    ("expenses", "expenses"),
    ("neighborhood", "neighborhood"),
    ("users", "users"),
    ("venue", "venue"),
    ("zGateway", "gateway"),
    ("zDiscovery", "discovery"),
]


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

def timestamp() -> str:
    return datetime.now().strftime("%Y-%m-%d %H:%M:%S")


def maven_wrapper(service_dir: Path) -> list[str]:
    """Return the correct wrapper invocation for the current OS."""
    if sys.platform == "win32":
        cmd = service_dir / "mvnw.cmd"
        if cmd.exists():
            return [str(cmd)]
        return ["mvn"]
    cmd = service_dir / "mvnw"
    if cmd.exists():
        return ["./mvnw"]
    return ["mvn"]


def run_maven_test(service_dir: Path, log_file: Path) -> int:
    """Runs `./mvnw test` in service_dir, writes stdout+stderr to log_file, returns exit code."""
    base = maven_wrapper(service_dir)
    # Exclude context-load tests (they need a live MySQL).
    cmd = base + [
        "test",
        "-Dmaven.test.skip=false",
        "-DfailIfNoTests=false",
        "-Dtest=!*ApplicationTests",
    ]

    with log_file.open("w", encoding="utf-8") as fh:
        fh.write(f"===== {timestamp()} — {' '.join(cmd)} =====\n")
        fh.flush()
        try:
            proc = subprocess.run(
                cmd,
                cwd=str(service_dir),
                stdout=fh,
                stderr=subprocess.STDOUT,
                text=True,
            )
            fh.write(f"\n===== exit code: {proc.returncode} =====\n")
            return proc.returncode
        except FileNotFoundError:
            fh.write("ERROR: Maven wrapper / mvn not found\n")
            return 127


def parse_surefire_reports(service_dir: Path) -> dict:
    """Read target/surefire-reports/*.txt and return summary."""
    report_dir = service_dir / "target" / "surefire-reports"
    if not report_dir.exists():
        return {"tests": 0, "failures": 0, "errors": 0, "skipped": 0}

    summary = {"tests": 0, "failures": 0, "errors": 0, "skipped": 0}
    for txt in report_dir.glob("*.txt"):
        content = txt.read_text(encoding="utf-8", errors="replace")
        m = re.search(
            r"Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+)",
            content,
        )
        if m:
            summary["tests"] += int(m.group(1))
            summary["failures"] += int(m.group(2))
            summary["errors"] += int(m.group(3))
            summary["skipped"] += int(m.group(4))
    return summary


def print_header(title: str) -> None:
    print(f"\n{'=' * 70}")
    print(f"  {title}")
    print(f"{'=' * 70}")


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main() -> int:
    ap = argparse.ArgumentParser(description="Run Maven tests for all microservices")
    ap.add_argument("--root", required=True, help="Path to Server/ directory")
    ap.add_argument("--services", nargs="*", help="Limit to specific service dirs")
    ap.add_argument("--fail-fast", action="store_true", help="Stop at first failure")
    ap.add_argument("--log-dir", default="logs/test-reports", help="Where to write logs")
    args = ap.parse_args()

    root = Path(args.root).resolve()
    log_dir = root / args.log_dir
    log_dir.mkdir(parents=True, exist_ok=True)

    selected = SERVICES
    if args.services:
        selected = [(d, n) for d, n in SERVICES if d in args.services]

    overall_exit = 0
    results: list[dict] = []

    print_header(f"Running tests in {root}")
    print(f"Log directory: {log_dir}\n")

    for dir_name, label in selected:
        service_dir = root / dir_name
        if not service_dir.exists():
            print(f"⚠  skipping {dir_name} — not found")
            continue

        log_file = log_dir / f"{dir_name}.log"
        print(f"→ {label} ({dir_name}) … ", end="", flush=True)

        code = run_maven_test(service_dir, log_file)
        report = parse_surefire_reports(service_dir)

        status = "PASS" if code == 0 else "FAIL"
        print(
            f"{status}  (tests={report['tests']}, "
            f"failures={report['failures']}, errors={report['errors']}, "
            f"skipped={report['skipped']})"
        )

        results.append({
            "dir": dir_name,
            "label": label,
            "code": code,
            "report": report,
            "log": str(log_file),
        })

        if code != 0:
            overall_exit = 1
            if args.fail_fast:
                print("⚠  --fail-fast enabled, stopping.")
                break

    # Summary
    print_header("Test Summary")
    total_tests = sum(r["report"]["tests"] for r in results)
    total_fail = sum(r["report"]["failures"] + r["report"]["errors"] for r in results)

    for r in results:
        mark = "✓" if r["code"] == 0 else "✗"
        print(
            f"  {mark} {r['label']:<15} {r['report']['tests']:>3} tests, "
            f"{r['report']['failures'] + r['report']['errors']:>2} failed  "
            f"→ {r['log']}"
        )

    print(f"\n  Total: {total_tests} tests, {total_fail} failed")
    print(f"  Overall: {'PASS' if overall_exit == 0 else 'FAIL'}\n")

    return overall_exit


if __name__ == "__main__":
    sys.exit(main())
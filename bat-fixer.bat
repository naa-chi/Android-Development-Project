@echo off
REM ====================================================================
REM  bat-fixer.bat — generate + run unit tests for Juntas Vecinos
REM
REM  Placement: Android-Development-Project/bat-fixer.bat
REM             (same folder as Server/, Test/, README.md)
REM
REM  What it does:
REM    1. Runs Server/generate_tests.py to (re)create all JUnit tests
REM    2. Runs Server/run_tests.py to execute them via the Maven wrapper
REM ====================================================================

setlocal

set "SCRIPT_DIR=%~dp0"
set "SERVER_DIR=%SCRIPT_DIR%Server"
set "GENERATE=%SERVER_DIR%\generate_tests.py"
set "RUN_TESTS=%SERVER_DIR%\run_tests.py"

REM --- sanity checks ---------------------------------------------------
if not exist "%SERVER_DIR%" (
    echo ERROR: Server directory not found at "%SERVER_DIR%"
    exit /b 2
)
if not exist "%GENERATE%" (
    echo ERROR: generate_tests.py not found at "%GENERATE%"
    exit /b 2
)
if not exist "%RUN_TESTS%" (
    echo ERROR: run_tests.py not found at "%RUN_TESTS%"
    exit /b 2
)

REM --- pick a Python 3 launcher ----------------------------------------
where py >nul 2>&1
if %ERRORLEVEL%==0 (
    set "PY=py -3"
    goto :have_python
)
where python >nul 2>&1
if %ERRORLEVEL%==0 (
    set "PY=python"
    goto :have_python
)

echo ERROR: neither `py` nor `python` is on PATH.
echo Install Python 3.10+ and re-run.
exit /b 127

:have_python

REM --- 1. Generate tests ------------------------------------------------
echo.
echo ============================================================
echo  [1/2] Generating JUnit tests
echo ============================================================
%PY% "%GENERATE%" --root "%SERVER_DIR%"
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERROR: test generation failed. Fix the generator and re-run.
    exit /b %ERRORLEVEL%
)

REM --- 2. Run tests -----------------------------------------------------
echo.
echo ============================================================
echo  [2/2] Running tests
echo ============================================================
%PY% "%RUN_TESTS%" --root "%SERVER_DIR%"
exit /b %ERRORLEVEL%
---
name: script-styler
description: Formats shell scripts (.sh) and batch scripts (.bat) with a custom copyright header, clean options, and standardized colorized outputs (info, success, error, print, menu) for consistent UI styling.
---

# Script Styler Skill

This skill enforces a consistent, professional, and beautifully colored user interface for all Bash (`.sh`) and Windows Command (`.bat`) scripts. It also applies standard copyright headers and usage documentation blocks.

## When to Use This Skill
- When creating a new shell script (`.sh` or `.bat`).
- When modifying, extending, or refactoring existing scripts.
- When explicitly requested by the user to stylize or colorize a script.

---

## 1. Header and Copyright Template

Every script must start with a clean documentation header. Replace `{year}` with the current year (e.g. 2026), `{script_name}` with the filename, and document the usage clearly.

### Bash scripts (`.sh`):
```bash
#!/bin/bash
# ============================================================================
# Author: J. Jose de Jesus Silva A.
# Contact: jsilva210985@gmail.com
# Copyright (c) {year}
# All rights reserved.
# {script_name} - {Brief description of what the script does}
#
# Uso:
#   ./{script_name} [options]
# ============================================================================
```

### Windows Batch scripts (`.bat`):
```cmd
@echo off
REM ============================================================================
REM Author: J. Jose de Jesus Silva A.
REM Contact: jsilva210985@gmail.com
REM Copyright (c) {year}
REM All rights reserved.
REM {script_name} - {Brief description of what the script does}
REM
REM Uso:
REM   {script_name} [options]
REM ============================================================================
```

---

## 2. Style & Color Functions for Bash (`.sh`)

For all Bash scripts, define the following styles to colorize messages:

```bash
# ============================================
# Funciones de estilos
# ============================================
function info()    { echo -e "\033[38;2;51;255;255m$1\033[0m"; }
function success() { echo -e "\033[38;2;153;255;51m$1\033[0m"; }
function error()   { echo -e "\033[1;31m$1\033[0m"; }
function print()   { echo -e "$1"; }
function menu()    { echo -e "\033[38;2;255;255;51m$1\033[0m"; }
```

### Color Mapping Rules:
- **Info (Cyan - RGB 51, 255, 255)**: Use `info` for headers, division lines, or section titles.
- **Success (Lime Green - RGB 153, 255, 51)**: Use `success` for confirmation of successful steps.
- **Error (Bold Red)**: Use `error` for validation failures, missing files, or abort messages.
- **Menu (Yellow - RGB 255, 255, 51)**: Use `menu` for displaying CLI menus, help text, or usage instructions.
- **Print (Reset/Standard)**: Use `print` or default `echo` for general details, logs, and uncolored updates.

---

## 3. Style & Color Variables for Windows Batch (`.bat`)

Since Windows Batch files do not support function definitions like Bash, define the ANSI escape sequences as environment variables. This allows modern Windows Terminal and modern CMD shells to print the exact same colors.

```cmd
:: Enable ANSI escape sequences support
for /F %%a in ('echo prompt $E ^| cmd') do set "ESC=%%a"

:: Define styles
set "info=%ESC%[38;2;51;255;255m"
set "success=%ESC%[38;2;153;255;51m"
set "error=%ESC%[1;31m"
set "menu=%ESC%[38;2;255;255;51m"
set "reset=%ESC%[0m"
```

### How to use variables in Batch:
- **Info (Cyan)**: `echo %info%[Message]%reset%`
- **Success (Lime Green)**: `echo %success%[Message]%reset%`
- **Error (Bold Red)**: `echo %error%[Message]%reset%`
- **Menu (Yellow)**: `echo %menu%[Message]%reset%`

---

## Examples

### Bash Example:
```bash
#!/bin/bash
# ============================================================================
# Author: J. Jose de Jesus Silva A.
# Contact: jsilva210985@gmail.com
# Copyright (c) 2026
# All rights reserved.
# clean_cache.sh - Cleans build caches
#
# Uso:
#   ./clean_cache.sh -all
# ============================================================================

function info()    { echo -e "\033[38;2;51;255;255m$1\033[0m"; }
function success() { echo -e "\033[38;2;153;255;51m$1\033[0m"; }
function error()   { echo -e "\033[1;31m$1\033[0m"; }
function print()   { echo -e "$1"; }
function menu()    { echo -e "\033[38;2;255;255;51m$1\033[0m"; }

if [ "$1" == "-all" ]; then
    info "Starting cleanup process..."
    rm -rf ./target
    success "Caches successfully cleaned."
else
    menu "Usage: ./clean_cache.sh -all"
fi
```

### Batch Example:
```cmd
@echo off
REM ============================================================================
REM Author: J. Jose de Jesus Silva A.
REM Contact: jsilva210985@gmail.com
REM Copyright (c) 2026
REM All rights reserved.
REM clean_cache.bat - Cleans build caches
REM
REM Uso:
REM   clean_cache.bat -all
REM ============================================================================

for /F %%a in ('echo prompt $E ^| cmd') do set "ESC=%%a"
set "info=%ESC%[38;2;51;255;255m"
set "success=%ESC%[38;2;153;255;51m"
set "error=%ESC%[1;31m"
set "menu=%ESC%[38;2;255;255;51m"
set "reset=%ESC%[0m"

if "%1"=="-all" (
    echo %info%Starting cleanup process...%reset%
    rd /s /q target
    echo %success%Caches successfully cleaned.%reset%
) else (
    echo %menu%Usage: clean_cache.bat -all%reset%
)
```

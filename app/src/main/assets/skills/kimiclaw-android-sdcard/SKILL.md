---
name: kimiclaw-android-sdcard
description: Use this skill when the user wants to list, rename, move, copy, delete, or organize files on the Android device's shared storage (/sdcard/), including Download, DCIM, Documents, Pictures, Music, and Movies directories. Also use for processing files with tools like pdftotext, tesseract, ffmpeg, python3, etc.
homepage: https://www.kimi.com
---

# Android Shared Storage (sdcard)

Execute shell commands on the device's shared storage (`/sdcard/`) through `kimiclaw-cli`. Just write standard shell commands — the system handles execution for you.

Command pattern: `kimiclaw-cli '{"sdcard_exec": "<shell_command>", "timeout_ms": 30000}'`

## Permission

Requires "All files access" (`MANAGE_EXTERNAL_STORAGE`). If not granted, the first `sdcard_exec` call will open the system settings page — ask the user to toggle the switch, then retry.

Error response when permission is missing:
```json
{"status": "error", "error_code": "STORAGE_NOT_GRANTED", "message": "..."}
```

## Common Operations

```bash
# List files (detailed, sorted by time)
kimiclaw-cli '{"sdcard_exec": "ls -ltr /sdcard/Download/"}'

# Filter by pattern
kimiclaw-cli '{"sdcard_exec": "ls /sdcard/Download/ | grep -i pdf"}'

# Find files by name
kimiclaw-cli '{"sdcard_exec": "find /sdcard/Download/ -name \"*.jpg\" -type f"}'

# File count
kimiclaw-cli '{"sdcard_exec": "ls -1 /sdcard/Download/ | wc -l"}'

# Disk usage
kimiclaw-cli '{"sdcard_exec": "du -sh /sdcard/Download/*"}'

# Rename
kimiclaw-cli '{"sdcard_exec": "mv /sdcard/Download/old.txt /sdcard/Download/new.txt"}'

# Copy
kimiclaw-cli '{"sdcard_exec": "cp /sdcard/Download/a.txt /sdcard/Documents/a.txt"}'

# Move
kimiclaw-cli '{"sdcard_exec": "mv /sdcard/Download/photo.jpg /sdcard/Pictures/"}'

# Delete (confirm with user first)
kimiclaw-cli '{"sdcard_exec": "rm /sdcard/Download/temp.txt"}'

# Create directory
kimiclaw-cli '{"sdcard_exec": "mkdir -p /sdcard/Download/archive"}'

# Read text file
kimiclaw-cli '{"sdcard_exec": "cat /sdcard/Documents/notes.txt"}'

# Write text file
kimiclaw-cli '{"sdcard_exec": "echo \"hello\" > /sdcard/Download/test.txt"}'
```

## Available Directories

| Path | Content |
|------|---------|
| `/sdcard/Download/` | Downloaded files |
| `/sdcard/DCIM/` | Camera photos and videos |
| `/sdcard/Pictures/` | Images |
| `/sdcard/Documents/` | Documents |
| `/sdcard/Music/` | Audio files |
| `/sdcard/Movies/` | Video files |

## Installing Tools

For processing tasks (PDF, OCR, media, scripting), install the required tool with `apt-get` before use. If a command returns `command not found`, try installing the missing package first.

```bash
# PDF text extraction
kimiclaw-cli '{"sdcard_exec": "apt-get install -y poppler-utils", "timeout_ms": 300000}'
kimiclaw-cli '{"sdcard_exec": "pdftotext /sdcard/Documents/a.pdf -"}'

# OCR
kimiclaw-cli '{"sdcard_exec": "apt-get install -y tesseract-ocr", "timeout_ms": 300000}'
kimiclaw-cli '{"sdcard_exec": "tesseract /sdcard/Pictures/scan.jpg - -l eng"}'

# Python one-liner
kimiclaw-cli '{"sdcard_exec": "python3 -c \"import json; print(json.load(open(\\\"/sdcard/Download/x.json\\\")))\""}'
```

## Safety Rules

- **Deleting files** — ALWAYS confirm with user before `rm`
- **Bulk operations** — show the file list first, get user confirmation, then execute
- **Path traversal** (`..`) is forbidden
- **Timeout** — default 30s; use larger `timeout_ms` for slow operations:
  - `find` on large directories: 60000
  - `apt-get install`: 300000
  - Long-running scripts: set accordingly

## Response Format

```json
{
  "status": "success",
  "sdcard_exec": "ls /sdcard/Download/",
  "stdout": "file1.txt\nfile2.pdf\n",
  "exit_code": 0
}
```

On failure, `stderr` is included:
```json
{
  "status": "error",
  "sdcard_exec": "ls /sdcard/NoSuchDir/",
  "stdout": "",
  "stderr": "ls: /sdcard/NoSuchDir/: No such file or directory",
  "exit_code": 2
}
```

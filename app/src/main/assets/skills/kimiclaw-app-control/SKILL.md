---
name: android-app-control
description: >-
  Control Android apps on this device through UI automation: open apps, switch screens, browse feeds, search, navigate app interfaces, fill forms, check notifications and ANY request that involves operating Android apps. Use for both explicit and implicit requests to interact with an Android app, including colloquial phrases like "launch the browser", "check messages", "scroll social media", and "look something up in the browser".
homepage: https://www.kimi.com
---

# KimiClaw UI Automation (Android)

You control an Android device through `kimiclaw-cli`. You have zero visibility into the device — you cannot see the screen, check installed apps, or know any device state without executing commands. Every observation and every action requires a `kimiclaw-cli` call. Always execute first, then interpret the response.

## Typical Examples

Use this skill for requests like:

- Open a browser such as Chrome (`com.android.chrome`)
- Browse Xiaohongshu / RED feeds
- Search within an app
- Navigate app settings or profile pages
- Fill forms or input text in Android apps

## Safety Gates

- Banking, payment, and finance apps are strictly prohibited. Abort and warn the user.

## Progress Updates (Critical — narrate as you work)

The user can ONLY see your `text` output and the result of `notify_user`. Tool calls and their JSON results are invisible. Without progress updates, the chat will be silent for minutes while you operate the device — terrible UX.

**Rule**: send a short `notify_user` (1 short sentence, ~10 words) at these moments:

1. **Task start** — declare what you're about to do, e.g. `"Opening Xiaohongshu..."`
2. **Stage transition** — after UI state changes, e.g. `"In chat list, searching for Zhang"`
3. **Before long actions** — before consecutive swipes / waiting for load, e.g. `"Scrolling feed for the last 3 posts"`
4. **When blocked** — popup / failure / retry, e.g. `"Update prompt appeared, dismissing first"`

**Frequency**: send 1 `notify_user` after every 1-3 `kimiclaw-cli` calls. After 4 or more consecutive tool calls without one, you MUST send a notify_user before continuing. Do NOT notify on every individual click / tap.

**Language**: write the notify_user `text` in the same language the user used in their request.

```bash
kimiclaw-cli '{"ui": "notify_user", "text": "Opening Xiaohongshu"}'
```

**Note**: `notify_user` sends text only. For screenshots, keep using `send_screenshot` (which also accepts a text caption).

## Open App

Banking, payment, and finance apps are strictly prohibited. Abort and warn the user.

Resolve the app name to a package name using your knowledge, execute the command, and only then report the result to the user. Do not comment on app availability before seeing the command response.

```bash
kimiclaw-cli '{"ui": "open_app", "package_name": "com.tencent.mm", "timeout_ms": 10000}'
```

If the response contains `PACKAGE_NOT_FOUND`, check the alias map for the correct package name and retry:

```bash
cat ~/.openclaw/skills/kimiclaw-app-control/apps-aliases.json
```

## System Commands

```bash
kimiclaw-cli '{"ui": "ping"}'
kimiclaw-cli '{"ui": "info"}'
```

## UI Inspection

Start with `tree` — it returns text, resource_id, bounds, and interactive states, sufficient for most operations.

```bash
kimiclaw-cli '{"ui": "tree", "max_nodes": 400}'
```

When response contains `"screenshot_recommended": true`, fall back to screenshot:

```bash
kimiclaw-cli '{"ui": "screenshot"}'
```

After screenshot, use `read` tool to view `image_path`, then interact with `"image_coords": true`:

```bash
kimiclaw-cli '{"ui": "tap", "x": <image_x>, "y": <image_y>, "image_coords": true}'
```

Keyboard pushes content up — take a new screenshot after any input field tap.

```bash
kimiclaw-cli '{"ui": "find", "target": {"text_contains": "logcat"}, "timeout_ms": 3000}'
```

## UI Actions

These commands automatically return the updated UI tree on success.

### Click vs Tap — Always prefer `click`

**`click`** matches elements by semantic attributes (text, id, desc). It auto-searches the UI tree, retries within timeout, and handles scrolling/clickability fallbacks. It is more accurate and robust than tap.

**`tap`** is a raw coordinate gesture. It's imprecise — you may need multiple attempts to hit the right spot. **Only use `tap` when the tree response contains `"accessibility_limited": true` or `"screenshot_recommended": true`**, meaning the app blocks Accessibility and you have no semantic elements to target.

**Rule: If the UI tree has text/id/desc for your target, use `click`. Never guess coordinates when you have semantic data.**

```bash
# ✅ PREFERRED — click by selector (accurate, auto-retry)
kimiclaw-cli '{"ui": "click", "target": {"text_contains": "搜索"}, "timeout_ms": 3000}'

# ⚠️ FALLBACK ONLY — tap at coordinates (when accessibility is blocked)
kimiclaw-cli '{"ui": "tap", "x": 720, "y": 2300}'

# ⚠️ FALLBACK ONLY — tap using screenshot coordinates
kimiclaw-cli '{"ui": "tap", "x": 241, "y": 770, "image_coords": true}'

# Input text
kimiclaw-cli '{"ui": "input", "text": "Hello World", "index": 0}'

# Clear + input + submit (after page transition, ALWAYS add timeout_ms)
kimiclaw-cli '{"ui": "input", "text": "Hello World", "clear": true, "submit": true, "timeout_ms": 5000}'

# Input to specific field
kimiclaw-cli '{"ui": "input", "text": "Hello World", "target": {"clazz": "EditText"}, "clear": true}'
```

## Swipe

```bash
# Scroll down
kimiclaw-cli '{"ui": "swipe", "x1": 540, "y1": 1800, "x2": 540, "y2": 600}'

# Swipe left
kimiclaw-cli '{"ui": "swipe", "x1": 900, "y1": 1200, "x2": 100, "y2": 1200}'

# Custom duration
kimiclaw-cli '{"ui": "swipe", "x1": 540, "y1": 1800, "x2": 540, "y2": 600, "duration_ms": 800}'

# Screenshot coordinates (auto-converted)
kimiclaw-cli '{"ui": "swipe", "x1": 241, "y1": 800, "x2": 241, "y2": 200, "image_coords": true}'
```

## Key Event

```bash
kimiclaw-cli '{"ui": "key_event", "key": "KEYCODE_BACK"}'
kimiclaw-cli '{"ui": "key_event", "key": "KEYCODE_HOME"}'
```

## Navigation & Popups

Use `KEYCODE_BACK` as the universal back/dismiss action. It works in native views AND WebViews.

- **Dismiss popups/dialogs**: `KEYCODE_BACK` first, then retry your action
- **Navigate back**: Prefer `KEYCODE_BACK` over `click {desc: "返回"}` — it works everywhere including WebViews
- **WebView detected?** (`"has_webview": true`): The tree will be limited. Use `KEYCODE_BACK` to leave, not click.

## Wait

Event types: `window_changed` (default), `content_changed`, `exists`

```bash
kimiclaw-cli '{"ui": "wait", "timeout_ms": 5000}'
kimiclaw-cli '{"ui": "wait", "event": "content_changed", "timeout_ms": 5000}'
kimiclaw-cli '{"ui": "wait", "event": "exists", "target": {"text_contains": "Done"}, "timeout_ms": 5000}'
```

## Notifications

```bash
kimiclaw-cli '{"ui": "notification"}'
kimiclaw-cli '{"ui": "notification", "mode": "active"}'
```

## Target Selector

Fields (AND logic): `text_equals`, `text_contains`, `content_desc` (exact), `content_desc_contains`, `resource_id`, `class_name`, `package_name`, `clickable`, `scrollable`, `enabled`, `index`

- `content_desc` is **exact match**. Use `content_desc_contains` for substring match.
- Prefer `resource_id` for stability. For scrolling, use `swipe` then `tree`.

## Send Screenshot

Take a screenshot and send it directly to the current chat session. The target session is auto-detected — no parameters needed.

```bash
kimiclaw-cli '{"ui": "send_screenshot"}'
```

Optional: add `"message": "caption text"` to include a text caption with the screenshot.

```bash
kimiclaw-cli '{"ui": "send_screenshot", "message": "Current screen state"}'
```

## Notify User

Send a short text progress message to the current chat. Session auto-detected — no parameters needed besides `text`.

```bash
kimiclaw-cli '{"ui": "notify_user", "text": "Xiaohongshu opened"}'
```

Optional: explicit `chat_id` and `channel` if cross-session targeting is needed (rare).

## Clipboard

Read or set the device clipboard. No permissions needed. Clipboard may contain sensitive data (passwords, tokens) — treat content with care.

```bash
# Get current clipboard content
kimiclaw-cli '{"clipboard_cmd": "get_clipboard"}'
# {"status": "success", "text": "clipboard content here", "has_text": true}

# Set clipboard content
kimiclaw-cli '{"clipboard_cmd": "set_clipboard", "text": "Hello World"}'
# {"status": "success", "clipboard_cmd": "set_clipboard"}
```

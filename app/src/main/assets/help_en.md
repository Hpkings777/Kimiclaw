# Basic Usage Tips

## Recommended Usage
- After installing the app, enable the "Allow Full Background Activity" permission and keep the app in the foreground
- Disable automatic system updates to prevent unexpected phone restarts caused by automatic upgrades

## How to Use the UI Automation Feature
- After enabling the Accessibility permission, keep the screen unlocked

## Backup & Restore
- It is recommended to encrypt and back up important configurations to phone storage via the "Backup & Restore" option in Settings. The backup path is the Documents/KimiClaw directory
- The Restore Backup feature: backup files include agent configurations, skill configurations, and some logs. Restoring will overwrite current configurations, so please use with caution

# Gateway Related Issues

## Agent Not Responding to Chat
- Check phone network connectivity
- Check if the agent is running, or restart the agent

## Gateway Fails to Start
- First try the "Restart" button on the Dashboard; if that doesn't work, go to Settings and tap "One-Click Repair"
- The default port is 18789. If it is occupied, it will cause a startup failure
- The last resort is to clear app data via Settings -> Storage -> Clear Data. Note that this will erase agent configurations, skill configurations, and some logs, so make sure to use the backup feature in Settings beforehand

## Huawei System Killing Background Processes
- Huawei systems may kill background processes, preventing the agent from working properly. When using on a Huawei phone, go to System Settings -> Apps -> KimiClaw -> Battery Details -> Startup Management, disable "Auto Management", and enable "Allow Background Activity", "Allow Associated Startup", and "Allow Background Activity".

## Xiaomi System
- Xiaomi systems kill background processes, so you need to grant KimiClaw auto-start and background running permissions. Go to System Settings -> Apps -> Manage Apps -> KimiClaw -> Permission Management, and enable "Auto-start" and "Background Activity".

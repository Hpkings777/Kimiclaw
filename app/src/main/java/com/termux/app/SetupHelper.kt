package com.termux.app

import android.app.Activity
import android.util.Log
import java.io.File

object SetupHelper {
    private const val TAG = "TermuxSetup"

    fun checkInstallation(activity: Activity, onComplete: () -> Unit) {
        val usrDir = File("/data/data/com.moonshot.kimiclaw/files/usr")

        if (!usrDir.exists()) {
            Log.i(TAG, "Bootstrap not installed: $usrDir does not exist")
            TermuxInstaller.setupBootstrap(activity, Runnable(onComplete))
            return
        }

        if (!usrDir.isDirectory) {
            Log.w(TAG, "Bootstrap check failed: $usrDir is not a directory")
            TermuxInstaller.setupBootstrap(activity, Runnable(onComplete))
            return
        }

        val binDir = File(usrDir, "bin")
        if (!binDir.exists() || !binDir.isDirectory) {
            Log.i(TAG, "Bootstrap not fully installed: bin directory missing")
            TermuxInstaller.setupBootstrap(activity, Runnable(onComplete))
            return
        }

        if (!File(usrDir, "bin/bash").exists()) {
            Log.i(TAG, "Bootstrap not fully installed: bash binary missing")
            TermuxInstaller.setupBootstrap(activity, Runnable(onComplete))
            return
        }

        Log.i(TAG, "Bootstrap is installed at $usrDir")
        Log.i(TAG, "Bootstrap already installed, skipping setup")
        onComplete()
    }
}

package com.termux.shared.net.socket.local

import android.app.Application
import android.content.Context

class AmExecutor(
    private val context: Context,
    private val args: Array<String>
) {
    var stdout: String = ""
    var stderr: String = ""

    fun execute() {
        if (args.isEmpty()) {
            stderr = "No arguments provided"
            return
        }

        try {
            val app = context.applicationContext as Application
            val result = StringBuilder()
            val errResult = StringBuilder()

            when (args[0]) {
                "start", "startservice" -> {
                    if (args.size < 2) {
                        errResult.appendLine("Usage: am start <intent>")
                    } else {
                        // FIXME: parse and execute intent
                        result.appendLine("Starting: ${args.drop(1).joinToString(" ")}")
                    }
                }
                "broadcast" -> {
                    result.appendLine("Broadcasting: ${args.drop(1).joinToString(" ")}")
                }
                "instrument" -> {
                    result.appendLine("Instrumenting: ${args.drop(1).joinToString(" ")}")
                }
                "force-stop" -> {
                    result.appendLine("Force stopping: ${args.drop(1).joinToString(" ")}")
                }
                else -> {
                    errResult.appendLine("Unknown command: ${args[0]}")
                }
            }

            stdout = result.toString()
            stderr = errResult.toString()
        } catch (e: Exception) {
            stderr = "Error: ${e.message}"
        }
    }
}

package com.termux.shared.termux.shell.am

import android.content.Context
import android.util.Log
import com.termux.shared.net.socket.local.LocalSocketManager
import com.termux.shared.net.socket.local.SocketClient
import com.termux.shared.net.socket.local.SocketServerConfig

class TermuxAmSocketServer {

    companion object {
        @Volatile
        var TERMUX_APP_AM_SOCKET_SERVER_ENABLED: Boolean? = null

        @Volatile
        var socketManager: LocalSocketManager? = null

        private const val SOCKET_TITLE = "TermuxAm"

        fun isEnabled(context: Context): Boolean? {
            if ("com.moonshot.kimiclaw" == context.packageName) {
                return TERMUX_APP_AM_SOCKET_SERVER_ENABLED
            }
            return null
        }

        fun start(context: Context) {
            Log.d("TermuxAmSocketServer", "Starting TermuxAm socket server since its enabled")

            synchronized(TermuxAmSocketServer::class.java) {
                socketManager?.let {
                    it.stop()
                    socketManager = null
                }

                val config = object : SocketServerConfig(SOCKET_TITLE) {}
                LocalSocketManager.init(context)
                val manager = LocalSocketManager(context, config)
                val error = manager.start()
                if (error != null) {
                    manager.handleError(null, error)
                } else if (manager.isRunning) {
                    Log.d("TermuxAmSocketServer", "TermuxAm socket server successfully started")
                    TERMUX_APP_AM_SOCKET_SERVER_ENABLED = true
                } else {
                    TERMUX_APP_AM_SOCKET_SERVER_ENABLED = false
                }
                socketManager = manager
            }
        }

        fun handleError(context: Context, client: SocketClient?, message: String) {
            Log.e("TermuxAmSocketServer", "Socket Server Error: $message")
        }
    }
}

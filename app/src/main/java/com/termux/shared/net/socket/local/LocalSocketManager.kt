package com.termux.shared.net.socket.local

import android.content.Context
import android.util.Log
import com.termux.shared.jni.models.JniResult

class LocalSocketManager(
    context: Context,
    val config: SocketServerConfig
) {
    val context: Context = context.applicationContext
    val exceptionHandler = Thread.UncaughtExceptionHandler { _, _ -> }
    @Volatile var isRunning = false

    private val serverSocket = LocalServerSocket(this, config)

    fun start(): String? {
        Log.d("LocalSocketManager", "start\n${config.toLogString()}")
        if (!nativeLoaded) {
            try {
                Log.d("LocalSocketManager", "Loading \"local-socket\" library")
                System.loadLibrary("local-socket")
                nativeLoaded = true
            } catch (t: Throwable) {
                val msg = "Failed to load local-socket library: ${t.message}"
                Log.e("LocalSocketManager", msg)
                return msg
            }
        }
        isRunning = true
        return serverSocket.start()
    }

    fun stop(): String? {
        if (!isRunning) return null
        Log.d("LocalSocketManager", "stop\n${config.toLogString()}")
        isRunning = false
        serverSocket.listenerThread.interrupt()
        val err = serverSocket.closeAndDeleteSocket()
        return err ?: serverSocket.deleteSocketFile()
    }

    fun handleClient(client: SocketClient) {
        Thread {
            try {
                client.configureTimeouts()
                val sb = StringBuilder()
                try {
                    java.io.InputStreamReader(client.inputStream).use { reader ->
                        var c = reader.read()
                        while (c > 0) {
                            sb.append(c.toChar())
                            c = reader.read()
                        }
                    }
                } catch (e: java.io.IOException) {
                    // FIXME: create error CH
                }

                val command = sb.toString().trim()
                Log.d("AmSocketServer", "am command received from peer ${client.peerCred.getMinimalString()}: `$command`")
                executeAmCommand(client, command)
            } catch (e: Exception) {
                Log.e("AmSocketServer", "Client handler failed", e)
            } finally {
                client.closeSilently()
            }
        }.apply {
            uncaughtExceptionHandler = exceptionHandler
            start()
        }
    }

    private fun executeAmCommand(client: SocketClient, command: String) {
        if (command.isBlank()) {
            sendClientResponse(client, "", "Empty command")
            return
        }
        val args = command.split(" ").toTypedArray()
        Log.d("AmSocketServer", "am command: ${args.joinToString(" ")}")
        val stdout = StringBuilder()
        val stderr = StringBuilder()
        try {
            val am = AmExecutor(context, args)
            am.execute()
            stdout.append(am.stdout)
            stderr.append(am.stderr)
        } catch (e: Exception) {
            stderr.appendLine(e.message)
        }
        sendClientResponse(client, stdout.toString(), stderr.toString())
    }

    private fun sendClientResponse(client: SocketClient, stdout: String, stderr: String) {
        val response = buildString {
            if (stdout.isNotEmpty()) appendLine(stdout.trimEnd())
            if (stderr.isNotEmpty()) appendLine("ERROR: $stderr".trimEnd())
        }
        try {
            client.writeString(response)
        } catch (_: Exception) {}
    }

    fun handleError(client: SocketClient?, message: String) {
        Log.e("LocalSocketManager", message)
    }

    fun runOnThread(runnable: Runnable) {
        Thread(runnable).apply {
            uncaughtExceptionHandler = exceptionHandler
            start()
        }
    }

    companion object {
        @Volatile
        private var nativeLoaded = false

        lateinit var appContext: Context
            private set

        private var appContextRef: Context? = null

        fun init(context: Context) {
            appContext = context.applicationContext
            appContextRef = appContext
        }

        @JvmStatic
        fun accept(socketName: String, serverFd: Int): JniResult? {
            return try {
                acceptNative(socketName, serverFd)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in acceptNative()", t)
                JniResult("Exception in acceptNative()", t)
            }
        }

        @JvmStatic
        fun read(socketName: String, fd: Int, buffer: ByteArray): JniResult? {
            return try {
                readNative(socketName, fd, buffer, 0)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in readNative()", t)
                JniResult("Exception in readNative()", t)
            }
        }

        @JvmStatic
        fun send(socketName: String, fd: Int, data: ByteArray): JniResult? {
            return try {
                sendNative(socketName, fd, data, 0)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in sendNative()", t)
                JniResult("Exception in sendNative()", t)
            }
        }

        @JvmStatic
        fun closeSocket(socketName: String, fd: Int): JniResult? {
            return try {
                closeSocketNative(socketName, fd)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in closeSocketNative()", t)
                JniResult("Exception in closeSocketNative()", t)
            }
        }

        @JvmStatic
        fun createServerSocket(socketName: String, pathBytes: ByteArray, backlog: Int): JniResult? {
            return try {
                createServerSocketNative(socketName, pathBytes, backlog)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in createServerSocketNative()", t)
                JniResult("Exception in createServerSocketNative()", t)
            }
        }

        @JvmStatic
        fun getPeerCred(socketName: String, fd: Int, peerCred: PeerCred): JniResult? {
            return try {
                getPeerCredNative(socketName, fd, peerCred)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in getPeerCredNative()", t)
                JniResult("Exception in getPeerCredNative()", t)
            }
        }

        @JvmStatic
        fun available(socketName: String, fd: Int): JniResult? {
            return try {
                availableNative(socketName, fd)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in availableNative()", t)
                JniResult("Exception in availableNative()", t)
            }
        }

        @JvmStatic
        fun setReadTimeout(socketName: String, fd: Int, timeout: Int): JniResult? {
            return try {
                setSocketReadTimeoutNative(socketName, fd, timeout)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in setSocketReadTimeoutNative()", t)
                JniResult("Exception in setSocketReadTimeoutNative()", t)
            }
        }

        @JvmStatic
        fun setSendTimeout(socketName: String, fd: Int, timeout: Int): JniResult? {
            return try {
                setSocketSendTimeoutNative(socketName, fd, timeout)
            } catch (t: Throwable) {
                Log.e("LocalSocketManager", "Exception in setSocketSendTimeoutNative()", t)
                JniResult("Exception in setSocketSendTimeoutNative()", t)
            }
        }

        private external fun acceptNative(socketName: String, serverFd: Int): JniResult
        private external fun readNative(socketName: String, fd: Int, buffer: ByteArray, deadline: Long): JniResult
        private external fun sendNative(socketName: String, fd: Int, data: ByteArray, deadline: Long): JniResult
        private external fun closeSocketNative(socketName: String, fd: Int): JniResult
        private external fun createServerSocketNative(socketName: String, pathBytes: ByteArray, backlog: Int): JniResult
        private external fun getPeerCredNative(socketName: String, fd: Int, peerCred: PeerCred): JniResult
        private external fun availableNative(socketName: String, fd: Int): JniResult
        private external fun setSocketReadTimeoutNative(socketName: String, fd: Int, timeout: Int): JniResult
        private external fun setSocketSendTimeoutNative(socketName: String, fd: Int, timeout: Int): JniResult
    }
}

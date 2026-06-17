package com.termux.shared.net.socket.local

import android.util.Log
import com.termux.shared.jni.models.JniResult
import java.io.Closeable
import java.io.File
import java.nio.charset.StandardCharsets

class LocalServerSocket(
    private val manager: LocalSocketManager,
    private val config: SocketServerConfig
) : Closeable {

    private val listenerThread = Thread(ClientSocketListener(this))
    private var running = false

    fun start(): String? {
        Log.d("LocalServerSocket", "start")

        val path = config.path
        if (path.isNullOrEmpty()) return "Path is null or empty"

        val finalPath = if (!config.isAbstractNamespaceSocket && path.startsWith("/")) {
            path
        } else path

        if (!config.isAbstractNamespaceSocket) {
            val parentFile = File(finalPath).parentFile
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs()
            }
            deleteSocketFileIfExists()
        }

        val pathBytes = finalPath.toByteArray(StandardCharsets.UTF_8)
        val backlog = config.backlog ?: 50
        val result = LocalSocketManager.createServerSocket(
            config.getSocketName() + " (server)", pathBytes, backlog
        )

        if (result != null && result.retval == 0) {
            val newFd = result.intData
            config.fd = newFd
            listenerThread.uncaughtExceptionHandler = manager.exceptionHandler
            listenerThread.start()
            running = true
            return null
        }
        return "Failed to create server socket: ${JniResult.getErrorString(result)}"
    }

    private fun deleteSocketFileIfExists() {
        if (!config.isAbstractNamespaceSocket) {
            val file = File(config.path)
            if (file.exists()) {
                LocalSocketManager.closeSocket(config.getSocketName() + " (server)", config.fd)
                file.delete()
            }
        }
    }

    fun acceptClient(): SocketClient? {
        while (running) {
            val configFd = config.fd
            if (configFd < 0) return null

            val result = LocalSocketManager.accept(
                config.getSocketName() + " (client)", configFd
            )

            if (result == null || result.retval != 0) {
                manager.handleError(null, "Accept failed: ${JniResult.getErrorString(result)}")
                continue
            }

            val clientFd = result.intData
            if (clientFd < 0) {
                manager.handleError(null, "Invalid client FD: $clientFd")
                continue
            }

            val peerCred = PeerCred()
            val credResult = LocalSocketManager.getPeerCred(
                config.getSocketName() + " (client)", clientFd, peerCred
            )

            if (credResult == null || credResult.retval != 0) {
                manager.handleError(null, "getPeerCred failed: ${JniResult.getErrorString(credResult)}")
                SocketClient(config, clientFd, PeerCred()).closeSilently()
                continue
            }

            if (peerCred.uid < 0) {
                manager.handleError(null, "Invalid peer UID: ${peerCred.uid}")
                SocketClient(config, clientFd, PeerCred()).closeSilently()
                continue
            }

            val client = SocketClient(config, clientFd, peerCred)
            Log.d("LocalServerSocket", "Client socket accept for \"${config.title}\" server\n${client.toLogString()}")

            val appUid = manager.context.applicationInfo.uid
            if (peerCred.uid == appUid || peerCred.uid == 0) {
                return client
            }

            manager.handleError(client, "Peer UID ${peerCred.uid} not allowed")
            client.closeSilently()
        }
        return null
    }

    override fun close() {
        Log.d("LocalServerSocket", "close")
        running = false
        if (config.fd >= 0) {
            val result = LocalSocketManager.closeSocket(config.getSocketName() + " (server)", config.fd)
            if (result == null || result.retval != 0) {
                Log.w("LocalServerSocket", "Close failed: ${JniResult.getErrorString(result)}")
            }
            config.fd = -1
        }
    }

    fun closeAndDeleteSocket(): String? {
        Log.d("LocalServerSocket", "closeServerSocket")
        try {
            close()
        } catch (e: Exception) {
            return "Close failed: ${e.message}"
        }
        return null
    }

    fun deleteSocketFile(): String? {
        return if (!config.isAbstractNamespaceSocket) {
            try {
                File(config.path).delete()
                null
            } catch (e: Exception) {
                "Failed to delete socket file: ${e.message}"
            }
        } else null
    }
}

class ClientSocketListener(
    private val serverSocket: LocalServerSocket
) : Runnable {
    override fun run() {
        Log.d("LocalServerSocket", "ClientSocketListener start")
        try {
            while (!Thread.currentThread().isInterrupted()) {
                val client = serverSocket.acceptClient() ?: continue
                client.configureTimeouts()
                serverSocket.manager.handleClient(client)
            }
        } catch (_: Exception) {
        } finally {
            serverSocket.close()
        }
        Log.d("LocalServerSocket", "ClientSocketListener end")
    }
}

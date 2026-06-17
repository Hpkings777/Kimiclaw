package com.termux.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import com.termux.terminal.TerminalSession
import com.termux.terminal.TerminalSessionClient
import com.moonshot.kimiclaw.KimiClawService
import com.moonshot.kimiclaw.R

class TermuxService : Service(), TerminalSessionClient {

    companion object {
        const val CHANNEL_ID = "termux-service"
        const val NOTIFICATION_ID = 1002
        const val ACTION_STOP = "com.termux.service_stop"

        @Volatile
        var instance: TermuxService? = null
    }

    private val binder = TermuxServiceBinder()
    val sessions = mutableListOf<TerminalSession>()
    var currentSession: TerminalSession? = null
        private set
    private var wakeLock: android.os.PowerManager.WakeLock? = null

    inner class TermuxServiceBinder : Binder() {
        fun getService(): TermuxService = this@TermuxService
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannel()
        acquireWakeLock()

        if (sessions.isEmpty()) {
            createTermuxSession()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        startForeground(NOTIFICATION_ID, buildNotification())
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        sessions.forEach { it.finish() }
        sessions.clear()
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Termux Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Termux terminal service"
                setShowBadge(false)
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            packageManager.getLaunchIntentForPackage(packageName),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val sessionCount = sessions.size
        val contentText = if (sessionCount == 1) {
            "1 terminal session"
        } else {
            "$sessionCount terminal sessions"
        }

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Termux")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    private fun acquireWakeLock() {
        val pm = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        wakeLock = pm.newWakeLock(
            android.os.PowerManager.PARTIAL_WAKE_LOCK,
            "termux:service"
        ).apply { acquire() }
    }

    fun createTermuxSession(
        executable: String = "/data/data/com.moonshot.kimiclaw/files/usr/bin/bash",
        cwd: String = "/data/data/com.moonshot.kimiclaw/files/home",
        argv: Array<String>? = arrayOf("bash", "--login"),
        envp: Array<String>? = null
    ): TerminalSession {
        val session = TerminalSession(executable, cwd, argv, envp, null, this)
        sessions.add(session)
        currentSession = session
        session.initialize(80, 24, 1024, 768)
        updateNotification()
        return session
    }

    fun switchToSession(index: Int) {
        if (index in sessions.indices) {
            currentSession = sessions[index]
        }
    }

    fun closeSession(session: TerminalSession) {
        session.finish()
        sessions.remove(session)
        if (currentSession == session) {
            currentSession = sessions.lastOrNull()
        }
        updateNotification()
    }

    private fun updateNotification() {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, buildNotification())
    }

    override fun onSessionUpdated(session: TerminalSession) {
        if (session == currentSession) {
            // Notify any bound activity
        }
    }

    override fun onSessionFinished(session: TerminalSession) {
        closeSession(session)
    }

    override fun onSessionStarted(pid: Int, session: TerminalSession) {
        android.util.Log.d("TermuxService", "Session started: pid=$pid")
    }
}

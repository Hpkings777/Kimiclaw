package com.moonshot.kimiclaw

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import com.moonshot.kimiclaw.openclaw.BackupCrypto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.Locale
import java.util.concurrent.TimeUnit

data class ShellResult(
    val output: String,
    val exitCode: Int,
    val error: String,
    val success: Boolean
)

class KimiClawService : Service() {

    companion object {
        private const val CHANNEL_ID = "kimi_claw_service"
        private const val NOTIFICATION_ID = 1001

        @Volatile
        var j: Job? = null

        @Volatile
        var k: WeakReference<KimiClawService>? = null
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var wakeLock: PowerManager.WakeLock? = null
    private var wifiLock: WifiManager.WifiLock? = null
    private var notificationText: String? = null

    inner class ServiceBinder : android.os.Binder() {
        fun getService(): KimiClawService = this@KimiClawService
    }

    private val binder = ServiceBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        k = WeakReference(this)
        createNotificationChannel()
        acquireLocks()
        updateNotification(null)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return try {
            startForegroundService()
            START_STICKY
        } catch (e: Exception) {
            if (Build.VERSION.SDK_INT >= 31 && e is android.app.ForegroundServiceStartNotAllowedException) {
                stopSelf()
                START_NOT_STICKY
            } else throw e
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        j?.cancel()
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
        wifiLock?.let {
            if (it.isHeld) it.release()
        }
        scope.cancel()
        if (k?.get() == this) k = null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.notification_channel_description)
            setShowBadge(false)
        }
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    private fun startForegroundService() {
        val notification = buildNotification(
            notificationText ?: getString(R.string.notification_running)
        )
        when {
            Build.VERSION.SDK_INT >= 34 -> startForeground(NOTIFICATION_ID, notification, 0)
            Build.VERSION.SDK_INT >= 29 -> startForeground(NOTIFICATION_ID, notification, 1)
            else -> startForeground(NOTIFICATION_ID, notification)
        }
    }

    fun updateNotification(text: String?) {
        synchronized(this) {
            notificationText = text
            if (wakeLock != null) {
                val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val content = text ?: getString(R.string.notification_running)
                nm.notify(NOTIFICATION_ID, buildNotification(content))
            }
        }
    }

    private fun buildNotification(text: String): Notification {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val displayText = if (text.length > 5120) text.substring(0, 5120) else text
        val title = if ("Kimi Claw".length > 5120) "Kimi Claw".substring(0, 5120) else "Kimi Claw"

        return Notification.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(displayText)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setPriority(Notification.PRIORITY_LOW)
            .setShowWhen(true)
            .build()
    }

    private fun acquireLocks() {
        if (wakeLock == null) {
            val powerManager = getSystemService(POWER_SERVICE) as PowerManager
            val tag = "kimiclaw:service-wakelock".lowercase(Locale.getDefault())
            powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag).also {
                it.acquire()
                wakeLock = it
            }
        }
        if (wifiLock == null) {
            val wm = getSystemService(WIFI_SERVICE) as WifiManager
            val tag = "kimiclaw".lowercase(Locale.getDefault())
            wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, tag)?.let {
                it.acquire()
                wifiLock = it
            }
        }
    }

    fun runCommand(command: String, callback: (ShellResult) -> Unit) {
        scope.launch {
            try {
                val process = ProcessBuilder()
                    .command("sh", "-c", command)
                    .directory(getDir("bin", MODE_PRIVATE))
                    .redirectErrorStream(false)
                    .start()
                val output = process.inputStream.bufferedReader().readText()
                val error = process.errorStream.bufferedReader().readText()
                val exitCode = process.waitFor()
                callback(ShellResult(output, exitCode, error, exitCode == 0))
            } catch (e: Exception) {
                callback(ShellResult("", -1, e.message ?: "Unknown error", false))
            }
        }
    }

    fun runScript(
        scriptName: String,
        timeoutMs: Long = 360_000L,
        onLog: (String) -> Unit = {},
        onResult: (ShellResult) -> Unit = {}
    ) {
        scope.launch {
            try {
                val scriptBytes = BackupCrypto.a(this@KimiClawService, scriptName)
                if (scriptBytes == null) {
                    onResult(ShellResult("", -1, "Failed to decrypt script: $scriptName", false))
                    return@launch
                }

                val scriptPath = cacheScript(scriptName, scriptBytes)
                val process = ProcessBuilder()
                    .command("sh", scriptPath)
                    .directory(filesDir)
                    .redirectErrorStream(false)
                    .start()

                val output = StringBuilder()

                val reader = launch(Dispatchers.IO) {
                    process.inputStream.bufferedReader().use { br ->
                        br.lines().forEach { line ->
                            output.appendLine(line)
                            onLog(line)
                        }
                    }
                }
                val errorReader = launch(Dispatchers.IO) {
                    process.errorStream.bufferedReader().use { br ->
                        br.lines().forEach { line ->
                            output.appendLine(line)
                            onLog(line)
                        }
                    }
                }

                val finished = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)
                reader.join()
                errorReader.join()

                if (!finished) {
                    process.destroyForcibly()
                    onResult(ShellResult(output.toString(), -2, "Timeout (${timeoutMs}ms)", false))
                } else {
                    val exitCode = process.exitValue()
                    onResult(ShellResult(output.toString(), exitCode, "", exitCode == 0))
                }
            } catch (e: Exception) {
                onResult(ShellResult("", -1, e.message ?: "Unknown error", false))
            }
        }
    }

    private fun cacheScript(name: String, data: ByteArray): String {
        val scriptFile = java.io.File(cacheDir, "scripts/$name.sh")
        scriptFile.parentFile?.mkdirs()
        scriptFile.writeBytes(data)
        scriptFile.setExecutable(true)
        return scriptFile.absolutePath
    }

}

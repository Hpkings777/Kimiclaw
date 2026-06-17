package com.moonshot.kimiclaw.api.automation

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.app.Notification
import android.content.Intent
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import com.moonshot.kimiclaw.KimiClawService
import com.moonshot.kimiclaw.R
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReference

@SuppressLint("AccessibilityServiceUsage")
class KimiClawAccessibilityService : AccessibilityService() {

    companion object {
        @JvmStatic
        var currentInstance: WeakReference<KimiClawAccessibilityService>? = null

        @JvmStatic
        var isConnected: Boolean = false
    }

    private val latchHolder = AtomicReference<CountDownLatch?>(null)
    private val notificationQueue = ConcurrentLinkedQueue<TimestampedNotification>()
    private var currentClassName = ""
    private val lastWindowChangeTime = AtomicLong(0)
    private val lastNotificationTime = AtomicLong(0)
    private val lastNotificationCheckTime = AtomicLong(0)
    private var lastNotificationPackage = ""
    private var overlayView: View? = null

    fun waitForStability(
        baseTime: Long,
        stableThreshold: Long,
        timeout: Long
    ): String? {
        val deadline = baseTime + timeout
        val effectiveThreshold = minOf(stableThreshold, 150L)

        while (true) {
            val now = System.currentTimeMillis()
            if (now >= deadline) return null

            val lastChange = lastWindowChangeTime.get()
            val lastNotif = lastNotificationTime.get()
            val hasWindowChange = lastChange > baseTime
            val hasNotification = lastNotif > baseTime

            if (!hasWindowChange && !hasNotification) {
                val remaining = deadline - System.currentTimeMillis()
                if (remaining <= 0) return null
                val latch = CountDownLatch(1)
                latchHolder.set(latch)
                if (maxOf(lastWindowChangeTime.get(), lastNotificationTime.get()) > baseTime) {
                    latchHolder.set(null)
                } else {
                    latch.await(minOf(remaining, 200L), TimeUnit.MILLISECONDS)
                    latchHolder.set(null)
                }
            } else {
                val threshold = if (hasWindowChange) stableThreshold else effectiveThreshold
                val maxTime = maxOf(lastChange, lastNotif)
                val elapsed = System.currentTimeMillis() - maxTime
                if (elapsed >= threshold) return "stable"
                val waitTime = minOf(threshold - elapsed, deadline - System.currentTimeMillis())
                if (waitTime <= 0) return "settled_after_change"
                val latch = CountDownLatch(1)
                latchHolder.set(latch)
                if (maxOf(lastWindowChangeTime.get(), lastNotificationTime.get()) > maxTime) {
                    latchHolder.set(null)
                } else {
                    latch.await(waitTime, TimeUnit.MILLISECONDS)
                    latchHolder.set(null)
                }
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                event.packageName?.toString()
                event.className?.toString()?.let { currentClassName = it }
                lastWindowChangeTime.set(System.currentTimeMillis())
                (latchHolder.get() as? CountDownLatch)?.countDown()
            }
            AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED -> {
                val pkg = event.packageName?.toString() ?: ""
                val textList = event.text
                val text = textList?.joinToString(separator = " ")?.trim() ?: ""

                val finalText = if (text.isNotEmpty()) {
                    text
                } else {
                    val parcelable = event.parcelableData
                    if (parcelable is Notification) {
                        val extras = parcelable.extras
                        val title = extras?.getString("android.title") ?: ""
                        val subText = extras?.getCharSequence("android.subText") ?: ""
                        val bigText = extras?.getCharSequence("android.bigText") ?: ""
                        val text2 = extras?.getCharSequence("android.text") ?: ""
                        val parts = mutableListOf<String>()
                        if (title.isNotEmpty()) parts.add(title)
                        if (subText.isNotEmpty()) parts.add(subText.toString())
                        if (bigText.isNotEmpty()) parts.add(bigText.toString())
                        else if (text2.isNotEmpty()) parts.add(text2.toString())
                        parts.joinToString(separator = " ")
                    } else ""
                }

                if (finalText.isNotEmpty()) {
                    notificationQueue.offer(TimestampedNotification(pkg, finalText, System.currentTimeMillis()))
                }

                val now = System.currentTimeMillis()
                val lastCheck = lastNotificationCheckTime.get()
                if (pkg != lastNotificationPackage || now - lastCheck >= 50) {
                    lastNotificationTime.set(now)
                    lastNotificationCheckTime.set(now)
                    lastNotificationPackage = pkg
                    (latchHolder.get() as? CountDownLatch)?.countDown()
                }
            }
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                val now = System.currentTimeMillis()
                val pkg = event.packageName?.toString() ?: ""
                lastNotificationTime.set(now)
                lastNotificationCheckTime.set(now)
                val prevPkg = lastNotificationPackage
                lastNotificationPackage = pkg
                if (pkg != prevPkg || now - lastNotificationCheckTime.get() >= 50) {
                    (latchHolder.get() as? CountDownLatch)?.countDown()
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        currentInstance = WeakReference(this)
        isConnected = true

        if (overlayView == null) {
            try {
                val wm = getSystemService(WINDOW_SERVICE) as WindowManager
                val params = WindowManager.LayoutParams(
                    1, 1,
                    WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    android.graphics.PixelFormat.TRANSPARENT
                )
                params.gravity = android.view.Gravity.START or android.view.Gravity.TOP
                val view = View(this)
                view.keepScreenOn = true
                overlayView = view
                wm.addView(view, params)
            } catch (_: Exception) {}
        }

        startForegroundService(Intent(this, KimiClawService::class.java))
        val text = getString(R.string.notification_accessibility_running)
        KimiClawService.k?.get()?.updateNotification(text)
    }

    override fun onInterrupt() {
        (latchHolder.get() as? CountDownLatch)?.countDown()
        latchHolder.set(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        overlayView?.let {
            try {
                val wm = getSystemService(WINDOW_SERVICE) as WindowManager
                wm.removeView(it)
            } catch (_: Exception) {}
            overlayView = null
        }
        if (currentInstance?.get() == this) {
            currentInstance = null
            isConnected = false
        }
        val text = getString(R.string.notification_running)
        KimiClawService.k?.get()?.updateNotification(text)
    }

    data class TimestampedNotification(
        val packageName: String,
        val text: String,
        val timestamp: Long
    )
}

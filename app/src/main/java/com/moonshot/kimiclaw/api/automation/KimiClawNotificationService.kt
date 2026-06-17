package com.moonshot.kimiclaw.api.automation

import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class KimiClawNotificationService : NotificationListenerService() {

    companion object {
        @Volatile
        var currentInstance: KimiClawNotificationService? = null
    }

    @Volatile
    var isConnected: Boolean = false

    override fun onListenerConnected() {
        super.onListenerConnected()
        isConnected = true
        currentInstance = this
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        isConnected = false
        if (currentInstance == this) currentInstance = null
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let {
            it.packageName
            it.id
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        sbn?.let {
            it.packageName
            it.id
        }
    }

    companion object {
        @JvmStatic
        fun notificationText(sbn: StatusBarNotification): String {
            val extras = sbn.notification.extras
            val title = extras?.getString("android.title") ?: ""
            val text = extras?.getCharSequence("android.text") ?: ""
            val bigText = extras?.getCharSequence("android.bigText") ?: ""
            val subText = extras?.getCharSequence("android.subText") ?: ""

            val parts = mutableListOf<String>()
            if (title.isNotEmpty()) parts.add(title)
            if (subText.isNotEmpty()) parts.add(subText.toString())
            if (bigText.isNotEmpty()) parts.add(bigText.toString())
            else if (text.isNotEmpty()) parts.add(text.toString())

            return parts.joinToString(separator = " - ")
        }
    }
}

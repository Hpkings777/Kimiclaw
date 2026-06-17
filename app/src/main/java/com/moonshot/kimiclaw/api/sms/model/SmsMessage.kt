package com.moonshot.kimiclaw.api.sms.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsMessage(
    val address: String = "",
    val body: String = "",
    val contactName: String = "",
    val date: String = "",
    val isMms: Boolean = false,
    val isRead: Boolean = false,
    val messageId: Long = 0L,
    val threadId: Long = 0L,
    val type: String = ""
)

package com.moonshot.kimiclaw.api.sms.model

import kotlinx.serialization.Serializable

@Serializable
data class SmsConversation(
    val address: String = "",
    val contactName: String = "",
    val date: String = "",
    val messageCount: Int = 0,
    val snippet: String = "",
    val threadId: Long = 0L,
    val unreadCount: Int = 0
)

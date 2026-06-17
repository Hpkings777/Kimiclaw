package com.moonshot.kimiclaw.api.email.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailFolder(
    val fullName: String = "",
    val messageCount: Int = 0,
    val name: String = "",
    val unreadCount: Int = 0
)

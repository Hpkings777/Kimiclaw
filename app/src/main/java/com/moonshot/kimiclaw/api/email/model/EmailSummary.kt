package com.moonshot.kimiclaw.api.email.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailSummary(
    val date: String = "",
    val from: String = "",
    val hasAttachments: Boolean = false,
    val isRead: Boolean = false,
    val snippet: String = "",
    val subject: String = "",
    val to: List<String> = emptyList(),
    val uid: Long = 0L
)

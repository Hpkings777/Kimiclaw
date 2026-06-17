package com.moonshot.kimiclaw.api.email.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailAccount(
    val displayName: String = "",
    val email: String = "",
    val id: String = "",
    val imapHost: String = "",
    val imapPort: Int = 993,
    val password: String = "",
    val provider: EmailProvider = EmailProvider.CUSTOM,
    val smtpHost: String = "",
    val smtpPort: Int = 587,
    val ssl: Boolean = true
)

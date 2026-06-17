package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val publicKey: String,
    val authScope: Int,
    val botId: String = ""
)

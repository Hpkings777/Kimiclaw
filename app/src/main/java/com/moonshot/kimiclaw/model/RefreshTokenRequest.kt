package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String = ""
)

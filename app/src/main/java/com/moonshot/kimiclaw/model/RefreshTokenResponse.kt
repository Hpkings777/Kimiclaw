package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val accessToken: String = "",
    val refreshToken: String = ""
)

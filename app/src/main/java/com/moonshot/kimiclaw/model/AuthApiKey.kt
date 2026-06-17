package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthApiKey(
    val apiKey: String = ""
) {
    fun valid() = apiKey.length > 0
}

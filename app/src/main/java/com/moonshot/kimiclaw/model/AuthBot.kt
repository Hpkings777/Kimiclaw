package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthBot(
    val botId: String = "",
    val botToken: String = ""
) {
    fun valid() = botId.length > 0 && botToken.length > 0
}

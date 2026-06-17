package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthAccount(
    val id: String = "",
    val name: String = "",
    val avatar: String = "",
    var accessToken: String = "",
    val refreshToken: String = ""
) {
    fun valid() = accessToken.length > 0 && refreshToken.length > 0
}

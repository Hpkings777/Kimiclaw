package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class ClawAuthData(
    val account: AuthAccount = AuthAccount(),
    val apiKey: AuthApiKey = AuthApiKey(),
    val bot: AuthBot = AuthBot()
) {
    fun isLoggedIn() = account.valid() && apiKey.valid()

    fun isValid(flags: Int): Boolean {
        if (flags and 1 != 0 && !account.valid()) return false
        if (flags and 2 != 0 && !apiKey.valid()) return false
        if (flags and 4 != 0 && !bot.valid()) return false
        return true
    }
}

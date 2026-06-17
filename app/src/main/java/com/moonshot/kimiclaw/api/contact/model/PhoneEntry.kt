package com.moonshot.kimiclaw.api.contact.model

import kotlinx.serialization.Serializable

@Serializable
data class PhoneEntry(
    val number: String = "",
    val type: String = ""
)

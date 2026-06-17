package com.moonshot.kimiclaw.api.contact.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailEntry(
    val email: String = "",
    val type: String = ""
)

package com.moonshot.kimiclaw.api.contact.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactSummary(
    val contactId: Long = 0L,
    val emails: List<String> = emptyList(),
    val name: String = "",
    val phones: List<String> = emptyList(),
    val photoUri: String = ""
)

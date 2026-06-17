package com.moonshot.kimiclaw.api.contact.model

import kotlinx.serialization.Serializable

@Serializable
data class ContactDetail(
    val address: String = "",
    val birthday: String = "",
    val contactId: Long = 0L,
    val emails: List<EmailEntry> = emptyList(),
    val name: String = "",
    val notes: String = "",
    val organization: String = "",
    val phones: List<PhoneEntry> = emptyList(),
    val photoUri: String = "",
    val title: String = ""
)

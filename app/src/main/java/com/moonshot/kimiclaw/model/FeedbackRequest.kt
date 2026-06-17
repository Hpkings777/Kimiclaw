package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequest(
    val content: String,
    val emotionType: String,
    val fileObjectNames: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val chatId: String = "",
    val segmentId: String = "",
    val path: String = "",
    val phone: String = "",
    val feedbackType: String = "general",
    val thumbStatus: Int = 0,
    val target: String = ""
)

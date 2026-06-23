package dev.hermes.manager.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: String,
    val title: String,
    val preview: String,
    val lastUpdated: Long,
    val messageCount: Int
)

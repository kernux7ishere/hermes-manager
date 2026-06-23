package dev.hermes.manager.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

enum class Role { USER, ASSISTANT, SYSTEM }

@Serializable
data class Message(
    val id: String = UUID.randomUUID().toString(),
    val role: Role,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isStreaming: Boolean = false
)

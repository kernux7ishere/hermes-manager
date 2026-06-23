package dev.hermes.manager.data.model

import kotlinx.serialization.Serializable

enum class ConnectionMode { LOCAL, REMOTE }

@Serializable
data class ConnectionConfig(
    val mode: ConnectionMode = ConnectionMode.LOCAL,
    val gatewayUrl: String = "http://localhost:8080",
    val apiKey: String = "",
    val profile: String = "sylpha"
)

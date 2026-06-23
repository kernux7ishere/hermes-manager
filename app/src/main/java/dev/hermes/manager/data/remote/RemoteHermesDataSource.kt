package dev.hermes.manager.data.remote

import dev.hermes.manager.data.model.ConnectionConfig
import dev.hermes.manager.data.model.Session
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteHermesDataSource @Inject constructor(
    private val client: HttpClient
) {
    fun sendMessage(message: String, config: ConnectionConfig): Flow<String> = flow {
        client.webSocket("${config.gatewayUrl.replace("http", "ws")}/ws/chat") {
            send(Json.encodeToString(mapOf("message" to message, "profile" to config.profile)))
            for (frame in incoming) {
                if (frame is Frame.Text) emit(frame.readText())
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getSessions(config: ConnectionConfig): List<Session> = emptyList()
}

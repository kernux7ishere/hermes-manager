package dev.hermes.manager.data.local

import dev.hermes.manager.data.model.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalHermesDataSource @Inject constructor() {

    fun sendMessage(message: String, profile: String): Flow<String> = flow {
        val process = ProcessBuilder(
            "hermes", "--profile", profile, "ask", message
        )
            .redirectErrorStream(true)
            .start()

        val reader = process.inputStream.bufferedReader()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            emit(line!!)
        }
        process.waitFor()
    }.flowOn(Dispatchers.IO)

    suspend fun getSessions(profile: String): List<Session> = withContext(Dispatchers.IO) {
        try {
            val output = ProcessBuilder(
                "hermes", "--profile", profile, "sessions", "--json"
            )
                .start()
                .inputStream.bufferedReader().readText()
            Json.decodeFromString(output)
        } catch (e: Exception) {
            emptyList()
        }
    }
}

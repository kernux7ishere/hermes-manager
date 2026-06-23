package dev.hermes.manager.data.repository

import dev.hermes.manager.data.local.LocalHermesDataSource
import dev.hermes.manager.data.model.ConnectionMode
import dev.hermes.manager.data.model.Session
import dev.hermes.manager.data.remote.RemoteHermesDataSource
import dev.hermes.manager.data.store.ConnectionConfigStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HermesRepository @Inject constructor(
    private val local: LocalHermesDataSource,
    private val remote: RemoteHermesDataSource,
    private val configStore: ConnectionConfigStore
) {
    fun sendMessage(message: String): Flow<String> {
        // Note: Flow-based config read; caller collects
        return kotlinx.coroutines.flow.flow {
            val config = configStore.config.first()
            if (config.mode == ConnectionMode.LOCAL) {
                local.sendMessage(message, config.profile).collect { emit(it) }
            } else {
                remote.sendMessage(message, config).collect { emit(it) }
            }
        }
    }

    suspend fun getSessions(): List<Session> {
        val config = configStore.config.first()
        return if (config.mode == ConnectionMode.LOCAL)
            local.getSessions(config.profile)
        else
            remote.getSessions(config)
    }
}

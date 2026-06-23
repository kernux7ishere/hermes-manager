package dev.hermes.manager.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dev.hermes.manager.data.model.ConnectionConfig
import dev.hermes.manager.data.model.ConnectionMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hermes_config")

@Singleton
class ConnectionConfigStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val MODE = stringPreferencesKey("mode")
        val GATEWAY_URL = stringPreferencesKey("gateway_url")
        val API_KEY = stringPreferencesKey("api_key")
        val PROFILE = stringPreferencesKey("profile")
    }

    val configFlow: Flow<ConnectionConfig> = context.dataStore.data.map { prefs ->
        ConnectionConfig(
            mode = ConnectionMode.valueOf(prefs[Keys.MODE] ?: ConnectionMode.LOCAL.name),
            gatewayUrl = prefs[Keys.GATEWAY_URL] ?: "http://localhost:8080",
            apiKey = prefs[Keys.API_KEY] ?: "",
            profile = prefs[Keys.PROFILE] ?: "sylpha"
        )
    }

    suspend fun setMode(mode: ConnectionMode) { context.dataStore.edit { it[Keys.MODE] = mode.name } }
    suspend fun setGatewayUrl(url: String) { context.dataStore.edit { it[Keys.GATEWAY_URL] = url } }
    suspend fun setApiKey(key: String) { context.dataStore.edit { it[Keys.API_KEY] = key } }
    suspend fun setProfile(profile: String) { context.dataStore.edit { it[Keys.PROFILE] = profile } }
}

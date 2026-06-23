package dev.hermes.manager.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hermes.manager.data.model.ConnectionConfig
import dev.hermes.manager.data.model.ConnectionMode
import dev.hermes.manager.data.store.ConnectionConfigStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val store: ConnectionConfigStore
) : ViewModel() {

    val config: StateFlow<ConnectionConfig> = store.configFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectionConfig())

    fun setMode(mode: ConnectionMode) = viewModelScope.launch { store.setMode(mode) }
    fun setGatewayUrl(url: String) = viewModelScope.launch { store.setGatewayUrl(url) }
    fun setApiKey(key: String) = viewModelScope.launch { store.setApiKey(key) }
    fun setProfile(profile: String) = viewModelScope.launch { store.setProfile(profile) }
}

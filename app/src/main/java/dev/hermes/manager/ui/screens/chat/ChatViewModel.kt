package dev.hermes.manager.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.hermes.manager.data.model.Message
import dev.hermes.manager.data.model.Role
import dev.hermes.manager.data.repository.HermesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: HermesRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    fun onInputChange(text: String) { _inputText.value = text }

    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isBlank() || _isStreaming.value) return

        val userMsg = Message(role = Role.USER, content = text)
        _messages.update { it + userMsg }
        _inputText.value = ""

        val assistantId = UUID.randomUUID().toString()
        val assistantMsg = Message(id = assistantId, role = Role.ASSISTANT, content = "", isStreaming = true)
        _messages.update { it + assistantMsg }
        _isStreaming.value = true

        viewModelScope.launch {
            val buffer = StringBuilder()
            repository.sendMessage(text)
                .catch { e ->
                    _messages.update { msgs ->
                        msgs.map { if (it.id == assistantId) it.copy(content = "Error: ${e.message}", isStreaming = false) else it }
                    }
                    _isStreaming.value = false
                }
                .collect { chunk ->
                    buffer.append(chunk)
                    _messages.update { msgs ->
                        msgs.map { if (it.id == assistantId) it.copy(content = buffer.toString(), isStreaming = true) else it }
                    }
                }
            _messages.update { msgs ->
                msgs.map { if (it.id == assistantId) it.copy(isStreaming = false) else it }
            }
            _isStreaming.value = false
        }
    }
}

package dev.hermes.manager.ui.screens.chat

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.hermes.manager.data.model.Message
import dev.hermes.manager.data.model.Role
import dev.hermes.manager.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    onMenuClick: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isStreaming by viewModel.isStreaming.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.size - 1)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hermes", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Default.Menu, contentDescription = "Sessions")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HermesBg,
                    titleContentColor = HermesOnSurface
                )
            )
        },
        bottomBar = {
            MessageInputBar(
                text = inputText,
                onTextChange = viewModel::onInputChange,
                onSend = viewModel::sendMessage,
                enabled = !isStreaming
            )
        },
        containerColor = HermesBg
    ) { padding ->
        if (messages.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Hermes", style = MaterialTheme.typography.titleLarge, color = HermesOrange)
                    Spacer(Modifier.height(8.dp))
                    Text("How can I help you today?", color = HermesMutedText)
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                items(messages, key = { it.id }) { message ->
                    MessageBubble(message = message)
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message) {
    val isUser = message.role == Role.USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    color = if (isUser) HermesUserBubble else HermesAssistantBubble,
                    shape = RoundedCornerShape(
                        topStart = 18.dp, topEnd = 18.dp,
                        bottomStart = if (isUser) 18.dp else 4.dp,
                        bottomEnd = if (isUser) 4.dp else 18.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                Text(text = message.content, color = HermesOnSurface, style = MaterialTheme.typography.bodyLarge)
                if (message.isStreaming) {
                    StreamingDot()
                }
            }
        }
    }
}

@Composable
fun StreamingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "dot")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "dotAlpha"
    )
    Box(Modifier.size(8.dp).alpha(alpha).background(HermesOrange, RoundedCornerShape(50)))
}

@Composable
fun MessageInputBar(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, enabled: Boolean) {
    Surface(color = HermesBg, tonalElevation = 4.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message Hermes...", color = HermesMutedText) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() }),
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = HermesOrange,
                    unfocusedBorderColor = HermesDivider,
                    focusedTextColor = HermesOnSurface,
                    unfocusedTextColor = HermesOnSurface,
                    cursorColor = HermesOrange
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = onSend, enabled = enabled && text.isNotBlank()) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = if (enabled && text.isNotBlank()) HermesOrange else HermesMutedText)
            }
        }
    }
}

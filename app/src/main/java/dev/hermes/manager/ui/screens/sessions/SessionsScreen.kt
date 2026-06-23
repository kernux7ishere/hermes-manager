package dev.hermes.manager.ui.screens.sessions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.hermes.manager.data.model.Session
import dev.hermes.manager.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsScreen(
    sessions: List<Session>,
    onSessionClick: (Session) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Conversations", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = HermesBg,
                    titleContentColor = HermesOnSurface,
                    navigationIconContentColor = HermesOnSurface
                )
            )
        },
        containerColor = HermesBg
    ) { padding ->
        if (sessions.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Chat, contentDescription = null, tint = HermesMutedText, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("No conversations yet", color = HermesMutedText)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(sessions, key = { it.id }) { session ->
                    SessionItem(session = session, onClick = { onSessionClick(session) })
                    HorizontalDivider(color = HermesDivider, thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun SessionItem(session: Session, onClick: () -> Unit) {
    val dateStr = remember(session.lastUpdated) {
        SimpleDateFormat("MMM d", Locale.getDefault()).format(Date(session.lastUpdated))
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = session.title,
                style = MaterialTheme.typography.bodyLarge,
                color = HermesOnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = session.preview,
                style = MaterialTheme.typography.bodyMedium,
                color = HermesMutedText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(text = dateStr, style = MaterialTheme.typography.labelSmall, color = HermesMutedText)
    }
}

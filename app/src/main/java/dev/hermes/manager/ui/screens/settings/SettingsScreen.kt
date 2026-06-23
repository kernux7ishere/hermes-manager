package dev.hermes.manager.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.hermes.manager.data.model.ConnectionMode
import dev.hermes.manager.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val config by viewModel.config.collectAsState()
    var showApiKey by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Connection Mode
            Text("Connection Mode", style = MaterialTheme.typography.titleLarge, color = HermesOnSurface)
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                ConnectionMode.entries.forEachIndexed { index, mode ->
                    SegmentedButton(
                        selected = config.mode == mode,
                        onClick = { viewModel.setMode(mode) },
                        shape = SegmentedButtonDefaults.itemShape(index, ConnectionMode.entries.size),
                        colors = SegmentedButtonDefaults.colors(activeContainerColor = HermesOrange)
                    ) { Text(mode.name) }
                }
            }

            // Gateway URL (remote only)
            if (config.mode == ConnectionMode.REMOTE) {
                OutlinedTextField(
                    value = config.gatewayUrl,
                    onValueChange = viewModel::setGatewayUrl,
                    label = { Text("Gateway URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = hermesTextFieldColors()
                )
                OutlinedTextField(
                    value = config.apiKey,
                    onValueChange = viewModel::setApiKey,
                    label = { Text("API Key") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = if (showApiKey) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showApiKey = !showApiKey }) {
                            Icon(if (showApiKey) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
                        }
                    },
                    colors = hermesTextFieldColors()
                )
            }

            // Profile
            OutlinedTextField(
                value = config.profile,
                onValueChange = viewModel::setProfile,
                label = { Text("Hermes Profile") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = hermesTextFieldColors()
            )
        }
    }
}

@Composable
fun hermesTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = HermesOrange,
    unfocusedBorderColor = HermesDivider,
    focusedLabelColor = HermesOrange,
    focusedTextColor = HermesOnSurface,
    unfocusedTextColor = HermesOnSurface,
    cursorColor = HermesOrange
)

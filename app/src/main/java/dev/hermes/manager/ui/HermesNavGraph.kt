package dev.hermes.manager.ui

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.hermes.manager.data.model.Session
import dev.hermes.manager.ui.screens.chat.ChatScreen
import dev.hermes.manager.ui.screens.sessions.SessionsScreen
import dev.hermes.manager.ui.screens.settings.SettingsScreen

@Composable
fun HermesNavGraph() {
    val navController = rememberNavController()
    var sessions by remember { mutableStateOf<List<Session>>(emptyList()) }

    NavHost(navController = navController, startDestination = "chat") {
        composable("chat") {
            ChatScreen(
                onMenuClick = { navController.navigate("sessions") }
            )
        }
        composable("sessions") {
            SessionsScreen(
                sessions = sessions,
                onSessionClick = { navController.navigate("chat") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}

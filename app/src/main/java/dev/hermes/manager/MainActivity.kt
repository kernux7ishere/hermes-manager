package dev.hermes.manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import dev.hermes.manager.ui.HermesNavGraph
import dev.hermes.manager.ui.theme.HermesManagerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HermesManagerTheme {
                HermesNavGraph()
            }
        }
    }
}

package com.example.microapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Main App composable — matches JSX: export default function App()
 * Handles screen routing and bottom navigation.
 */
@Composable
fun InstaVaultApp() {
    var screen by remember { mutableStateOf("home") }

    val navScreens = listOf("home", "tasks", "games", "profile")

    // Active nav calculation — exact copy from JSX logic
    val activeNav = when {
        navScreens.contains(screen) -> screen
        screen == "task_success" -> "tasks"
        screen in listOf("quiz", "tap", "spin", "cards", "math", "words") -> "games"
        else -> "home"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(C.bg)
            .systemBarsPadding() // Prevents overlap with time/battery
    ) {
        // Screen content
        Box(modifier = Modifier.fillMaxSize()) {
            when (screen) {
                "home" -> HomeScreen(onNavigate = { screen = it })
                "tasks" -> TasksScreen(onNavigate = { screen = it })
                "games" -> GamesScreen(onNavigate = { screen = it })
                "quiz" -> QuizGame(onNavigate = { screen = it })
                "tap" -> TapGame(onNavigate = { screen = it })
                "spin" -> SpinWheel(onNavigate = { screen = it })
                "task_success" -> TaskSuccess(onNavigate = { screen = it })
                "profile" -> ProfileScreen()
                else -> HomeScreen(onNavigate = { screen = it })
            }
        }

        // Bottom Navigation — always visible, fixed at bottom
        BottomNav(
            active = activeNav,
            onNavigate = { screen = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

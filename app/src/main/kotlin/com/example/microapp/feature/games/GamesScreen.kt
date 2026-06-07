package com.example.microapp.feature.games

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.microapp.core.theme.C
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.microapp.navigation.Screen

@Composable
fun GamesScreen(onNavigate: (Screen) -> Unit, viewModel: GamesViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "🎮 Games",
            fontSize = 20.sp,
            fontWeight = FontWeight(900),
            color = C.white,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Task ke baad bonus Sparks kamao!",
            fontSize = 12.sp,
            color = C.grey,
            modifier = Modifier.padding(bottom = 18.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            uiState.games.chunked(2).forEach { rowGames ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowGames.forEach { g ->
                        val targetScreen = when (g.routeId) {
                            "quiz" -> Screen.Quiz
                            "tap" -> Screen.TapGame
                            "spin" -> Screen.SpinWheel
                            "cards" -> Screen.CardMatch
                            "math" -> Screen.MathRush
                            "words" -> Screen.WordHunt
                            else -> Screen.Games
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            g.color.copy(alpha = 0.09f),
                                            C.card
                                        )
                                    )
                                )
                                .border(1.dp, g.color.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                                .clickable {
                                    if (g.routeId == "cards" || g.routeId == "math" || g.routeId == "words") {
                                        Toast.makeText(context, "${g.title} is Coming Soon! ⏳", Toast.LENGTH_SHORT).show()
                                    } else {
                                        onNavigate(targetScreen)
                                    }
                                }
                                .padding(vertical = 18.dp, horizontal = 14.dp)
                        ) {
                            Text(
                                text = g.icon,
                                fontSize = 34.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Text(
                                text = g.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(800),
                                color = C.white,
                                modifier = Modifier.padding(bottom = 3.dp)
                            )
                            Text(
                                text = g.subtitle,
                                fontSize = 11.sp,
                                color = C.grey,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .background(g.color.copy(alpha = 0.13f), RoundedCornerShape(8.dp))
                                    .border(1.dp, g.color.copy(alpha = 0.27f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = g.reward,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight(700),
                                    color = g.color
                                )
                            }
                        }
                    }
                    if (rowGames.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

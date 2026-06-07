package com.example.microapp.feature.games.tap

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.microapp.core.components.AppCard
import com.example.microapp.core.components.Btn
import com.example.microapp.core.components.Tag
import com.example.microapp.core.theme.C
import com.example.microapp.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun TapGameScreen(onNavigate: (Screen) -> Unit, viewModel: TapGameViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Countdown timer
    LaunchedEffect(uiState.isStarted, uiState.isDone) {
        if (!uiState.isStarted || uiState.isDone) return@LaunchedEffect
        for (time in 30 downTo 1) {
            delay(1000)
            viewModel.decrementTimer()
        }
    }

    // Spark spawner every 700ms
    LaunchedEffect(uiState.isStarted, uiState.isDone) {
        if (!uiState.isStarted || uiState.isDone) return@LaunchedEffect
        while (!uiState.isDone) {
            delay(700)
            if (uiState.isDone) break
            viewModel.spawnSpark()
        }
    }

    // ── Result screen ──
    if (uiState.isDone) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🎯", fontSize = 60.sp, modifier = Modifier.padding(bottom = 12.dp))
            Text(
                "Time Up!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = C.white,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "${uiState.score} sparks tap kiye!",
                fontSize = 14.sp,
                color = C.grey,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            AppCard(
                glow = "green",
                contentPadding = PaddingValues(horizontal = 40.dp, vertical = 24.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    "+${uiState.reward}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    color = C.green,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "⚡ Sparks Earned!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = C.green,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Btn(
                text = "← Games Pe Wapas",
                onClick = { onNavigate(Screen.Games) },
                variant = "primary"
            )
        }
        return
    }

    // ── Game screen ──
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar: Back, Tag, Timer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "← Back",
                color = C.greyL,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigate(Screen.Games) }
            )
            Tag(text = "🎯 Tap Frenzy", color = C.green)
            Tag(text = "⏱️ ${uiState.timeLeft}s", color = if (uiState.timeLeft < 10) C.red else C.greyL)
        }

        // Score cards row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AppCard(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)) {
                Text(
                    "${uiState.score}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = C.gold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Tapped",
                    fontSize = 11.sp,
                    color = C.grey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AppCard(contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)) {
                Text(
                    "+${uiState.reward}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = C.green,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "Sparks",
                    fontSize = 11.sp,
                    color = C.grey,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (!uiState.isStarted) {
            // Start button centered
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Btn(
                    text = "🎯 Start Game!",
                    onClick = { viewModel.startGame() },
                    variant = "gold",
                    modifier = Modifier.width(180.dp)
                )
            }
        } else {
            // Game area
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(20.dp))
                    .background(C.cardL)
                    .border(1.dp, C.green.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
            ) {
                // Background watermark ⚡
                Text(
                    "⚡",
                    fontSize = 80.sp,
                    color = Color.White.copy(alpha = 0.04f),
                    modifier = Modifier.align(Alignment.Center)
                )

                // Sparks
                uiState.sparks.forEach { s ->
                    Text(
                        "⚡",
                        fontSize = s.size.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.TopStart)
                            .offset(
                                x = (s.x * 280).dp,
                                y = (s.y * 400).dp
                            )
                            .clickable {
                                viewModel.tapSpark(s.id)
                            }
                    )
                }

                // Bottom label
                Text(
                    "TAP THE SPARKS!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.3f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 12.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

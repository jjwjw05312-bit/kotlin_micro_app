package com.example.microapp.feature.games.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.microapp.core.components.AppProgressBar
import com.example.microapp.core.components.Btn
import com.example.microapp.core.components.Tag
import com.example.microapp.core.theme.C
import com.example.microapp.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(onNavigate: (Screen) -> Unit, viewModel: QuizViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Timer: counts down every second; auto-advances when 0
    LaunchedEffect(uiState.currentIndex, uiState.isDone, uiState.selectedAnswer) {
        if (uiState.isDone || uiState.selectedAnswer != null) return@LaunchedEffect
        for (time in 15 downTo 1) {
            delay(1000)
            if (uiState.isDone || uiState.selectedAnswer != null) break
            viewModel.decrementTimer()
        }
        if (uiState.selectedAnswer == null && !uiState.isDone) {
            viewModel.advanceQuestion()
        }
    }

    // Auto-advance after answer selection
    LaunchedEffect(uiState.selectedAnswer) {
        if (uiState.selectedAnswer == null) return@LaunchedEffect
        delay(800)
        viewModel.advanceQuestion()
    }

    // ── Result screen ──
    if (uiState.isDone) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🧠", fontSize = 60.sp, modifier = Modifier.padding(bottom = 12.dp))
            Text(
                "Quiz Complete!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = C.white,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                "${uiState.score}/5 sahi jawab",
                fontSize = 14.sp,
                color = C.grey,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            AppCard(
                glow = "gold",
                contentPadding = PaddingValues(horizontal = 40.dp, vertical = 24.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    "+${uiState.score * 6}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    color = C.gold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "⚡ Sparks Earned!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = C.gold,
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
    val q = uiState.questions[uiState.currentIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 24.dp)
    ) {
        // Top bar: Back, Tag, Timer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "← Back",
                color = C.greyL,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onNavigate(Screen.Games) }
            )
            Tag(text = "🧠 Quiz Master", color = C.blue)
            Tag(text = "⏱️ ${uiState.timeLeft}s", color = if (uiState.timeLeft < 6) C.red else C.greyL)
        }

        // Progress bar + counter
        Column(modifier = Modifier.padding(bottom = 20.dp)) {
            AppProgressBar(value = uiState.currentIndex, max = uiState.questions.size, color = C.blue)
            Text(
                "${uiState.currentIndex + 1} / ${uiState.questions.size}",
                fontSize = 11.sp,
                color = C.grey,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }

        // Question card
        AppCard(
            contentPadding = PaddingValues(22.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                q.question,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = C.white,
                lineHeight = 24.sp
            )
        }

        // Options
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            q.options.forEachIndexed { i, option ->
                val bg: Color
                val borderColor: Color
                val textColor: Color

                if (uiState.selectedAnswer != null) {
                    when {
                        i == q.correctIndex -> {
                            bg = C.green.copy(alpha = 0.13f)
                            borderColor = C.green.copy(alpha = 0.53f)
                            textColor = C.green
                        }
                        i == uiState.selectedAnswer && uiState.selectedAnswer != q.correctIndex -> {
                            bg = C.red.copy(alpha = 0.13f)
                            borderColor = C.red.copy(alpha = 0.53f)
                            textColor = C.red
                        }
                        else -> {
                            bg = C.cardL
                            borderColor = Color.White.copy(alpha = 0.08f)
                            textColor = C.white
                        }
                    }
                } else {
                    bg = C.cardL
                    borderColor = Color.White.copy(alpha = 0.08f)
                    textColor = C.white
                }

                val letter = ('A' + i)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(bg)
                        .border(1.5.dp, borderColor, RoundedCornerShape(14.dp))
                        .clickable {
                            if (uiState.selectedAnswer == null) {
                                viewModel.selectAnswer(i)
                            }
                        }
                        .padding(horizontal = 18.dp, vertical = 14.dp)
                ) {
                    Text(
                        "$letter. $option",
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

package com.example.microapp.feature.games.spin

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
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
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SpinWheelScreen(onNavigate: (Screen) -> Unit, viewModel: SpinWheelViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val animatedAngle = remember { Animatable(0f) }

    val segAngle = 360f / uiState.segments.size


    // Launch animation when targetAngle changes (spinning starts)
    LaunchedEffect(uiState.targetAngle) {
        if (uiState.isSpinning) {
            animatedAngle.animateTo(
                targetValue = uiState.targetAngle,
                animationSpec = tween(
                    durationMillis = 3000,
                    easing = CubicBezierEasing(0.17f, 0.67f, 0.35f, 1f)
                )
            )
            viewModel.onSpinComplete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top bar: Back, Tag, Free/Day
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
            Tag(text = "🎰 Spin Wheel", color = C.gold)
            Tag(text = "1 Free/Day", color = C.greyL)
        }

        // Pointer ▼
        Text(
            "▼",
            fontSize = 28.sp,
            color = C.gold,
            modifier = Modifier.padding(bottom = 0.dp)
        )

        // Wheel
        Box(
            modifier = Modifier
                .size(280.dp)
                .padding(bottom = 0.dp),
            contentAlignment = Alignment.Center
        ) {
            // Wheel canvas with rotation
            Canvas(
                modifier = Modifier
                    .size(260.dp)
                    .graphicsLayer(rotationZ = animatedAngle.value)
            ) {
                val centerX = size.width / 2f
                val centerY = size.height / 2f
                val radius = size.width / 2f

                uiState.segments.forEachIndexed { i, seg ->
                    val startDeg = i * segAngle - 90f
                    // Draw arc segment
                    drawArc(
                        color = seg.color,
                        startAngle = startDeg,
                        sweepAngle = segAngle,
                        useCenter = true,
                        topLeft = Offset.Zero,
                        size = Size(size.width, size.height)
                    )

                    // Draw segment text
                    val midAngleRad = Math.toRadians((startDeg + segAngle / 2f).toDouble())
                    val textRadius = radius * 0.67f
                    val tx = centerX + textRadius * cos(midAngleRad).toFloat()
                    val ty = centerY + textRadius * sin(midAngleRad).toFloat()

                    drawIntoCanvas { canvas ->
                        val paint = Paint().apply {
                            color = C.bg.toArgb()
                            textSize = 24f
                            textAlign = Paint.Align.CENTER
                            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                            isAntiAlias = true
                        }
                        canvas.nativeCanvas.save()
                        canvas.nativeCanvas.rotate(
                            i * segAngle + segAngle / 2f,
                            tx,
                            ty
                        )
                        canvas.nativeCanvas.drawText(
                            seg.sparks.toString(),
                            tx,
                            ty + 8f,
                            paint
                        )
                        canvas.nativeCanvas.restore()
                    }
                }

                // Center circle
                drawCircle(
                    color = C.bg,
                    radius = 24f * (size.width / 260f),
                    center = Offset(centerX, centerY)
                )
                drawCircle(
                    color = C.gold,
                    radius = 24f * (size.width / 260f),
                    center = Offset(centerX, centerY),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 3f * (size.width / 260f)
                    )
                )

                // Center ⚡ emoji
                drawIntoCanvas { canvas ->
                    val paint = Paint().apply {
                        textSize = 36f
                        textAlign = Paint.Align.CENTER
                        isAntiAlias = true
                    }
                    canvas.nativeCanvas.drawText(
                        "⚡",
                        centerX,
                        centerY + 12f,
                        paint
                    )
                }
            }

            // Outer glow ring
            Box(
                modifier = Modifier
                    .size(264.dp)
                    .border(3.dp, C.gold.copy(alpha = 0.4f), CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Result or Spin button
        if (uiState.result != null) {
            AppCard(
                glow = "gold",
                contentPadding = PaddingValues(horizontal = 40.dp, vertical = 20.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Text(
                    "+${uiState.result!!.sparks}",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    color = C.gold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    "⚡ Sparks Won!",
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
        } else {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)) {
                Btn(
                    text = if (uiState.isSpinning) "Spinning... 🌀" else "🎰 SPIN!",
                    onClick = { viewModel.spin() },
                    variant = "gold"
                )
            }
        }
    }
}

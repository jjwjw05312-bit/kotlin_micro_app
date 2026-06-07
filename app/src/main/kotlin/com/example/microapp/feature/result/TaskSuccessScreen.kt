package com.example.microapp.feature.result

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.microapp.core.components.Btn
import com.example.microapp.core.theme.C
import com.example.microapp.navigation.Screen

@Composable
fun TaskSuccessScreen(onNavigate: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(start = 24.dp, end = 24.dp, top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Big emoji
        Text("🎉", fontSize = 64.sp, modifier = Modifier.padding(bottom = 10.dp))

        // Title
        Text(
            "TASK COMPLETE!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            color = C.white,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Subtitle
        Text(
            "Bahut badhiya Rahul!",
            fontSize = 13.sp,
            color = C.grey,
            modifier = Modifier.padding(bottom = 28.dp)
        )

        // Gold reward card (gradient #1A2A00 to C.card)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(26.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF1A2A00), C.card)
                    )
                )
                .border(2.dp, C.gold.copy(alpha = 0.4f), RoundedCornerShape(26.dp))
                .padding(horizontal = 48.dp, vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "+400",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Black,
                    color = C.gold,
                    letterSpacing = (-2).sp
                )
                Text(
                    "⚡ Sparks Earned!",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = C.gold
                )
                Text(
                    "New Balance: ⚡ 2,100",
                    fontSize = 12.sp,
                    color = C.grey,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Bonus icons row: ⭐ +40 RP, 🔥 Day 7!, 🎰 +3 Tickets
        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            listOf(
                "⭐" to "+40 RP",
                "🔥" to "Day 7!",
                "🎰" to "+3 Tickets"
            ).forEach { (icon, label) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(icon, fontSize = 24.sp)
                    Text(
                        label,
                        fontSize = 11.sp,
                        color = C.greyL,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Green success banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(C.green.copy(alpha = 0.08f))
                .border(1.dp, C.green.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                .padding(horizontal = 18.dp, vertical = 10.dp)
        ) {
            Text(
                "✅ Aaj ka task ho gaya! Kal naye 5 tasks aayenge.",
                fontSize = 12.sp,
                color = C.green
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Games button (primary)
        Btn(
            text = "🎮 Bonus Games Khelo",
            onClick = { onNavigate(Screen.Games) },
            variant = "primary",
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Home button (ghost)
        Btn(
            text = "🏠 Home Pe Jaao",
            onClick = { onNavigate(Screen.Home) },
            variant = "ghost"
        )
    }
}

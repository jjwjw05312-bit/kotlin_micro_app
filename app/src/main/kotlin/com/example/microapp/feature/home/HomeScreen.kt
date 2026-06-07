package com.example.microapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.microapp.core.theme.C
import com.example.microapp.feature.home.components.BalanceCard
import com.example.microapp.feature.home.components.DailyTaskBanner
import com.example.microapp.feature.home.components.QuickActionGrid
import com.example.microapp.feature.home.components.RecentActivityList
import com.example.microapp.navigation.Screen

@Composable
fun HomeScreen(onNavigate: (Screen) -> Unit, viewModel: HomeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(C.purple, C.purpleD))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "RA", color = C.white, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                Column {
                    Text(text = "Welcome back,", color = C.greyL, fontSize = 12.sp)
                    Text(text = "${uiState.userName} ⚡", color = C.white, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
            // Bell Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(C.card)
                    .border(1.dp, C.purple.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔔", fontSize = 18.sp)
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                        .size(8.dp)
                        .background(C.red, CircleShape)
                )
            }
        }

        // --- Hero Balance Card ---
        BalanceCard(
            balance = uiState.balance,
            level = uiState.level,
            levelName = uiState.levelName,
            maxBalance = uiState.maxBalance
        )

        // --- Daily Task Banner ---
        DailyTaskBanner(onClick = { onNavigate(Screen.Tasks) })

        // --- Quick Actions Grid ---
        QuickActionGrid(onNavigate = onNavigate)

        // --- Recent Activity ---
        RecentActivityList(activities = uiState.recentActivity)
    }
}

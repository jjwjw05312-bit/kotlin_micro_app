package com.example.microapp.feature.profile

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.microapp.core.theme.C
import com.example.microapp.feature.profile.components.AchievementGrid
import com.example.microapp.feature.profile.components.ProfileHeader
import com.example.microapp.feature.profile.components.StatsGrid

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "👤 My Vault",
            fontSize = 20.sp,
            fontWeight = FontWeight(900),
            color = C.white,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        
        ProfileHeader(
            userName = uiState.userName,
            userId = uiState.userId,
            memberSince = uiState.memberSince,
            rank = uiState.rank
        )
        
        StatsGrid(stats = uiState.stats)
        
        AchievementGrid(badges = uiState.badges)
        
        // Bottom Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.buttons.forEach { btn ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(C.cardL)
                        .border(1.dp, Color.White.copy(alpha = 0.06f), RoundedCornerShape(14.dp))
                        .clickable { /* Handle click */ }
                        .padding(vertical = 14.dp, horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = btn.icon,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = btn.text,
                        color = C.white,
                        fontWeight = FontWeight(600),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "→",
                        color = C.grey,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

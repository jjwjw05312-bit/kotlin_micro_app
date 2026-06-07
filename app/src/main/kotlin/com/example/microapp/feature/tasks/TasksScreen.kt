package com.example.microapp.feature.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.microapp.core.theme.C
import com.example.microapp.feature.tasks.components.TaskItemCard
import com.example.microapp.navigation.Screen

@Composable
fun TasksScreen(onNavigate: (Screen) -> Unit, viewModel: TasksViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(text = "Aajka Task", color = C.white, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        // Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(C.purple.copy(alpha = 0.15f))
                .border(1.dp, C.purple.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Koi bhi 1 task complete karo aur extra 100 ⚡ bonus paao!",
                color = C.purpleL,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        // Task List
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            uiState.tasks.forEach { t ->
                TaskItemCard(
                    task = t,
                    isSelected = uiState.selectedTaskId == t.id,
                    onSelect = { viewModel.selectTask(t.id) },
                    onStartTask = { onNavigate(Screen.TaskSuccess) }
                )
            }
        }
    }
}

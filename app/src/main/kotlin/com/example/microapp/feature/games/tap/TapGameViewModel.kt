package com.example.microapp.feature.games.tap

import androidx.lifecycle.ViewModel
import com.example.microapp.data.model.Spark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.min
import kotlin.random.Random

class TapGameViewModel : ViewModel() {

    data class UiState(
        val score: Int = 0,
        val timeLeft: Int = 30,
        val isStarted: Boolean = false,
        val isDone: Boolean = false,
        val reward: Int = 0,
        val sparks: List<Spark> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var idCounter = 0

    fun startGame() {
        _uiState.update { it.copy(isStarted = true) }
    }

    fun tapSpark(id: Int) {
        _uiState.update { state ->
            val updatedSparks = state.sparks.filterNot { it.id == id }
            val newScore = state.score + 1
            state.copy(
                score = newScore,
                reward = min(newScore * 2, 20),
                sparks = updatedSparks
            )
        }
    }

    fun spawnSpark() {
        idCounter++
        val newSpark = Spark(
            id = idCounter,
            x = Random.nextFloat() * 0.75f,
            y = Random.nextFloat() * 0.70f,
            size = 28f + Random.nextFloat() * 20f
        )
        _uiState.update { state ->
            val list = state.sparks.toMutableList()
            if (list.size >= 10) {
                list.removeAt(0)
            }
            list.add(newSpark)
            state.copy(sparks = list)
        }
    }

    fun decrementTimer() {
        _uiState.update { state ->
            val newTime = state.timeLeft - 1
            if (newTime <= 0) {
                state.copy(timeLeft = 0, isDone = true, reward = min(state.score * 2, 20))
            } else {
                state.copy(timeLeft = newTime)
            }
        }
    }
}

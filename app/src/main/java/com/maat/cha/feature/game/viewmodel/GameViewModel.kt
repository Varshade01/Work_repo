package com.maat.cha.feature.game.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maat.cha.core.navigation.destinations.InfoType
import com.maat.cha.core.navigation.destinations.ReferenceInfoSource
import com.maat.cha.feature.game.dialogs.model.GameDialogType
import com.maat.cha.feature.game.events.GameEvents
import com.maat.cha.feature.game.navigation.GameNavigationActions
import com.maat.cha.feature.game.state.GameState
import com.maat.cha.feature.game.utils.GameUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val navigationActions: GameNavigationActions,
    private val dataStorePreferences: com.maat.cha.core.datastore.utils.DataStorePreferences
) : ViewModel() {

    private val _state = MutableStateFlow(
        GameState(
            gameField = GameUtils.generateGameField(1) // Initialize with random field
        )
    )
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {
        // Load saved coins from DataStore
        viewModelScope.launch {
            dataStorePreferences.totalCoins.collect { savedCoins ->
                _state.update { it.copy(totalCoins = savedCoins) }
            }
        }
    }

    fun onEvent(event: GameEvents) {
        when (event) {
            is GameEvents.OnStartClick -> {
                if (!state.value.isGameStarted) {
                    startGame()
                }
            }

            is GameEvents.OnNextRoundClick -> {
                nextRound()
            }

            is GameEvents.OnAgainClick -> {
                resetGame()
            }

            is GameEvents.OnInfoClick -> {
                viewModelScope.launch {
                    navigationActions.navigateToReferenceInfo(
                        InfoType.HOW_TO_PLAY,
                        ReferenceInfoSource.GAME
                    )
                }
            }

            is GameEvents.OnRoundSelect -> {
                // Round selection is disabled during game
                if (!state.value.isGameStarted) {
                    _state.update {
                        it.copy(
                            currentRound = event.roundNumber,
                            gameField = GameUtils.generateGameField(event.roundNumber) // Generate new field for selected round
                        )
                    }
                }
            }
        }
    }

    private fun startGame() {
        viewModelScope.launch {
            _state.update { it.copy(isGameStarted = true) }
            spinGame()
        }
    }

    private suspend fun spinGame() {
        _state.update { it.copy(isSpinning = true) }

        // Phase 1: Very fast spinning (1.5 seconds) - ultra smooth transitions
        repeat(6) { spinIndex ->
            val newField = GameUtils.generateGameField(state.value.currentRound)
            _state.update { it.copy(gameField = newField) }
            delay(250L) // 3x slower for ultra smooth transitions
        }

        // Phase 2: Fast spinning (1.5 seconds) - ultra smooth transitions
        repeat(5) { spinIndex ->
            val newField = GameUtils.generateGameField(state.value.currentRound)
            _state.update { it.copy(gameField = newField) }
            delay(300L) // 3x slower for gradual slowdown
        }

        // Phase 3: Medium speed (1.5 seconds) - ultra smooth transitions
        repeat(4) { spinIndex ->
            val newField = GameUtils.generateGameField(state.value.currentRound)
            _state.update { it.copy(gameField = newField) }
            delay(375L) // 3x slower for smooth medium speed
        }

        // Phase 4: Slow down (1 second) - ultra smooth transitions
        repeat(3) { spinIndex ->
            val newField = GameUtils.generateGameField(state.value.currentRound)
            _state.update { it.copy(gameField = newField) }
            delay(333L) // 3x slower for smoother feel
        }

        // Phase 5: Very slow (0.5 seconds) - ultra smooth transitions
        repeat(2) { spinIndex ->
            val newField = GameUtils.generateGameField(state.value.currentRound)
            _state.update { it.copy(gameField = newField) }
            delay(250L) // 3x slower for smooth finish
        }

        // Final result with guaranteed match for better user experience
        val finalField = generateFieldWithMatch(state.value.currentRound)
        val matchedItems = GameUtils.calculateMatchedItems(finalField)
        val roundCoins = GameUtils.calculateCoins(matchedItems, state.value.currentRound)

        _state.update {
            it.copy(
                gameField = finalField,
                matchedItems = matchedItems,
                roundCoins = roundCoins,
                isSpinning = false
            )
        }

        // Wait 2 seconds to show the collected fruits before dialog
        delay(2000L)

        // Show round result dialog
        showRoundResultDialog()
    }

    private fun generateFieldWithMatch(round: Int): List<List<Int>> {
        val size = GameUtils.getFieldSizeForRound(round)
        val field = List(size) { row ->
            List(size) { col ->
                GameUtils.FRUIT_ICONS.random()
            }
        }

        // Ensure there's at least one match for better user experience
        val matchedItems = GameUtils.calculateMatchedItems(field)
        if (matchedItems.isNotEmpty()) {
            return field
        }

        // If no match, create one by modifying a row
        val modifiedField = field.toMutableList()
        val randomFruit = GameUtils.FRUIT_ICONS.random()
        val randomRow = (0 until size).random()

        // Create a match of 3 in a row
        val startCol = (0..(size - 3)).random()
        for (col in startCol until startCol + 3) {
            modifiedField[randomRow] = modifiedField[randomRow].toMutableList().apply {
                this[col] = randomFruit
            }
        }

        return modifiedField
    }

    private fun showRoundResultDialog() {
        val currentState = state.value

        // Update collected fruits for current round
        val collectedFruits =
            getCollectedFruitsForRound(currentState.gameField, currentState.matchedItems)
        val updatedCollectedFruits = currentState.collectedFruitsPerRound.toMutableMap()
        updatedCollectedFruits[currentState.currentRound] = collectedFruits

        if (currentState.currentRound < currentState.totalRounds) {
            // Show round finished dialog - don't add coins yet
            val dialog = GameDialogType.RoundFinished(
                roundNumber = currentState.currentRound,
                collectedFruits = collectedFruits
            )
            _state.update {
                it.copy(
                    currentDialog = dialog,
                    isDialogVisible = true,
                    collectedFruitsPerRound = updatedCollectedFruits
                )
            }
        } else {
            // Show "Collect all rules" dialog - don't add coins yet
            val collectRulesDialog = GameDialogType.CollectRuiles(
                columns = (1..4).map { round ->
                    val collectedFruits = updatedCollectedFruits[round] ?: emptyList()
                    collectedFruits
                }
            )
            _state.update {
                it.copy(
                    currentDialog = collectRulesDialog,
                    isDialogVisible = true,
                    collectedFruitsPerRound = updatedCollectedFruits
                )
            }
        }
    }

    private fun getCollectedFruitsForRound(
        gameField: List<List<Int>>,
        matchedItems: List<List<Int>>
    ): List<Int> {
        if (matchedItems.isEmpty()) return emptyList()

        val collectedFruits = mutableListOf<Int>()
        val matchedPositions = matchedItems.flatten().toSet()

        // Group matched items by fruit type
        val fruitGroups = mutableMapOf<Int, Int>()

        for (row in gameField.indices) {
            for (col in gameField[row].indices) {
                val position = row * gameField[row].size + col
                if (matchedPositions.contains(position)) {
                    val fruitIcon = gameField[row][col]
                    fruitGroups[fruitIcon] = fruitGroups.getOrDefault(fruitIcon, 0) + 1
                }
            }
        }

        // Add fruits that have 3 or more items (show 3 of the same fruit)
        fruitGroups.forEach { (fruitIcon, count) ->
            if (count >= 3) {
                // Add the fruit 3 times to show 3 icons
                repeat(3) {
                    collectedFruits.add(fruitIcon)
                }
            }
        }

        return collectedFruits
    }

    private fun nextRound() {
        val currentState = state.value
        val nextRound = currentState.currentRound + 1

        _state.update {
            it.copy(
                currentRound = nextRound,
                totalCoins = it.totalCoins, // Don't add round coins yet
                roundCoins = 0,
                currentDialog = null,
                isDialogVisible = false,
                matchedItems = emptyList(),
                gameField = GameUtils.generateGameField(nextRound) // Generate new field for next round
            )
        }

        // Start next round spinning
        viewModelScope.launch {
            spinGame()
        }
    }

    private fun resetGame() {
        val currentState = state.value

        // Save total coins to DataStore
        viewModelScope.launch {
            dataStorePreferences.setTotalCoins(currentState.totalCoins)
        }

        _state.update {
            GameState(
                currentRound = 1,
                totalRounds = 4,
                isSpinning = false,
                isGameStarted = false,
                currentDialog = null,
                totalCoins = currentState.totalCoins, // Preserve collected coins
                roundCoins = 0,
                gameField = GameUtils.generateGameField(1), // Initialize with random field
                matchedItems = emptyList(),
                isDialogVisible = false,
                collectedFruitsPerRound = emptyMap()
            )
        }
    }

    fun onBackClick() {
        viewModelScope.launch {
            navigationActions.navigateToMain()
        }
    }

    fun onCollectRulesClick() {
        val currentState = state.value
        val allCollectedFruits = currentState.collectedFruitsPerRound.values.flatten()
        val totalCollectedFruits = allCollectedFruits.size / 3 // Count unique fruit types
        val newCoins = totalCollectedFruits * 100 // 100 coins per fruit type
        val updatedTotalCoins = currentState.totalCoins + newCoins // Add to existing coins

        // Show total win dialog
        val dialog = GameDialogType.TotalWin(updatedTotalCoins)
        _state.update {
            it.copy(
                currentDialog = dialog,
                isDialogVisible = true,
                totalCoins = updatedTotalCoins
            )
        }
    }
} 
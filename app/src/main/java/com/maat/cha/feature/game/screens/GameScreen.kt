package com.maat.cha.feature.game.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maat.cha.R
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.CircularIconButton
import com.maat.cha.feature.composable.MainButton
import com.maat.cha.feature.composable.Title
import com.maat.cha.feature.game.components.AnimatedGameField
import com.maat.cha.feature.game.components.Match3GameField
import com.maat.cha.feature.game.components.CollectionItemsSection
import com.maat.cha.feature.game.dialogs.GameDialog
import com.maat.cha.feature.game.events.GameEvents
import com.maat.cha.feature.game.viewmodel.GameViewModel
import com.maat.cha.ui.theme.Orange_light

@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    GameScreenUI(
        state = state,
        onBackClick = { viewModel.onBackClick() },
        onInfoClick = { viewModel.onEvent(GameEvents.OnInfoClick) },
        onStartClick = { viewModel.onEvent(GameEvents.OnStartClick) },
        onNextRoundClick = { viewModel.onEvent(GameEvents.OnNextRoundClick) },
        onAgainClick = { viewModel.onEvent(GameEvents.OnAgainClick) },
        onCollectRulesClick = { viewModel.onCollectRulesClick() },
        onRoundSelect = { round -> viewModel.onEvent(GameEvents.OnRoundSelect(round)) },
        onFruitSelected = { fruit -> viewModel.onEvent(GameEvents.OnFruitSelected(fruit)) },
        onFruitMoved = { fruit, toRow, toCol -> viewModel.onEvent(GameEvents.OnFruitMoved(fruit, toRow, toCol)) },
        onFruitDeselected = { viewModel.onEvent(GameEvents.OnFruitDeselected) },
        onMoveCompleted = { viewModel.onEvent(GameEvents.OnMoveCompleted) }
    )
}

@Composable
fun GameScreenUI(
    state: com.maat.cha.feature.game.state.GameState,
    onBackClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    onStartClick: () -> Unit = {},
    onNextRoundClick: () -> Unit = {},
    onAgainClick: () -> Unit = {},
    onCollectRulesClick: () -> Unit = {},
    onRoundSelect: (Int) -> Unit = {},
    onFruitSelected: (com.maat.cha.feature.game.model.FruitItem) -> Unit = {},
    onFruitMoved: (com.maat.cha.feature.game.model.FruitItem, Int, Int) -> Unit = { _, _, _ -> },
    onFruitDeselected: () -> Unit = {},
    onMoveCompleted: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundApp(
            backgroundRes = R.drawable.background_app,
            modifier = Modifier.matchParentSize()
        )

        // Blur background when dialog is visible
        if (state.isDialogVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 8.dp)
            )
        }

        CircularIconButton(
            onClick = onBackClick,
            iconRes = R.drawable.ic_btn_previous,
            contentDescription = "btn previous",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            backgroundRes = null,
            iconSize = 56,
            sizeItem = 56
        )
        CircularIconButton(
            onClick = onInfoClick,
            iconRes = R.drawable.ic_btn_info,
            contentDescription = "btn info",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp),
            backgroundRes = null,
            iconSize = 56,
            sizeItem = 56
        )

        Title(
            text = state.totalCoins.toString(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            textColor = Color.White,
            borderColor = Color.White,
            icon = R.drawable.ic_coin,
            widthItem = 180.dp
        )

        // Game content
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Rounds indicator
            GameRoundsIndicator(
                currentRound = state.currentRound,
                totalRounds = state.totalRounds,
                isGameStarted = state.isGameStarted,
                onRoundSelect = onRoundSelect
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Game field - use Match 3 field if enabled, otherwise use animated field
            if (state.isMatch3Mode && state.gameBoard != null) {
                Match3GameField(
                    gameBoard = state.gameBoard,
                    isProcessingMove = state.isProcessingMove,
                    onFruitSelected = onFruitSelected,
                    onFruitMoved = onFruitMoved,
                    onFruitDeselected = onFruitDeselected,
                    onMoveCompleted = onMoveCompleted,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                AnimatedGameField(
                    gameField = state.gameField,
                    isSpinning = state.isSpinning,
                    currentRound = state.currentRound,
                    matchedItems = state.matchedItems,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Collection items section
            CollectionItemsSection(
                modifier = Modifier.padding(bottom = 32.dp),
                totalRounds = state.totalRounds,
                collectedFruitsPerRound = state.collectedFruitsPerRound,
                selectedColor = Orange_light,
                unselectedColor = Color.Transparent,
                borderColor = Color.White,
                cornerRadiusSelection = 16.dp,
                boxWidth = 92.dp,
                boxHeight = 132.dp,
                backgroundColor = Color(0xC6FF6AC3),
                iconSize = 32.dp,
                cornerRadiusFruits = 12.dp
            )
        }

        // Start button - hide in Match 3 mode when game is started
        if (!state.isMatch3Mode || !state.isGameStarted) {
            MainButton(
                onClick = onStartClick,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                btnText = if (state.isGameStarted) "SPINNING..." else "Start",
                textColor = Color.White,
                backgroundRes = R.drawable.background_btn,
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.BottomCenter),
                contentDescription = "Start button",
                buttonWidth = 320.dp,
                enabled = !state.isGameStarted
            )
        }

        // Game dialog
        if (state.isDialogVisible && state.currentDialog != null) {
            GameDialog(
                type = state.currentDialog,
                onMainClick = {
                    when (state.currentDialog) {
                        is com.maat.cha.feature.game.dialogs.model.GameDialogType.RoundFinished -> onNextRoundClick()
                        is com.maat.cha.feature.game.dialogs.model.GameDialogType.TotalWin -> onAgainClick()
                        is com.maat.cha.feature.game.dialogs.model.GameDialogType.CollectRuiles -> onCollectRulesClick()
                    }
                },
                onInfoClick = onInfoClick
            )
        }
    }
}

@Composable
fun GameRoundsIndicator(
    currentRound: Int,
    totalRounds: Int,
    isGameStarted: Boolean,
    onRoundSelect: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.rounds_title),
            fontSize = 18.sp,
            color = Orange_light,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..totalRounds) {
                Box(
                    modifier = Modifier
                        .size(width = 76.dp, height = 32.dp)
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(18.dp)
                        )
                        .background(
                            color = if (i == currentRound) Color(0xFFD157B5) else Color(0xFF330227),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .clickable(enabled = !isGameStarted) { onRoundSelect(i) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = i.toString(),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                if (i < totalRounds) {
                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .width(24.dp)
                            .background(Color.White)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScreenUI() {
    GameScreenUI(
        state = com.maat.cha.feature.game.state.GameState()
    )
}

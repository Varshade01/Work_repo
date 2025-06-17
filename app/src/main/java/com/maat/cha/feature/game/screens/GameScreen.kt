package com.maat.cha.feature.game.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maat.cha.R
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.CircularIconButton
import com.maat.cha.feature.composable.MainButton
import com.maat.cha.feature.composable.Title
import com.maat.cha.ui.theme.Orange_light

@Composable
fun GameScreen() {
    GameScreenUI()
}

@Composable
fun GameScreenUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundApp(
            backgroundRes = R.drawable.background_app,
            modifier = Modifier.matchParentSize()
        )

        CircularIconButton(
            onClick = { /* TODO */ },
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
            onClick = { /* TODO */ },
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
            text = stringResource(R.string.game_points),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            textColor = Color.White,
            borderColor = Color.White,
            icon = R.drawable.ic_coin,
            widthItem = 180.dp
        )
        CollectRuiles(modifier = Modifier.align(Alignment.Center))

        MainButton(
            onClick = { /* TODO */ },
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            btnText = "Start",
            textColor = Color.White,
            backgroundRes = R.drawable.background_btn,
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.BottomCenter),
            contentDescription = "Main screen btn",
            buttonWidth = 320.dp
        )
    }
}

@Composable
fun GameSurface(modifier: Modifier = Modifier) {
    var currentRound by rememberSaveable { mutableIntStateOf(1) }
    val totalRounds = 4

    Column(
        modifier = modifier,
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
                        .clickable { currentRound = i }
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
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .size(width = 340.dp, height = 340.dp),
            contentAlignment = Alignment.Center
        ) {
            BackgroundApp(
                backgroundRes = R.drawable.background_surface_game,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@Composable
fun CollectRuiles(modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameSurface(modifier = Modifier)

        Spacer(modifier = Modifier.height(12.dp))

        var selectedIcon by rememberSaveable { mutableIntStateOf(1) }
        SelectionAndFruitsSection(
            totalItems = 3,
            currentItem = selectedIcon,
            onItemClick = { selectedIcon = it },
            selectedColor = Orange_light,
            unselectedColor = Color.Transparent,
            borderColor = Color.White,
            cornerRadiusSelection = 16.dp,
            fruitIconResList = listOf(
                R.drawable.ic_raspberry,
                R.drawable.ic_star,
                R.drawable.ic_plum
            ),
            boxWidth = 92.dp,
            boxHeight = 132.dp,
            backgroundColor = Color(0xC6FF6AC3),
            iconSize = 32.dp,
            cornerRadiusFruits = 12.dp
        )
    }
}

@Composable
fun SelectionAndFruitsSection(
    modifier: Modifier = Modifier,
    totalItems: Int,
    currentItem: Int,
    onItemClick: (Int) -> Unit,
    selectedColor: Color,
    unselectedColor: Color,
    borderColor: Color,
    cornerRadiusSelection: Dp,
    fruitIconResList: List<Int>,
    boxWidth: Dp,
    boxHeight: Dp,
    backgroundColor: Color,
    iconSize: Dp,
    cornerRadiusFruits: Dp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Selection row (transparent background, no connectors)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..totalItems) {
                Box(
                    modifier = Modifier
                        .size(width = 76.dp, height = 32.dp)
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(cornerRadiusSelection)
                        )
                        .background(
                            color = if (i == currentItem) selectedColor else unselectedColor,
                            shape = RoundedCornerShape(cornerRadiusSelection)
                        )
                        .clickable { onItemClick(i) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = i.toString(),
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            fruitIconResList.forEach { resId ->
                Box(
                    modifier = Modifier
                        .size(width = boxWidth, height = boxHeight)
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(cornerRadiusFruits)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(3) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = null,
                                modifier = Modifier.size(iconSize),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGameScreenUI() {
    GameScreenUI()
}

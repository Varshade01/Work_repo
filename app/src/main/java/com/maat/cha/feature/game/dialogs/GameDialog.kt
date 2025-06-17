package com.maat.cha.feature.game.dialogs

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.maat.cha.R
import com.maat.cha.feature.composable.CircularIconButton
import com.maat.cha.feature.composable.MainButton
import com.maat.cha.feature.game.dialogs.model.GameDialogType

@Composable
fun GameDialog(
    type: GameDialogType,
    onMainClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { /* TODO */ }) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .wrapContentHeight()
                .border(
                    width = 1.dp,
                    color = Color(0xFFFDB001),
                    shape = RoundedCornerShape(16.dp)
                ),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6A044F))
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (type) {
                    is GameDialogType.RoundFinished -> {
                        Text(
                            text = "Round ${type.roundNumber}\nfinished",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_star_shine),
                            contentDescription = null,
                            modifier = Modifier.size(164.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        MainButton(
                            onClick = onMainClick,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            btnText = "NEXT ROUND",
                            textColor = Color.White,
                            backgroundRes = R.drawable.background_btn,
                            modifier = Modifier,
                            contentDescription = null,
                            buttonWidth = 260.dp,
                            buttonHeight = 48.dp
                        )
                    }

                    is GameDialogType.TotalWin -> {
                        Text(
                            text = "Total win",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = type.points.toString(),
                            fontSize = 48.sp,
                            color = Color(0xFFFDB001),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 6f
                                )
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "coins",
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    offset = Offset(1f, 1f),
                                    blurRadius = 3f
                                )
                            )
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        MainButton(
                            onClick = onMainClick,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            btnText = "Again",
                            textColor = Color.White,
                            backgroundRes = R.drawable.background_btn,
                            modifier = Modifier,
                            contentDescription = null,
                            buttonWidth = 260.dp,
                            buttonHeight = 48.dp
                        )
                    }

                    is GameDialogType.CollectRuiles -> {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularIconButton(
                                onClick = { /* TODO: info click */ },
                                iconRes = R.drawable.ic_btn_info,
                                contentDescription = "Info",
                                modifier = Modifier.size(24.dp),
                                backgroundRes = null,
                                iconSize = 24,
                                sizeItem = 24
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Collect ruiles:",
                                fontSize = 24.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        val totalItems = type.columns.size.coerceAtLeast(1)
                        var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color(0xFF430034),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    for (i in 1..totalItems) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(32.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = Color.White,
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                                .background(
                                                    color = if (i == selectedIndex) Color(0xFFFDB001) else Color.Transparent,
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                                .clickable { selectedIndex = i },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "0",
                                                fontSize = 14.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    for (columnIcons in type.columns) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(
                                                    color = Color(0xC6FF6AC3),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(8.dp),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                for (resId in columnIcons) {
                                                    Image(
                                                        painter = painterResource(id = resId),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        MainButton(
                            onClick = onMainClick,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            btnText = "GOT IT!",
                            textColor = Color.White,
                            backgroundRes = R.drawable.background_btn,
                            modifier = Modifier,
                            contentDescription = null,
                            buttonWidth = 260.dp,
                            buttonHeight = 48.dp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_GameDialog_RoundFinished() {
    GameDialog(
        type = GameDialogType.RoundFinished(roundNumber = 1),
        onMainClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun Preview_GameDialog_TotalWin() {
    GameDialog(
        type = GameDialogType.TotalWin(points = 5635),
        onMainClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun Preview_GameDialog_CollectRules() {
    GameDialog(
        type = GameDialogType.CollectRuiles(
            columns = listOf(
                listOf(R.drawable.ic_raspberry, R.drawable.ic_raspberry, R.drawable.ic_raspberry),
                listOf(R.drawable.ic_star, R.drawable.ic_star, R.drawable.ic_star),
                listOf(R.drawable.ic_plum, R.drawable.ic_plum, R.drawable.ic_plum),
                listOf(R.drawable.ic_lemon, R.drawable.ic_lemon, R.drawable.ic_lemon)
            )
        ),
        onMainClick = {}
    )
}
package com.maat.cha.feature.settings.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maat.cha.R
import com.maat.cha.feature.composable.BackgroundApp
import com.maat.cha.feature.composable.CircularIconButton
import com.maat.cha.feature.composable.CustomSwitch
import com.maat.cha.feature.composable.Title

@Composable
fun SettingsScreen() {
    SettingsScreenUI()
}

@Composable
fun SettingsScreenUI() {

    var musicSwitchChecked by remember { mutableStateOf(true) }
    var vfxSwitchChecked by remember { mutableStateOf(true) }

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
                .padding(horizontal = 16.dp, vertical = 16.dp),
            btnText = "",
            textColor = Color.Yellow,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            contentScale = ContentScale.Crop,
            backgroundRes = null,
            iconSize = 56,
            sizeItem = 56,
        )
        Title(
            text = stringResource(R.string.settings_title),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(start = 36.dp, top = 16.dp),
            textColor = Color.White,
            borderColor = Color.White,
            icon = null,
            widthItem = 240.dp
        )
        Column(modifier = Modifier.align(Alignment.Center)) {
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = stringResource(R.string.music_switch_title),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            CustomSwitch(
                checked = musicSwitchChecked,
                onCheckedChange = { musicSwitchChecked = it }
            )

            Text(
                modifier = Modifier.padding(bottom = 24.dp, top = 48.dp),
                text = stringResource(R.string.vfx_switch_title),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            CustomSwitch(
                checked = vfxSwitchChecked,
                onCheckedChange = { vfxSwitchChecked = it }
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(32.dp),
            text = stringResource(R.string.privacy_policy),
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
        )

        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp),
            text = stringResource(R.string.title_terms_of_use),
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
        )
    }
}

@Composable
@Preview
fun SettingsScreenPreview() {
    SettingsScreen()
}
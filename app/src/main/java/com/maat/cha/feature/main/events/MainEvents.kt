package com.maat.cha.feature.main.events

sealed class MainEvents {
    object OnBackClick : MainEvents()
    object OnSettingsClick : MainEvents()
    object OnPlayClick : MainEvents()
    object OnHowToPlayClick : MainEvents()
} 
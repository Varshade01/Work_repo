package com.maat.cha.feature.settings.events

sealed class SettingsEvents {
    data class OnMusicToggle(val enabled: Boolean) : SettingsEvents()
    data class OnVfxToggle(val enabled: Boolean) : SettingsEvents()
    object OnBackClick : SettingsEvents()
    object OnPrivacyPolicyClick : SettingsEvents()
    object OnTermsOfUseClick : SettingsEvents()
} 
package com.maat.cha.core.datastore.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val HAS_READ_ALL_INFO = booleanPreferencesKey("has_read_all_info")
    val PRIVACY_ACCEPTED = booleanPreferencesKey("privacy_accepted")
    val PRIVACY_READ = booleanPreferencesKey("privacy_read")
    val HOW_TO_PLAY_READ = booleanPreferencesKey("how_to_play_read")
    val TERMS_OF_USE_READ = booleanPreferencesKey("terms_of_use_read")
    val USER_NAME = stringPreferencesKey("user_name")
    val TOTAL_COINS = intPreferencesKey("total_coins")
    val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
    val VFX_ENABLED = booleanPreferencesKey("vfx_enabled")
} 
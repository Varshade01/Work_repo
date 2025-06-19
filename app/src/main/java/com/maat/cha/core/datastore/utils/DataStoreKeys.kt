package com.maat.cha.core.datastore.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object DataStoreKeys {
    val HAS_READ_ALL_INFO = booleanPreferencesKey("has_read_all_info")
    val USER_NAME = stringPreferencesKey("user_name")
    val TOTAL_COINS = intPreferencesKey("total_coins")
    val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
    val VFX_ENABLED = booleanPreferencesKey("vfx_enabled")
} 
package com.maat.cha.core.datastore.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class DataStorePreferences(
    private val context: Context
) {

    val hasReadAllInfo: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.HAS_READ_ALL_INFO] ?: false
    }

    suspend fun setHasReadAllInfo(hasRead: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.HAS_READ_ALL_INFO] = hasRead
        }
    }

    suspend fun resetHasReadAllInfo() {
        context.dataStore.edit { preferences ->
            preferences.remove(DataStoreKeys.HAS_READ_ALL_INFO)
        }
    }

    val privacyAccepted: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.PRIVACY_ACCEPTED] ?: false
    }

    suspend fun setPrivacyAccepted(accepted: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.PRIVACY_ACCEPTED] = accepted
        }
    }

    suspend fun resetPrivacyAccepted() {
        context.dataStore.edit { preferences ->
            preferences.remove(DataStoreKeys.PRIVACY_ACCEPTED)
        }
    }

    val privacyRead: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.PRIVACY_READ] ?: false
    }

    suspend fun setPrivacyRead(read: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.PRIVACY_READ] = read
        }
    }

    val howToPlayRead: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.HOW_TO_PLAY_READ] ?: false
    }

    suspend fun setHowToPlayRead(read: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.HOW_TO_PLAY_READ] = read
        }
    }

    val termsOfUseRead: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.TERMS_OF_USE_READ] ?: false
    }

    suspend fun setTermsOfUseRead(read: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.TERMS_OF_USE_READ] = read
        }
    }

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.USER_NAME] ?: ""
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.USER_NAME] = name
        }
    }

    val totalCoins: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.TOTAL_COINS] ?: 0
    }

    suspend fun setTotalCoins(coins: Int) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.TOTAL_COINS] = coins
        }
    }

    suspend fun addCoins(coins: Int) {
        context.dataStore.edit { preferences ->
            val currentCoins = preferences[DataStoreKeys.TOTAL_COINS] ?: 0
            preferences[DataStoreKeys.TOTAL_COINS] = currentCoins + coins
        }
    }

    val musicEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.MUSIC_ENABLED] ?: true
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.MUSIC_ENABLED] = enabled
        }
    }

    val vfxEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.VFX_ENABLED] ?: true
    }

    suspend fun setVfxEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.VFX_ENABLED] = enabled
        }
    }
} 
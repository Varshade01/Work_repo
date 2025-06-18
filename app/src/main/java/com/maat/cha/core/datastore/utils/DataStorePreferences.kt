package com.maat.cha.core.datastore.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class DataStorePreferences @Inject constructor(
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

    val userName: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DataStoreKeys.USER_NAME] ?: ""
    }

    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.USER_NAME] = name
        }
    }
} 
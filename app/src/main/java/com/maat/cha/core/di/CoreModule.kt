package com.maat.cha.core.di

import android.content.Context
import com.maat.cha.core.audio.AudioManager
import com.maat.cha.core.datastore.utils.DataStorePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    
    @Provides
    @Singleton
    fun provideAudioManager(
        @ApplicationContext context: Context,
        dataStorePreferences: DataStorePreferences
    ): AudioManager {
        return AudioManager(context, dataStorePreferences)
    }
} 
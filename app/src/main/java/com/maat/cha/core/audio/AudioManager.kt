package com.maat.cha.core.audio

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Build
import android.util.Log
import com.maat.cha.R
import com.maat.cha.core.datastore.utils.DataStorePreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dataStorePreferences: DataStorePreferences
) {
    private var backgroundMusicPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var coinSoundId: Int = 0
    
    private var isMusicEnabled: Boolean = true
    private var isVfxEnabled: Boolean = true
    private var isInitialized: Boolean = false
    
    private val audioScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    companion object {
        private const val TAG = "AudioManager"
        private const val MUSIC_VOLUME = 0.6f
        private const val SOUND_VOLUME = 0.8f
    }
    
    init {
        initializeAudio()
        loadSettings()
    }
    
    private fun initializeAudio() {
        try {
            // Initialize SoundPool for sound effects
            soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SoundPool.Builder()
                    .setMaxStreams(4)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                SoundPool(4, android.media.AudioManager.STREAM_MUSIC, 0)
            }
            
            // Load coin sound
            coinSoundId = soundPool?.load(context, R.raw.coins, 1) ?: 0
            
            isInitialized = true
            Log.d(TAG, "AudioManager initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize AudioManager", e)
        }
    }
    
    private fun loadSettings() {
        audioScope.launch {
            try {
                isMusicEnabled = dataStorePreferences.musicEnabled.first()
                isVfxEnabled = dataStorePreferences.vfxEnabled.first()
                
                Log.d(TAG, "Settings loaded - Music: $isMusicEnabled, VFX: $isVfxEnabled")
                
                if (isMusicEnabled && isInitialized) {
                    startBackgroundMusic()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to load settings", e)
            }
        }
    }
    
    suspend fun updateMusicSetting(enabled: Boolean) {
        isMusicEnabled = enabled
        if (enabled && isInitialized) {
            startBackgroundMusic()
        } else {
            stopBackgroundMusic()
        }
        Log.d(TAG, "Music setting updated: $enabled")
    }
    
    suspend fun updateVfxSetting(enabled: Boolean) {
        isVfxEnabled = enabled
        Log.d(TAG, "VFX setting updated: $enabled")
    }
    
    private fun startBackgroundMusic() {
        if (backgroundMusicPlayer?.isPlaying == true) return
        
        try {
            backgroundMusicPlayer = MediaPlayer.create(context, R.raw.casino)
            backgroundMusicPlayer?.apply {
                isLooping = true
                setVolume(MUSIC_VOLUME, MUSIC_VOLUME)
                setOnPreparedListener { 
                    start()
                    Log.d(TAG, "Background music started")
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "Background music error: what=$what, extra=$extra")
                    true
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start background music", e)
        }
    }
    
    private fun stopBackgroundMusic() {
        try {
            backgroundMusicPlayer?.apply {
                if (isPlaying) {
                    stop()
                }
                release()
            }
            backgroundMusicPlayer = null
            Log.d(TAG, "Background music stopped")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop background music", e)
        }
    }
    
    fun playCoinSound() {
        if (!isVfxEnabled || !isInitialized) return
        
        try {
            soundPool?.play(
                coinSoundId,
                SOUND_VOLUME, // left volume
                SOUND_VOLUME, // right volume
                1, // priority
                0, // loop
                1.0f // rate
            )
            Log.d(TAG, "Coin sound played")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to play coin sound", e)
        }
    }
    
    fun pauseBackgroundMusic() {
        try {
            backgroundMusicPlayer?.pause()
            Log.d(TAG, "Background music paused")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to pause background music", e)
        }
    }
    
    fun resumeBackgroundMusic() {
        if (isMusicEnabled && backgroundMusicPlayer != null && isInitialized) {
            try {
                backgroundMusicPlayer?.start()
                Log.d(TAG, "Background music resumed")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to resume background music", e)
            }
        }
    }
    
    fun release() {
        try {
            stopBackgroundMusic()
            soundPool?.release()
            soundPool = null
            isInitialized = false
            Log.d(TAG, "AudioManager released")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to release AudioManager", e)
        }
    }
} 
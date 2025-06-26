package com.maat.cha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.maat.cha.core.audio.AudioManager
import com.maat.cha.core.navigation.root.Root
import com.maat.cha.ui.theme.MaatchaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var audioManager: AudioManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Configure window to handle camera cutout properly
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Hide system bars but allow content to extend into cutout area
        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            MaatchaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Root()
                }
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        audioManager.pauseBackgroundMusic()
    }
    
    override fun onResume() {
        super.onResume()
        audioManager.resumeBackgroundMusic()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        audioManager.release()
    }
}

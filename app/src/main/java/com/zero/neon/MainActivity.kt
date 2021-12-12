package com.zero.neon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zero.neon.audio.AudioPlayer
import com.zero.neon.ui.theme.NeonTheme

class MainActivity : ComponentActivity() {

    private val audioPlayer: AudioPlayer by lazy { AudioPlayer(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NeonTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(color = Color.Transparent)
                GameScreen()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        audioPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        audioPlayer.start()
    }
}
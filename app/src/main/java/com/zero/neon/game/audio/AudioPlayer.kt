package com.zero.neon.game.audio

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.zero.neon.game.settings.GameStatus

@Composable
fun AudioPlayer(gameStatus: GameStatus) {

    val context = LocalContext.current
    val playlist by rememberSaveable { mutableStateOf(Song.values().apply { shuffle() }) }
    var currentPosition by rememberSaveable { mutableStateOf<Long?>(null) }
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            playlist.forEach {
                val mediaItem = MediaItem.fromUri(it.uri)
                addMediaItem(mediaItem)
            }
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            prepare()
        }
    }

    fun playAudio() {
        player.playWhenReady = true
        currentPosition?.let { player.seekTo(it) }
    }

    fun stopAudio() {
        currentPosition = player.currentPosition
        player.playWhenReady = false
    }

    DisposableEffect(Unit) {
        playAudio()
        onDispose {
            stopAudio()
        }
    }
    LaunchedEffect(gameStatus) {
        when (gameStatus) {
            GameStatus.RUNNING -> playAudio()
            GameStatus.PAUSE -> stopAudio()
        }
    }
}

enum class Song(val uri: String) {
    OBLIVION_CITY("https://www.mboxdrive.com/Fury%20Weekend%20-%20Oblivion%20City.mp3"),
    AUTOMATIC_LOVE("https://www.mboxdrive.com/Fury%20Weekend%20-%20Automatic%20Love%20(feat.%20Essenger).mp3"),
    STAR_FIGHTER("https://www.mboxdrive.com/Wice%20-%20Star%20Fighter%20(Official%20Video)%20-%20Magnatron%202.0%20is%20OUT%20NOW%20.mp3")
}

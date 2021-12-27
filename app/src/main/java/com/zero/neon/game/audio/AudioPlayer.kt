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
            else -> stopAudio()
        }
    }
}

enum class Song(val uri: String) {
    THIS_MEANS_WAR("https://firebasestorage.googleapis.com/v0/b/neon-3872b.appspot.com/o/Volkor%20X%20-%20This%20Means%20War.mp3?alt=media&token=fe238e48-add5-4888-bb3b-d63eeae4ca27"),
    THE_BOMB("https://firebasestorage.googleapis.com/v0/b/neon-3872b.appspot.com/o/Volkor%20X%20-%20The%20Bomb.mp3?alt=media&token=5be82681-fe7e-456c-bfc5-431dbc83bcf9")
}

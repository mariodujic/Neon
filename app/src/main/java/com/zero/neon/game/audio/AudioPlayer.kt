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
    THIS_MEANS_WAR("https://bit.ly/3JxYIHF"),
    THE_BOMB("https://bit.ly/3qzTzGo")
}

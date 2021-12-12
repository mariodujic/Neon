package com.zero.neon.audio

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class AudioPlayer(context: Context) {

    private val player = ExoPlayer.Builder(context).build().apply {
        Playlist.values().apply {
            shuffle()
            forEach {
                val mediaItem = MediaItem.fromUri(it.uri)
                addMediaItem(mediaItem)
            }
        }
        repeatMode = ExoPlayer.REPEAT_MODE_ALL
        prepare()
    }

    fun start() {
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    private enum class Playlist(val uri: String) {
        ITEM_ONE("https://www.mboxdrive.com/Fury%20Weekend%20-%20Automatic%20Love%20(feat.%20Essenger).mp3"),
        ITEM_TWO("https://www.mboxdrive.com/MK%20Ultra%20-%20Tears%20in%20The%20Rain.mp3"),
        ITEM_THREE("https://www.mboxdrive.com/DEADLIFE%20-%20Obsolete%20(feat.%20Scandroid)%20[Official%20Lyric%20Video].mp3"),
        ITEM_FOUR("https://www.mboxdrive.com/Wolf%20and%20Raven%20-%20On%20The%20Run.mp3"),
        ITEM_FIVE("https://www.mboxdrive.com/Wice%20-%20Star%20Fighter%20(Official%20Video)%20-%20Magnatron%202.0%20is%20OUT%20NOW%20.mp3")
    }
}
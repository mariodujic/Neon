package com.zero.neon.game.common

sealed class GameTime(val timeMillis: Int) {
    class Millis(timeMillis: Int) : GameTime(timeMillis = timeMillis)
    object None : GameTime(timeMillis = 0)
}
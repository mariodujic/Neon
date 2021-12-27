package com.zero.neon.game.common

sealed class RepeatTime(val timeMillis: Int) {
    class Millis(timeMillis: Int) : RepeatTime(timeMillis = timeMillis)
    object None : RepeatTime(timeMillis = 0)
}
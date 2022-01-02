package com.zero.neon.game.common

import java.io.Serializable

sealed class RepeatTime(val timeMillis: Int) : Serializable {
    class Millis(timeMillis: Int) : RepeatTime(timeMillis = timeMillis)
    object Once : RepeatTime(timeMillis = Int.MAX_VALUE)
    object None : RepeatTime(timeMillis = 0)
}
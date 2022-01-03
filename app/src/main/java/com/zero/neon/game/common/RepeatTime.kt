package com.zero.neon.game.common

import java.io.Serializable

sealed interface RepeatTime : Serializable

class Millis(val timeMillis: Int) : RepeatTime
object Once : RepeatTime
object Never : RepeatTime
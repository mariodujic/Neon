package com.zero.neon.game.explosion.model

import java.io.Serializable

data class Explosion(
    val xOffset: Float,
    val yOffset: Float,
    val size: Float
) : Serializable {

    private val endTimeMillis = System.currentTimeMillis() + 450
    var removed: Boolean = false
        private set

    fun process() {
        if (System.currentTimeMillis() > endTimeMillis) removed = true
    }
}
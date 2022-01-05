package com.zero.neon.game.points.model

import java.io.Serializable

data class Point(
    val xOffset: Float,
    var yOffset: Float,
    val width: Float,
    val value: Int
) : Serializable {

    var alpha: Float = 1f
    private val alphaAnimationSpeed: Float = 0.003f

    private val maxYOffset: Float = yOffset - 80f
    private val yOffsetMovementSpeed: Float = 1f
    var removed: Boolean = false
        private set

    fun processPoint() {
        yOffset -= yOffsetMovementSpeed
        if (yOffset <= maxYOffset) {
            removed = true
        }
        alpha -= alphaAnimationSpeed
    }
}
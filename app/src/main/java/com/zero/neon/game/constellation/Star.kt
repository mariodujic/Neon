package com.zero.neon.game.constellation

import java.io.Serializable

data class Star(
    var xOffset: Float,
    var yOffset: Float,
    private val maxYOffset: Float,
    var size: Float
) : Serializable {

    private val initialStarSize = size
    private var enlargeStar = false
    private val resizeRate = 0.2f

    fun animateStar() {

        yOffset += 0.5f
        if (yOffset >= maxYOffset) {
            yOffset = 0f
        }

        if (enlargeStar) {
            if (size == initialStarSize) {
                enlargeStar = false
            } else {
                size += resizeRate
                yOffset += 2f
            }
        } else {
            if (size - resizeRate > 1) {
                size -= resizeRate
                yOffset += 2
            } else {
                enlargeStar = true
            }
        }
    }
}
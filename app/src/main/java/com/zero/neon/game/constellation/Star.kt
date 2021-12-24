package com.zero.neon.game.constellation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Star(var xOffset: Dp, var yOffset: Dp, private val maxYOffset: Dp, var size: Dp) {

    private val initialStarSize = size
    private var enlargeStar = false
    private val resizeRate = 0.2.dp

    fun animateStar() {

        yOffset += 0.5.dp
        if (yOffset >= maxYOffset) {
            yOffset = 0.dp
        }

        if (enlargeStar) {
            if (size == initialStarSize) {
                enlargeStar = false
            } else {
                size += resizeRate
                yOffset += 2.dp
            }
        } else {
            if (size - resizeRate > 1.dp) {
                size -= resizeRate
                yOffset += 2.dp
            } else {
                enlargeStar = true
            }
        }
    }
}
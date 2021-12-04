package com.zero.neon.constellation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Star(var xOffset: Dp, var yOffset: Dp, var size: Dp) {

    private val initialStarSize = size
    private var enlargeStar = false
    private val resizeRate = 0.2.dp

    fun animateStar() {
        if (enlargeStar) {
            if (size == initialStarSize) {
                enlargeStar = false
            } else {
                size += resizeRate
            }
        } else {
            if (size - resizeRate > 1.dp) {
                size -= resizeRate
            } else {
                enlargeStar = true
            }
        }
    }
}
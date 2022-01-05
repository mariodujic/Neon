package com.zero.neon.game.points.model

import androidx.compose.runtime.Immutable

@Immutable
data class PointUI(
    val xOffset: Float,
    val yOffset: Float,
    val width: Float,
    val value: Int,
    val alpha: Float
)
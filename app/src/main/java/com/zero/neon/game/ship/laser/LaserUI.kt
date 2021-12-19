package com.zero.neon.game.ship.laser

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
class LaserUI(
    val id: String,
    val xOffset: Dp,
    val yOffset: Dp,
    val width: Dp,
    val height: Dp,
    val rotation: Float,
    @DrawableRes val drawableId: Int
)
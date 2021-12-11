package com.zero.neon.game.ship.weapons

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
    @DrawableRes val drawableId: Int
)
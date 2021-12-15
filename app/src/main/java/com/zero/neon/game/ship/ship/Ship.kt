package com.zero.neon.game.ship.ship

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.Dp

data class Ship(
    val width: Dp,
    val height: Dp,
    val shieldSize: Dp,
    var shieldEnabled: Boolean,
    var laserBoosterEnabled: Boolean,
    var xOffset: Dp,
    var yOffset: Dp,
    var hp: Int,
    @DrawableRes val drawableId: Int
)
package com.zero.neon.game.ship.ship

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import com.zero.neon.R

@Immutable
data class Ship(
    val width: Dp,
    val height: Dp,
    val shieldSize: Dp,
    val shieldEnabled: Boolean,
    val laserBoosterEnabled: Boolean,
    val xOffset: Dp,
    val yOffset: Dp,
    val hp: Int,
    @DrawableRes val drawableId: Int = if (laserBoosterEnabled) R.drawable.ship_boosted_laser else R.drawable.ship_regular_laser
)
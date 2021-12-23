package com.zero.neon.game.ship.ship

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R

@Immutable
data class Ship(
    val width: Dp = 85.dp,
    val height: Dp = 90.dp,
    val shieldSize: Dp = height * 2,
    val shieldEnabled: Boolean = false,
    val laserBoosterEnabled: Boolean = false,
    val tripleLaserBoosterEnabled: Boolean = false,
    val xOffset: Dp,
    val yOffset: Dp,
    val hp: Int = 1000,
    @DrawableRes val drawableId: Int = R.drawable.ship_regular_laser
)
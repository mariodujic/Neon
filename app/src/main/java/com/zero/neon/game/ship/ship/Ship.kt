package com.zero.neon.game.ship.ship

import androidx.compose.ui.unit.Dp

data class Ship(
    val size: Dp,
    val shieldSize: Dp = size * 2,
    var shieldEnabled: Boolean = false,
    var laserBoosterEnabled: Boolean = false,
    var xOffset: Dp,
    var yOffset: Dp,
    var hp: Int = 1000,
    val moveLeft: (Boolean) -> Unit,
    val moveRight: (Boolean) -> Unit
)
package com.zero.neon.game.ship.ship

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Ship(
    val size: Dp,
    val shieldSize: Dp,
    var shieldEnabled: Boolean,
    var xOffset: Dp,
    var yOffset: Dp,
    var hp: Int = 1000,
    val moveLeft: (Boolean) -> Unit,
    val moveRight: (Boolean) -> Unit
)
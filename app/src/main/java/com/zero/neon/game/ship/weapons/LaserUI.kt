package com.zero.neon.game.ship.weapons

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
class LaserUI(
    val id: String,
    var xOffset: Dp,
    var yOffset: Dp,
    var width: Dp,
    var height: Dp
)
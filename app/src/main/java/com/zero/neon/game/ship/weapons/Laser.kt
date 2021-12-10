package com.zero.neon.game.ship.weapons

import androidx.compose.ui.unit.Dp

interface Laser {
    val id: String
    var xOffset: Dp
    var yOffset: Dp
    var width: Dp
    var height: Dp
    var shooting: Boolean
    var powerImpact: Int

    fun destroyLaser()
    fun moveLaser()
}
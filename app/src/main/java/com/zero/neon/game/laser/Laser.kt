package com.zero.neon.game.laser

import androidx.compose.ui.unit.Dp

interface Laser {
    val id: String
    var xOffset: Dp
    var yOffset: Dp
    var width: Dp
    var height: Dp
    var rotation: Float
    var impactPower: Int
    val drawableId: Int

    fun destroyLaser()
    fun moveLaser()
}
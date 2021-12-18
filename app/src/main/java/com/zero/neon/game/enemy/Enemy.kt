package com.zero.neon.game.enemy

import androidx.compose.ui.unit.Dp

interface Enemy {
    val enemyId: String
    val width: Dp
    val height: Dp
    var xOffset: Dp
    var yOffset: Dp
    var hp: Int

    fun move()
    fun onObjectImpact(impactPower: Int)
}
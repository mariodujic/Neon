package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

interface Enemy {
    val enemyId: String
    val width: Dp
    val height: Dp
    var xOffset: Dp
    var yOffset: Dp
    var hp: Int
    val impactPower: Int

    fun enemyRect(): Rect
    fun move()
    fun onObjectImpact(impactPower: Int)

    companion object {
        val DEFAULT_X_OFFSET_MOVE_SPEED = 0.5.dp
        val DEFAULT_Y_OFFSET_MOVE_SPEED = 0.2.dp
    }
}
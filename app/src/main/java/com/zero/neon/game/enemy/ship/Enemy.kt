package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Rect
import java.io.Serializable

interface Enemy : Serializable {
    val enemyId: String
    val width: Float
    val height: Float
    var xOffset: Float
    var yOffset: Float
    var hp: Float
    val initialHp: Float
    val impactPower: Int

    fun enemyRect(): Rect
    fun move()
    fun onObjectImpact(impactPower: Int)

    companion object {
        const val DEFAULT_X_OFFSET_MOVE_SPEED: Float = 0.5f
        const val DEFAULT_Y_OFFSET_MOVE_SPEED: Float = 0.2f
    }
}
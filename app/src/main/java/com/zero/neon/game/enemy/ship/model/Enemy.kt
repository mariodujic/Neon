package com.zero.neon.game.enemy.ship.model

import androidx.compose.ui.geometry.Rect
import com.zero.neon.game.laser.Laser
import java.io.Serializable

interface Enemy : Serializable {
    val enemyId: String
    val width: Float
    val height: Float
    var xOffset: Float
    var yOffset: Float
    var hp: Float
    val initialHp: Float
    val impactPower: Float
    val drawableId: Int

    fun enemyRect(): Rect
    fun move()
    fun generateLasers(): List<Laser>
    fun onObjectImpact(impactPower: Float)
}
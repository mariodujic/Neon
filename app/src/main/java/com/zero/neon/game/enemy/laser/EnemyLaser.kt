package com.zero.neon.game.enemy.laser

import com.zero.neon.R
import com.zero.neon.game.laser.Laser
import java.util.*

data class EnemyLaser(
    override var xOffset: Float,
    override var yOffset: Float,
    private val yRange: Float,
    override var width: Float,
    override var height: Float = 30f,
    override val xOffsetMovementSpeed: Float = 0f,
    override val yOffsetMovementSpeed: Float = 3.5f,
    override val drawableId: Int = R.drawable.laser_red_14,
    override var impactPower: Float = 100f
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var rotation: Float = 0f
    override var destroyed: Boolean = false

    override fun moveLaser() {
        xOffset += xOffsetMovementSpeed
        yOffset += yOffsetMovementSpeed
    }
}
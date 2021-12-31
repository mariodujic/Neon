package com.zero.neon.game.ship.laser

import com.zero.neon.R
import com.zero.neon.game.laser.Laser

data class ShipBoostedLaser(
    override val id: String,
    override var xOffset: Float,
    override var yOffset: Float,
    private val yRange: Float,
    override var width: Float = SHIP_BOOSTED_LASER_WIDTH
) : Laser {

    override val xOffsetMovementSpeed: Float = 0f
    override val yOffsetMovementSpeed: Float = 5f
    override var height: Float = 25f
    override var rotation: Float = 0f
    override var impactPower: Float = 100f
    override val drawableId: Int = R.drawable.laser_red_16
    override var destroyed: Boolean = false

    override fun moveLaser() {
        yOffset -= yOffsetMovementSpeed
    }

    companion object {
        const val SHIP_BOOSTED_LASER_WIDTH: Float = 8f
    }
}
package com.zero.neon.game.ship.laser

import com.zero.neon.R
import com.zero.neon.game.laser.Laser

data class ShipBoostedLaser(
    override val id: String,
    override var xOffset: Float,
    override var yOffset: Float,
    private val yRange: Float,
    override var width: Float = SHIP_BOOSTED_LASER_WIDTH,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override var height: Float = 25f
    override var rotation: Float = 0f
    override var impactPower: Int = 100
    override val drawableId: Int = R.drawable.laser_red_16

    override fun moveLaser() {
        if (yOffset > -yRange) {
            yOffset -= SHIP_BOOSTED_LASER_MOVEMENT
        } else {
            destroyLaser()
        }
    }

    override fun destroyLaser() {
        onDestroyLaser(id)
    }

    companion object {
        val SHIP_BOOSTED_LASER_WIDTH: Float = 8f
        val SHIP_BOOSTED_LASER_MOVEMENT: Float = 5f
    }
}
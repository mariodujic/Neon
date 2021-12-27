package com.zero.neon.game.ship.laser

import com.zero.neon.R
import com.zero.neon.game.laser.Laser

data class ShipLaser(
    override val id: String,
    override var xOffset: Float,
    override var yOffset: Float,
    private val yRange: Float,
    override var width: Float = SHIP_LASER_WIDTH,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override var height: Float = 20f
    override var rotation: Float = 0f
    override var impactPower: Int = 25
    override val drawableId: Int = R.drawable.laser_blue_7

    override fun moveLaser() {
        if (yOffset > -yRange) {
            yOffset -= SHIP_LASER_MOVEMENT_SPEED
        } else {
            destroyLaser()
        }
    }

    override fun destroyLaser() {
        onDestroyLaser(id)
    }

    companion object {
        const val SHIP_LASER_WIDTH: Float = 5f
        const val SHIP_LASER_MOVEMENT_SPEED: Float = 7f
    }
}
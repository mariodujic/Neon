package com.zero.neon.game.ship.laser

import com.zero.neon.R
import com.zero.neon.game.laser.Laser

data class UltimateLaser(
    override val id: String,
    override var xOffset: Float,
    override var yOffset: Float = -10f,
    private val yRange: Float,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override var width: Float = 30f
    override var height: Float = 30f
    override var rotation: Float = 0f
    override var impactPower: Int = 1000
    override val drawableId: Int = R.drawable.laser_blue_11

    override fun moveLaser() {
        rotation += 7f
        if (rotation > 360f) rotation = 0f

        if (yOffset > -yRange) {
            yOffset -= ULTIMATE_LASER_MOVEMENT_SPEED
        } else {
            destroyLaser()
        }
    }

    override fun destroyLaser() {
        onDestroyLaser(id)
    }

    companion object {
        const val ULTIMATE_LASER_MOVEMENT_SPEED: Float = 7f
    }
}
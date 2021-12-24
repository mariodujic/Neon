package com.zero.neon.game.ship.laser

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.game.laser.Laser

data class ShipLaser(
    override val id: String,
    override var xOffset: Dp,
    override var yOffset: Dp,
    private val yRange: Dp,
    override var width: Dp = SHIP_LASER_WIDTH,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override var height: Dp = 20.dp
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
        val SHIP_LASER_WIDTH = 5.dp
        val SHIP_LASER_MOVEMENT_SPEED = 7.dp
    }
}
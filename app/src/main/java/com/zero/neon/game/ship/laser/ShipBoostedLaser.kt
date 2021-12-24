package com.zero.neon.game.ship.laser

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.game.laser.Laser

data class ShipBoostedLaser(
    override val id: String,
    override var xOffset: Dp,
    override var yOffset: Dp,
    private val yRange: Dp,
    override var width: Dp = SHIP_BOOSTED_LASER_WIDTH,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override var height: Dp = 25.dp
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
        val SHIP_BOOSTED_LASER_WIDTH = 8.dp
        val SHIP_BOOSTED_LASER_MOVEMENT = 5.dp
    }
}
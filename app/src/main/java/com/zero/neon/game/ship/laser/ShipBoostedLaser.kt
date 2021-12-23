package com.zero.neon.game.ship.laser

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.game.laser.Laser
import java.util.*

data class ShipBoostedLaser(
    override var xOffset: Dp,
    private val yRange: Dp,
    override var width: Dp,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset: Dp = (-10).dp
    override var height: Dp = 25.dp
    override var rotation: Float = 0f
    override var impactPower: Int = 100
    override val drawableId: Int = R.drawable.laser_red_16

    override fun moveLaser() {
        if (yOffset > -yRange) {
            yOffset -= 5.dp
        } else {
            destroyLaser()
        }
    }

    override fun destroyLaser() {
        onDestroyLaser(id)
    }
}
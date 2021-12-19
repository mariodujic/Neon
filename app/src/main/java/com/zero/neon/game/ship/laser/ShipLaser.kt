package com.zero.neon.game.ship.laser

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.game.laser.Laser
import java.util.*

class ShipLaser(
    override var xOffset: Dp,
    private val yRange: Dp,
    override var width: Dp,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset: Dp = (-10).dp
    override var height: Dp = 20.dp
    override var rotation: Float = 0f
    override var impactPower: Int = 25
    override val drawableId: Int = R.drawable.laser_blue_7

    override fun moveLaser() {
        if (yOffset > -yRange) {
            yOffset -= 7.dp
        } else {
            destroyLaser()
        }
    }

    override fun destroyLaser() {
        onDestroyLaser(id)
    }
}
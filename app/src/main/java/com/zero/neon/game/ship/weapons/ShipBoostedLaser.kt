package com.zero.neon.game.ship.weapons

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import java.util.*

class ShipBoostedLaser(
    override var xOffset: Dp,
    private val yRange: Dp,
    override var width: Dp,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset: Dp = (-10).dp
    override var shooting: Boolean = false
    override var height: Dp = 25.dp
    override var powerImpact: Int = 100
    override val drawableId: Int = R.drawable.laser_red_16

    override fun moveLaser() {
        shooting = true
        if (yOffset > -yRange && shooting) {
            yOffset -= 5.dp
        } else {
            destroyLaser()
        }
    }

    override fun destroyLaser() {
        shooting = false
        onDestroyLaser(id)
    }
}
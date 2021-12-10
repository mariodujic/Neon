package com.zero.neon.game.ship.weapons

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

class ShipLaser(
    override var xOffset: Dp,
    private val yRange: Dp,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset: Dp = (-10).dp
    override var shooting: Boolean = false
    override var width: Dp = 5.dp
    override var height: Dp = 20.dp
    override var powerImpact: Int = 25

    override fun moveLaser() {
        shooting = true
        if (yOffset > -yRange && shooting) {
            yOffset -= 7.dp
        } else {
            destroyLaser()
        }
    }

    override fun destroyLaser() {
        shooting = false
        onDestroyLaser(id)
    }
}
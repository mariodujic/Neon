package com.zero.neon.ship

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

class ShipLaser(
    override var xOffset: Dp,
    private val yRange: Dp,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset: Dp by mutableStateOf((-10).dp)
    override var shooting: Boolean = false
    override var powerImpact: Int by mutableStateOf(5)

    fun moveLaser() {
        shooting = true
        if (yOffset > -yRange || !shooting) {
            yOffset -= 7.dp
        } else {
            shooting = false
        }
    }

    override fun destroyLaser() {
        shooting = false
        onDestroyLaser(id)
    }
}

interface Laser {
    val id: String
    var xOffset: Dp
    var yOffset: Dp
    var shooting: Boolean
    var powerImpact: Int

    fun destroyLaser()
}
package com.zero.neon.game.enemy.laser

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.game.laser.Laser
import java.util.*

class EnemyLaser(
    override var xOffset: Dp,
    override var yOffset: Dp,
    private val yRange: Dp,
    override var width: Dp,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var height: Dp = 30.dp
    override var rotation: Float = 0f
    override var impactPower: Int = 100
    override val drawableId: Int = R.drawable.laser_red_12

    override fun moveLaser() {
        yOffset += 3.5.dp
        if (yOffset > yRange) destroyLaser()
    }

    override fun destroyLaser() {
        onDestroyLaser(id)
    }
}
package com.zero.neon.game.enemy.laser

import com.zero.neon.game.laser.Laser
import com.zero.neon.R
import java.util.*

class EnemyLaser(
    override var xOffset: Float,
    override var yOffset: Float,
    private val yRange: Float,
    override var width: Float,
    private val onDestroyLaser: (laserId: String) -> Unit
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var height: Float = 30f
    override var rotation: Float = 0f
    override var impactPower: Int = 100
    override val drawableId: Int = R.drawable.laser_red_14

    override fun moveLaser() {
        yOffset += 3.5f
        if (yOffset > yRange) destroyLaser()
    }

    override fun destroyLaser() {
        onDestroyLaser(id)
    }
}
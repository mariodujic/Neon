package com.zero.neon.game.explosion.controller

import com.zero.neon.game.common.Millis
import com.zero.neon.game.explosion.model.Explosion
import java.util.*

class ExplosionController(
    initialExplosions: List<Explosion>,
    private val updateExplosions: (List<Explosion>) -> Unit
) {

    private var explosions: List<Explosion> = initialExplosions

    fun addExplosion(xOffset: Float, yOffset: Float, width: Float, height: Float) {
        val size: Float = maxOf(a = width, b = height)
        val explosion = Explosion(
            xOffset = xOffset - size,
            yOffset = yOffset - size,
            size = size * 2
        )
        explosions += explosion
        updateExplosions(explosions)
    }

    val processExplosionsId = UUID.randomUUID().toString()
    val processExplosionsRepeatTime = Millis(5)
    fun processExplosions() {
        explosions.forEach {
            it.process()
            if (it.removed) explosions -= it
        }
        updateExplosions(explosions)
    }
}
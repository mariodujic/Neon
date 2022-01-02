package com.zero.neon.game.booster

import java.io.Serializable
import kotlin.random.Random

data class Booster(
    val id: String,
    var xOffset: Float,
    var size: Float,
    private val screenHeight: Float,
    var collected: Boolean = false
) : Serializable {

    var yOffset = 1f
    private val boosters: Array<BoosterType> = BoosterType.values()
    private val index: Int = Random.nextInt(0, boosters.size)
    val type: BoosterType = boosters[index]
    val impactPower = when (type) {
        /**
         * This booster type heals space ship on impact.
         */
        BoosterType.HEALTH_BOOSTER -> -50
        else -> 0
    }

    fun moveObject() {
        if (yOffset < screenHeight + 100) {
            yOffset += 1
        } else {
            collected = true
        }
    }

    fun collect() {
        collected = true
    }
}
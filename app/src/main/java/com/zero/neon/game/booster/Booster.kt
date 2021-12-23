package com.zero.neon.game.booster

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.random.Random

class Booster(
    var xOffset: Dp,
    var size: Dp,
    private val screenHeight: Dp,
    private val onDestroyBooster: (boosterId: String) -> Unit
) {

    val id: String = UUID.randomUUID().toString()
    var yOffset by mutableStateOf(1.dp)
    private val randomDrawableIndex = Random.nextInt(0, BoosterType.values().size)
    val type: BoosterType = BoosterType.values()[randomDrawableIndex]
    var hp: Int = 1
    val impactPower = when (randomDrawableIndex) {
        /**
         * This booster type heals space ship on impact.
         */
        BoosterType.HEALTH_BOOSTER.ordinal -> -50
        else -> 0
    }

    fun moveObject() {
        if (yOffset < screenHeight + 100.dp) {
            yOffset += 1.dp
        } else {
            onDestroyBooster(id)
        }
    }

    fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
        if (hp <= 0) {
            onDestroyBooster(id)
        }
    }
}
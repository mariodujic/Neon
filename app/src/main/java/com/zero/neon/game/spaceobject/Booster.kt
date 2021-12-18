package com.zero.neon.game.spaceobject

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.random.Random

class Booster(
    override var xOffset: Dp,
    override var size: Dp,
    private val screenHeight: Dp,
    private val onDestroyBooster: (boosterId: String) -> Unit
) : SpaceObject {

    override val id: String = UUID.randomUUID().toString()
    override val destroyable: Boolean = false
    override val collectable: Boolean = true
    override var yOffset by mutableStateOf(1.dp)
    private val randomDrawableIndex = Random.nextInt(0, BoosterType.values().size)
    override val drawableId: Int = BoosterType.values()[randomDrawableIndex].drawableId
    override var hp: Int = 1
    override val impactPower = when (randomDrawableIndex) {
        /**
         * This booster type heals space ship on impact.
         */
        BoosterType.HEALTH_BOOSTER.ordinal -> -50
        else -> 0
    }
    override var rotation = 0f

    override fun moveObject() {
        if (yOffset < screenHeight + 100.dp) {
            yOffset += 1.dp
        } else {
            onDestroyBooster(id)
        }
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
        if (hp <= 0) {
            onDestroyBooster(id)
        }
    }
}
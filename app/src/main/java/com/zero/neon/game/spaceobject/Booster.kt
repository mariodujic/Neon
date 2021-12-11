package com.zero.neon.game.spaceobject

import androidx.annotation.DrawableRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
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
    override var floating by mutableStateOf(false)
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
        floating = true
        if (yOffset < screenHeight + 100.dp && floating) {
            yOffset += 1.dp
        } else {
            floating = false
            destroyObject()
        }
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
        if (hp <= 0) {
            destroyObject()
        }
    }

    private fun destroyObject() {
        floating = false
        onDestroyBooster(id)
    }

    enum class BoosterType(@DrawableRes val drawableId: Int) {
        ULTIMATE_WEAPON_BOOSTER(R.drawable.booster_green),
        SHIELD_BOOSTER(R.drawable.booster_1),
        HEALTH_BOOSTER(R.drawable.booster_health_2),
    }
}
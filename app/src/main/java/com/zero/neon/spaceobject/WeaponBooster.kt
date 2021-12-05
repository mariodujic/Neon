package com.zero.neon.spaceobject

import androidx.annotation.DrawableRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import java.util.*
import kotlin.random.Random

class WeaponBooster(
    override var xOffset: Dp,
    override var size: Dp,
    private val screenHeight: Dp,
    private val onDestroyBooster: (boosterId: String) -> Unit
) : SpaceObject {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset by mutableStateOf(1.dp)
    private val randomDrawableIndex = Random.nextInt(0, BoosterType.values().size)
    override val drawableId: Int = BoosterType.values()[randomDrawableIndex].drawableId
    override var floating by mutableStateOf(false)
    override val rect by derivedStateOf {
        Rect(
            offset = Offset(x = xOffset.value, y = yOffset.value),
            size = Size(width = size.value, height = size.value)
        )
    }

    override fun moveObject() {
        floating = true
        if (yOffset < screenHeight + 100.dp && floating) {
            yOffset += 1.dp
        } else {
            floating = false
            destroyObject()
        }
    }

    override fun destroyObject() {
        floating = false
        onDestroyBooster(id)
    }

    enum class BoosterType(@DrawableRes val drawableId: Int) {
        WEAPON_BOOSTER_ONE(R.drawable.weapon_boost),
    }
}
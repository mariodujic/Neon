package com.zero.neon.game.spaceobject

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import java.util.*
import kotlin.random.Random

class SpaceRock(
    override var xOffset: Dp,
    override var size: Dp,
    private val screenHeight: Dp,
    private val onDestroyRock: (rockId: String) -> Unit
) : SpaceObject {
    override val id: String = UUID.randomUUID().toString()
    override val destroyable: Boolean = true
    override val collectable: Boolean = false
    override var yOffset = 1.dp
    private val randomDrawableIndex = Random.nextInt(0, RockType.values().size)
    override val drawableId: Int = RockType.values()[randomDrawableIndex].drawableId
    override var hp: Int = size.value.toInt()
    override val impactPower = size.value.toInt()
    private var rotateClockWise = Random.nextBoolean()
    override var rotation = 0f
    private val minRotationSpeed = 0.05f
    private val maxRotationSpeed = 0.20f
    private val rotationSpeed =
        Random.nextFloat() * (maxRotationSpeed - minRotationSpeed) + minRotationSpeed

    override fun moveObject() {
        if (rotateClockWise) rotation += rotationSpeed else rotation -= rotationSpeed

        if (yOffset < screenHeight + 100.dp) {
            yOffset += 1.dp
        } else {
            onDestroyRock(id)
        }
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
        if (hp <= 0) {
            onDestroyRock(id)
        }
    }

    enum class RockType(@DrawableRes val drawableId: Int) {
        ROCK_ONE(R.drawable.space_rock_1),
        ROCK_TWO(R.drawable.space_rock_2),
        ROCK_THREE(R.drawable.space_rock_3),
        ROCK_FOUR(R.drawable.space_rock_4)
    }
}
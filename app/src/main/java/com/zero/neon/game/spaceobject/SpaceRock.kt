package com.zero.neon.game.spaceobject

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.zero.neon.R
import java.util.*
import kotlin.random.Random

class SpaceRock(
    override var xOffset: Float,
    override var size: Float,
    private val screenHeight: Float
) : SpaceObject {
    override val id: String = UUID.randomUUID().toString()
    override val destroyable: Boolean = true
    override var yOffset: Float = 1f
    private val randomDrawableIndex = Random.nextInt(0, RockType.values().size)
    override val drawableId: Int = RockType.values()[randomDrawableIndex].drawableId
    override var hp: Float = size
    override val impactPower = size.toInt()
    private var rotateClockWise = Random.nextBoolean()
    override var rotation = 0f
    private val minRotationSpeed = 0.05f
    private val maxRotationSpeed = 0.20f
    private val rotationSpeed =
        Random.nextFloat() * (maxRotationSpeed - minRotationSpeed) + minRotationSpeed

    override fun spaceObjectRect(): Rect {
        return Rect(
            center = Offset(
                x = xOffset + size / 2,
                y = yOffset + size / 2
            ),
            radius = size / 2
        )
    }

    override fun moveObject() {
        if (rotateClockWise) rotation += rotationSpeed else rotation -= rotationSpeed

        if (yOffset < screenHeight + 100) {
            yOffset += 1
        } else {
            hp = 0f
        }
    }

    override fun onObjectImpact(impactPower: Float) {
        hp -= impactPower
    }

    enum class RockType(@DrawableRes val drawableId: Int) {
        ROCK_ONE(R.drawable.space_rock_1),
        ROCK_TWO(R.drawable.space_rock_2),
        ROCK_THREE(R.drawable.space_rock_3),
        ROCK_FOUR(R.drawable.space_rock_4)
    }
}
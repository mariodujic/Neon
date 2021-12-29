package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.zero.neon.R
import com.zero.neon.game.enemy.laser.EnemyLaser
import com.zero.neon.game.laser.Laser
import com.zero.neon.game.ship.ship.Ship
import java.util.*

class LevelOneBoss(
    screenWidthDp: Float,
    private val screenHeightDp: Float,
    private val getShip: () -> Ship
) : Enemy {

    override val enemyId: String = UUID.randomUUID().toString()
    override val width: Float = 150f
    override val height: Float = 100f
    override var hp: Float = 3000f
    override val initialHp: Float = hp
    override val impactPower: Float = 10f
    override val drawableId: Int = R.drawable.enemy_level_one_boss
    private val bossMovementSpeed = 0.5f

    private val minXOffset = width
    private val maxXOffset = screenWidthDp - width
    private val minYOffset = height / 2
    private val maxYOffset = screenHeightDp / 2

    private var movement: Movement = Movement.TOP_LEFT_TOP_RIGHT

    override var xOffset: Float = minXOffset
    override var yOffset: Float = minYOffset

    override fun enemyRect(): Rect {
        return Rect(
            center = Offset(
                x = xOffset + width / 2,
                y = yOffset + height / 2
            ),
            radius = width / 2
        )
    }

    override fun move() {
        if (xOffset <= 0 && yOffset <= minYOffset) {
            movement = Movement.TOP_LEFT_TOP_RIGHT
        } else if (yOffset <= minYOffset && xOffset >= maxXOffset) {
            movement = Movement.TOP_RIGHT_BOTTOM_RIGHT
        } else if (xOffset >= maxXOffset && yOffset >= maxYOffset) {
            movement = Movement.BOTTOM_RIGHT_BOTTOM_LEFT
        } else if (yOffset >= maxYOffset && xOffset <= 0) {
            movement = Movement.BOTTOM_LEFT_TOP_RIGHT
        }

        when (movement) {
            Movement.TOP_LEFT_TOP_RIGHT -> xOffset += bossMovementSpeed
            Movement.TOP_RIGHT_BOTTOM_RIGHT -> yOffset += bossMovementSpeed
            Movement.BOTTOM_RIGHT_BOTTOM_LEFT -> xOffset -= bossMovementSpeed
            Movement.BOTTOM_LEFT_TOP_RIGHT -> yOffset -= bossMovementSpeed
        }
    }

    override fun generateLaser(): Laser {
        val width = 30f
        val ship: Ship = getShip()
        val xOffsetDiff = ship.xOffset - xOffset
        val yOffsetDiff = ship.yOffset - yOffset
        val xOffsetMovementSpeed = xOffsetDiff / (yOffsetDiff - 2)
        val yOffMovementSpeed = yOffsetDiff / (yOffsetDiff - 2)
        return EnemyLaser(
            xOffset = xOffset + this.width / 2 - width / 2,
            yOffset = yOffset + height,
            yRange = screenHeightDp,
            width = width,
            height = width,
            xOffsetMovementSpeed = xOffsetMovementSpeed,
            yOffsetMovementSpeed = yOffMovementSpeed,
            drawableId = R.drawable.laser_red_8
        )
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
    }

    private enum class Movement {
        TOP_LEFT_TOP_RIGHT,
        TOP_RIGHT_BOTTOM_RIGHT,
        BOTTOM_RIGHT_BOTTOM_LEFT,
        BOTTOM_LEFT_TOP_RIGHT
    }
}
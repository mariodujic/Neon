package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.zero.neon.game.enemy.laser.EnemyLaser
import com.zero.neon.game.laser.Laser
import java.util.*

class LevelOneEnemy(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    type: LevelOneEnemyType
) : Enemy {

    override val enemyId: String = UUID.randomUUID().toString()
    override val width: Float = type.width
    override val height: Float = type.height
    override var hp: Float = type.hp
    override val initialHp: Float = hp
    override val impactPower: Float = type.impactPower
    override var xOffset: Float = type.spawnPosition.let {
        when (it) {
            EnemySpawnPosition.LEFT -> 0f
            EnemySpawnPosition.RIGHT -> screenWidthDp
        }
    }
    override var yOffset: Float = 0f
    override val drawableId: Int = type.drawableId
    private var moveRight = true
    private val xOffsetMovementSpeed = type.xOffsetSpeed
    private val yOffsetMovementSpeed = type.yOffsetSpeed

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
        if (moveRight) {
            xOffset += xOffsetMovementSpeed
            if (xOffset + width > screenWidthDp) moveRight = false
        } else {
            xOffset -= xOffsetMovementSpeed
            if (xOffset < 0f) moveRight = true
        }
        yOffset += yOffsetMovementSpeed

        if (yOffset + height > screenHeightDp) hp = 0f
    }

    override fun generateLaser(): Laser {
        val laserWidth = 18f
        return EnemyLaser(
            xOffset = xOffset + width / 2 - laserWidth / 2,
            yOffset = yOffset + height,
            yRange = screenHeightDp,
            width = laserWidth
        )
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
    }
}
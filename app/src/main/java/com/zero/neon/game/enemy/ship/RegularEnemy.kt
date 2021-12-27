package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.zero.neon.game.enemy.ship.Enemy.Companion.DEFAULT_X_OFFSET_MOVE_SPEED
import com.zero.neon.game.enemy.ship.Enemy.Companion.DEFAULT_Y_OFFSET_MOVE_SPEED
import java.util.*

class RegularEnemy(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    enemySpawnAttributes: EnemySpawnAttributes?
) : Enemy {

    override val enemyId: String = UUID.randomUUID().toString()
    override val width: Float = 40f
    override val height: Float = 40f
    override var hp: Int = 250
    override val impactPower: Int = 250
    override var xOffset: Float = enemySpawnAttributes?.spawnPosition?.let {
        when (it) {
            EnemySpawnPosition.LEFT -> 0f
            EnemySpawnPosition.RIGHT -> screenWidthDp
        }
    } ?: 0f
    override var yOffset: Float = 0f
    private var moveRight = true
    private val xOffsetMoveSpeed = enemySpawnAttributes?.xOffsetSpeed ?: DEFAULT_X_OFFSET_MOVE_SPEED
    private val yOffsetMoveSpeed = enemySpawnAttributes?.yOffsetSpeed ?: DEFAULT_Y_OFFSET_MOVE_SPEED

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
            xOffset += xOffsetMoveSpeed
            if (xOffset + width > screenWidthDp) moveRight = false
        } else {
            xOffset -= xOffsetMoveSpeed
            if (xOffset < 0f) moveRight = true
        }
        yOffset += yOffsetMoveSpeed

        if (yOffset + height > screenHeightDp) hp = 0
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
    }
}
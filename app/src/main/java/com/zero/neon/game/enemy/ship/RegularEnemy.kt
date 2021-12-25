package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.game.enemy.ship.Enemy.Companion.DEFAULT_X_OFFSET_MOVE_SPEED
import com.zero.neon.game.enemy.ship.Enemy.Companion.DEFAULT_Y_OFFSET_MOVE_SPEED
import java.util.*

class RegularEnemy(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    enemySpawnAttributes: EnemySpawnAttributes?,
    private val onDestroyEnemy: (enemyId: String) -> Unit
) : Enemy {

    override val enemyId: String = UUID.randomUUID().toString()
    override val width: Dp = 40.dp
    override val height: Dp = 40.dp
    override var hp: Int = 250
    override val impactPower: Int = 250
    override var xOffset: Dp = enemySpawnAttributes?.spawnPosition?.let {
        when (it) {
            EnemySpawnPosition.LEFT -> 0.dp
            EnemySpawnPosition.RIGHT -> screenWidthDp
        }
    } ?: 0.dp
    override var yOffset: Dp = 0.dp
    private var moveRight = true
    private val xOffsetMoveSpeed = enemySpawnAttributes?.xOffsetSpeed ?: DEFAULT_X_OFFSET_MOVE_SPEED
    private val yOffsetMoveSpeed = enemySpawnAttributes?.yOffsetSpeed ?: DEFAULT_Y_OFFSET_MOVE_SPEED

    override fun enemyRect(): Rect {
        return Rect(
            center = Offset(
                x = xOffset.value + width.value / 2,
                y = yOffset.value + height.value / 2
            ),
            radius = width.value / 2
        )
    }

    override fun move() {
        if (moveRight) {
            xOffset += xOffsetMoveSpeed
            if (xOffset + width > screenWidthDp) moveRight = false
        } else {
            xOffset -= xOffsetMoveSpeed
            if (xOffset < 0.dp) moveRight = true
        }
        yOffset += yOffsetMoveSpeed

        if (yOffset + height > screenHeightDp) onDestroyEnemy(enemyId)
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
        if (hp <= 0) {
            onDestroyEnemy(enemyId)
        }
    }
}
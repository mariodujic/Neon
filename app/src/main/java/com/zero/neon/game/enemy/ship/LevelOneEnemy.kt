package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.util.*

class LevelOneEnemy(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    attributesLevelOne: LevelOneEnemyAttributes
) : Enemy {

    override val enemyId: String = UUID.randomUUID().toString()
    override val width: Float = attributesLevelOne.enemyType.width
    override val height: Float = attributesLevelOne.enemyType.height
    override var hp: Float = attributesLevelOne.enemyType.hp
    override val initialHp: Float = hp
    override val impactPower: Float = attributesLevelOne.enemyType.impactPower
    override var xOffset: Float = attributesLevelOne.spawnPosition.let {
        when (it) {
            EnemySpawnPosition.LEFT -> 0f
            EnemySpawnPosition.RIGHT -> screenWidthDp
        }
    }
    override var yOffset: Float = 0f
    override val drawableId: Int = attributesLevelOne.enemyType.drawableId
    private var moveRight = true
    private val xOffsetMoveSpeed = attributesLevelOne.xOffsetSpeed
    private val yOffsetMoveSpeed = attributesLevelOne.yOffsetSpeed

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

        if (yOffset + height > screenHeightDp) hp = 0f
    }

    override fun onObjectImpact(impactPower: Int) {
        hp -= impactPower
    }
}
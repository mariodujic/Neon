package com.zero.neon.game.enemy.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import java.util.*

class LevelOneEnemy(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    attributes: LevelOneEnemyAttributes
) : Enemy {

    override val enemyId: String = UUID.randomUUID().toString()
    override val width: Float = attributes.enemyType.width
    override val height: Float = attributes.enemyType.height
    override var hp: Float = attributes.enemyType.hp
    override val initialHp: Float = hp
    override val impactPower: Float = attributes.enemyType.impactPower
    override var xOffset: Float = attributes.spawnPosition.let {
        when (it) {
            EnemySpawnPosition.LEFT -> 0f
            EnemySpawnPosition.RIGHT -> screenWidthDp
        }
    }
    override var yOffset: Float = 0f
    override val drawableId: Int = attributes.enemyType.drawableId
    private var moveRight = true
    private val xOffsetMoveSpeed = attributes.xOffsetSpeed
    private val yOffsetMoveSpeed = attributes.yOffsetSpeed

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
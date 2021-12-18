package com.zero.neon.game.enemy

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

class RegularEnemy(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val onDestroyEnemy: (enemyId: String) -> Unit
) : Enemy {

    override val enemyId: String = UUID.randomUUID().toString()
    override val width: Dp = 40.dp
    override val height: Dp = 40.dp
    override var xOffset: Dp = 0.dp
    override var yOffset: Dp = 0.dp
    override var hp: Int = 250

    private var moveRight = true
    private val xOffsetMoveSpeed = 0.5.dp
    private val yOffsetMoveSpeed = 0.2.dp

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
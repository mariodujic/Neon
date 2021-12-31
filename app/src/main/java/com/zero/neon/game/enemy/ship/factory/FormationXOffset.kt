package com.zero.neon.game.enemy.ship.factory

import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.enemy.ship.model.Row
import com.zero.neon.game.enemy.ship.model.ZigZag
import com.zero.neon.game.enemy.ship.model.ZigZagInitialPosition

class FormationXOffset(private val screenWidth: Float) {

    fun zigZagXOffset(formation: ZigZag): Float {
        return if (formation.position == ZigZagInitialPosition.LEFT) 0f else screenWidth
    }

    fun rowXOffset(formation: Row, previousEnemy: Enemy?, enemyWidth: Float): Float {
        val divider = formation.rowCount + 1
        val distanceBetween = screenWidth / divider - enemyWidth / divider
        return previousEnemy?.let { it.xOffset + distanceBetween } ?: distanceBetween
    }
}
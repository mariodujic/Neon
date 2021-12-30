package com.zero.neon.game.enemy.ship

class FormationXOffset(private val screenWidth: Float) {

    fun zigZagXOffset(formation: ZigZag): Float {
        return if (formation.position == ZigZagInitialPosition.LEFT) 0f else screenWidth
    }

    fun rectangleXOffset(formation: Row, previousEnemy: Enemy?, enemyWidth: Float): Float {
        val divider = formation.rowCount + 1
        val distanceBetween = screenWidth / divider - enemyWidth / divider
        return previousEnemy?.let { it.xOffset + distanceBetween } ?: distanceBetween
    }
}
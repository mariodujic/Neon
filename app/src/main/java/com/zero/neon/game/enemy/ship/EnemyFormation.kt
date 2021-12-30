package com.zero.neon.game.enemy.ship

sealed interface EnemyFormation

data class ZigZag(val position: ZigZagInitialPosition) : EnemyFormation
data class Row(val rowCount: Int) : EnemyFormation

enum class ZigZagInitialPosition {
    LEFT,
    RIGHT
}
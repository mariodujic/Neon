package com.zero.neon.game.enemy.ship.model

import java.io.Serializable

sealed interface EnemyFormation : Serializable

data class ZigZag(val position: ZigZagInitialPosition) : EnemyFormation
data class Row(val rowCount: Int) : EnemyFormation

enum class ZigZagInitialPosition {
    LEFT,
    RIGHT
}
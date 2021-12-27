package com.zero.neon.game.enemy.ship

import com.zero.neon.game.common.GameTime

sealed interface EnemyAttributes

data class LevelOneEnemyAttributes(
    val enemyType: LevelOneEnemyType,
    val spawnPosition: EnemySpawnPosition,
    val xOffsetSpeed: Float,
    val yOffsetSpeed: Float,
    val enemySpawnRateMillis: GameTime,
    val enemyFireRateMillis: GameTime
) : EnemyAttributes
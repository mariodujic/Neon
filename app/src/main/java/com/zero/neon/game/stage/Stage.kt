package com.zero.neon.game.stage

import androidx.compose.ui.unit.dp
import com.zero.neon.game.enemy.ship.EnemySpawnAttributes
import com.zero.neon.game.enemy.ship.EnemySpawnPosition

enum class Stage(
    val spaceRockSpawnRateMillis: Int,
    val enemySpawnRateMillis: Int,
    val enemyEnemySpawnAttributes: EnemySpawnAttributes?,
    private val endTimeSec: Long
) {
    ONE(
        spaceRockSpawnRateMillis = 500,
        enemySpawnRateMillis = 0,
        enemyEnemySpawnAttributes = null,
        endTimeSec = 10
    ),
    TWO(
        spaceRockSpawnRateMillis = 0,
        enemySpawnRateMillis = 1000,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.LEFT,
            xOffsetSpeed = 0.6.dp,
            yOffsetSpeed = 0.5.dp
        ),
        20
    ),
    THREE(
        spaceRockSpawnRateMillis = 1000,
        enemySpawnRateMillis = 1000,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.RIGHT,
            xOffsetSpeed = 0.7.dp,
            yOffsetSpeed = 0.5.dp
        ),
        endTimeSec = 30
    ),
    FOUR(
        spaceRockSpawnRateMillis = 0,
        enemySpawnRateMillis = 800,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.LEFT,
            xOffsetSpeed = 0.8.dp,
            yOffsetSpeed = 0.5.dp
        ),
        endTimeSec = 40
    ),
    FIVE(
        1000,
        750,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.RIGHT,
            xOffsetSpeed = 0.8.dp,
            yOffsetSpeed = 0.8.dp
        ),
        endTimeSec = 50
    );

    companion object {
        fun getCurrentGameStage(currentTimeSec: Long): Stage {
            return values()
                .sortedBy { it.endTimeSec }
                .firstOrNull { currentTimeSec < it.endTimeSec }
                ?: values().last()
        }
    }
}
package com.zero.neon.game.stage

import androidx.compose.ui.unit.dp
import com.zero.neon.game.enemy.ship.EnemySpawnAttributes
import com.zero.neon.game.enemy.ship.EnemySpawnPosition

enum class Stage(
    val spaceRockSpawnRateMillis: Int,
    val enemySpawnRateMillis: Int,
    val enemyEnemySpawnAttributes: EnemySpawnAttributes?,
    private val durationSec: Long
) {
    ONE(
        spaceRockSpawnRateMillis = 500,
        enemySpawnRateMillis = 0,
        enemyEnemySpawnAttributes = null,
        durationSec = 10
    ),
    TWO(
        spaceRockSpawnRateMillis = 0,
        enemySpawnRateMillis = 1000,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.LEFT,
            xOffsetSpeed = 0.6.dp,
            yOffsetSpeed = 0.5.dp
        ),
        durationSec = 10
    ),
    THREE(
        spaceRockSpawnRateMillis = 1000,
        enemySpawnRateMillis = 1000,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.RIGHT,
            xOffsetSpeed = 0.7.dp,
            yOffsetSpeed = 0.5.dp
        ),
        durationSec = 10
    ),
    FOUR(
        spaceRockSpawnRateMillis = 0,
        enemySpawnRateMillis = 800,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.LEFT,
            xOffsetSpeed = 0.8.dp,
            yOffsetSpeed = 0.5.dp
        ),
        durationSec = 10
    ),
    FIVE(
        spaceRockSpawnRateMillis = 1000,
        enemySpawnRateMillis = 750,
        enemyEnemySpawnAttributes = EnemySpawnAttributes(
            spawnPosition = EnemySpawnPosition.RIGHT,
            xOffsetSpeed = 0.8.dp,
            yOffsetSpeed = 0.8.dp
        ),
        durationSec = 10
    );

    companion object {
        private val stages = mutableListOf<Long>().apply {
            values().forEachIndexed { index, stage ->
                add((getOrNull(index - 1) ?: 0) + stage.durationSec)
            }
        }

        fun getCurrentGameStage(currentTimeSec: Long): Stage {
            val index = stages.indexOfFirst { currentTimeSec < it }
            return values().getOrNull(index) ?: values().last()
        }
    }
}
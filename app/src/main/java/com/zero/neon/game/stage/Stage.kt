package com.zero.neon.game.stage

import androidx.compose.ui.unit.dp
import com.zero.neon.game.enemy.ship.EnemySpawnAttributes
import com.zero.neon.game.enemy.ship.EnemySpawnPosition

enum class Stage(
    val stagePartition: StagePartition,
    private val durationSec: Long
) {
    STAGE_ONE_MESSAGE_ONE(
        StageMessagePartition("Stage 1"),
        durationSec = 3
    ),
    STAGE_ONE_MESSAGE_TWO(
        StageMessagePartition("Get ready!"),
        durationSec = 3
    ),
    STAGE_ONE_MESSAGE_THREE(
        StageMessagePartition("GO!"),
        durationSec = 1
    ),
    STAGE_ONE_GAME_ONE(
        stagePartition = StageGamePartition(
            spaceRockSpawnRateMillis = 200,
            enemySpawnRateMillis = 0,
            enemyEnemySpawnAttributes = null,
        ),
        durationSec = 10
    ),
    STAGE_TWO_BREAK_ONE(
        stagePartition = StageBreakPartition,
        durationSec = 3
    ),
    STAGE_TWO_MESSAGE_ONE(
        stagePartition = StageMessagePartition("Well done!"),
        durationSec = 3
    ),
    STAGE_TWO_MESSAGE_TWO(
        stagePartition = StageMessagePartition("Stage 2"),
        durationSec = 3
    ),
    STAGE_TWO_MESSAGE_THREE(
        stagePartition = StageMessagePartition("GO!"),
        durationSec = 1
    ),
    STAGE_TWO_GAME_ONE(
        stagePartition = StageGamePartition(
            spaceRockSpawnRateMillis = 0,
            enemySpawnRateMillis = 1000,
            enemyEnemySpawnAttributes = EnemySpawnAttributes(
                spawnPosition = EnemySpawnPosition.LEFT,
                xOffsetSpeed = 0.6.dp,
                yOffsetSpeed = 0.5.dp
            )
        ),
        durationSec = 10
    ),
    STAGE_THREE_BREAK_ONE(
        stagePartition = StageBreakPartition,
        durationSec = 3
    ),
    STAGE_THREE_MESSAGE_ONE(
        stagePartition = StageMessagePartition("Easy.."),
        durationSec = 3
    ),
    STAGE_THREE_MESSAGE_TWO(
        stagePartition = StageMessagePartition("Stage 3"),
        durationSec = 3
    ),
    STAGE_THREE_MESSAGE_THREE(
        stagePartition = StageMessagePartition("GO!"),
        durationSec = 1
    ),
    GAME_THREE(
        stagePartition = StageGamePartition(
            spaceRockSpawnRateMillis = 500,
            enemySpawnRateMillis = 500,
            enemyEnemySpawnAttributes = EnemySpawnAttributes(
                spawnPosition = EnemySpawnPosition.RIGHT,
                xOffsetSpeed = 0.7.dp,
                yOffsetSpeed = 0.5.dp
            )
        ),
        durationSec = 10
    );

    companion object {
        private var stageStartSnapshotMillis: Long = System.currentTimeMillis()
        private var stageIndex = 0

        fun getGameStage(readyForNextStage: Boolean): Stage {
            val currentStage = values()[stageIndex]
            val stageTimeExpired =
                stageStartSnapshotMillis + currentStage.durationSec * 1000 < System.currentTimeMillis()
            val hasNextStage = stageIndex < values().lastIndex
            if (
                stageTimeExpired &&
                (currentStage.stagePartition !is StageBreakPartition || readyForNextStage) &&
                hasNextStage
            ) {
                stageIndex++
                stageStartSnapshotMillis = System.currentTimeMillis()
            }
            return values()[stageIndex]
        }
    }
}
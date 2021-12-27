package com.zero.neon.game.stage

import com.zero.neon.game.common.RepeatTime
import com.zero.neon.game.enemy.ship.EnemySpawnPosition
import com.zero.neon.game.enemy.ship.LevelOneEnemyAttributes
import com.zero.neon.game.enemy.ship.LevelOneEnemyType

enum class Stage(
    val stageAct: StageAct,
    val durationSec: Long
) {
    STAGE_ONE_MESSAGE_ONE(
        StageMessageAct("Stage 1"),
        durationSec = 3
    ),
    STAGE_ONE_MESSAGE_TWO(
        StageMessageAct("Get ready!"),
        durationSec = 3
    ),
    STAGE_ONE_MESSAGE_THREE(
        StageMessageAct("GO!"),
        durationSec = 1
    ),
    STAGE_ONE_GAME_ONE(
        stageAct = StageGameAct(
            spaceRockSpawnRateMillis = RepeatTime.Millis(timeMillis = 1000),
            enemyAttributes = LevelOneEnemyAttributes(
                spawnPosition = EnemySpawnPosition.LEFT,
                xOffsetSpeed = 0.4f,
                yOffsetSpeed = 0.4f,
                enemySpawnRateMillis = RepeatTime.Millis(timeMillis = 1000),
                enemyFireRateMillis = RepeatTime.None,
                enemyType = LevelOneEnemyType.TIER_THREE
            )
        ),
        durationSec = 10
    ),
    STAGE_TWO_BREAK_ONE(
        stageAct = StageBreakAct,
        durationSec = 3
    ),
    STAGE_TWO_MESSAGE_ONE(
        stageAct = StageMessageAct("Well done!"),
        durationSec = 3
    ),
    STAGE_TWO_MESSAGE_TWO(
        stageAct = StageMessageAct("Stage 2"),
        durationSec = 3
    ),
    STAGE_TWO_MESSAGE_THREE(
        stageAct = StageMessageAct("GO!"),
        durationSec = 1
    ),
    STAGE_TWO_GAME_ONE(
        stageAct = StageGameAct(
            enemyAttributes = LevelOneEnemyAttributes(
                spawnPosition = EnemySpawnPosition.LEFT,
                xOffsetSpeed = 0.6f,
                yOffsetSpeed = 0.5f,
                enemySpawnRateMillis = RepeatTime.Millis(timeMillis = 1000),
                enemyFireRateMillis = RepeatTime.Millis(timeMillis = 500),
                enemyType = LevelOneEnemyType.TIER_TWO
            )
        ),
        durationSec = 10
    ),
    STAGE_THREE_BREAK_ONE(
        stageAct = StageBreakAct,
        durationSec = 3
    ),
    STAGE_THREE_MESSAGE_ONE(
        stageAct = StageMessageAct("Easy.."),
        durationSec = 3
    ),
    STAGE_THREE_MESSAGE_TWO(
        stageAct = StageMessageAct("Stage 3"),
        durationSec = 3
    ),
    STAGE_THREE_MESSAGE_THREE(
        stageAct = StageMessageAct("GO!"),
        durationSec = 1
    ),
    GAME_THREE(
        stageAct = StageGameAct(
            spaceRockSpawnRateMillis = RepeatTime.Millis(timeMillis = 1000),
            enemyAttributes = LevelOneEnemyAttributes(
                spawnPosition = EnemySpawnPosition.RIGHT,
                xOffsetSpeed = 0.7f,
                yOffsetSpeed = 0.5f,
                enemySpawnRateMillis = RepeatTime.Millis(timeMillis = 1000),
                enemyFireRateMillis = RepeatTime.Millis(timeMillis = 500),
                enemyType = LevelOneEnemyType.TIER_ONE
            )
        ),
        durationSec = 10
    )
}
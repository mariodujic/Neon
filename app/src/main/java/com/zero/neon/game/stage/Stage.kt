package com.zero.neon.game.stage

import com.zero.neon.game.enemy.ship.EnemySpawnAttributes
import com.zero.neon.game.enemy.ship.EnemySpawnPosition

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
            spaceRockSpawnRateMillis = 1000,
            enemySpawnRateMillis = 1000,
            enemyEnemySpawnAttributes = null,
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
            spaceRockSpawnRateMillis = 0,
            enemySpawnRateMillis = 1000,
            enemyEnemySpawnAttributes = EnemySpawnAttributes(
                spawnPosition = EnemySpawnPosition.LEFT,
                xOffsetSpeed = 0.6f,
                yOffsetSpeed = 0.5f
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
            spaceRockSpawnRateMillis = 500,
            enemySpawnRateMillis = 500,
            enemyEnemySpawnAttributes = EnemySpawnAttributes(
                spawnPosition = EnemySpawnPosition.RIGHT,
                xOffsetSpeed = 0.7f,
                yOffsetSpeed = 0.5f
            )
        ),
        durationSec = 10
    )
}
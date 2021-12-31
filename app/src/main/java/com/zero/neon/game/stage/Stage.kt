package com.zero.neon.game.stage

import com.zero.neon.R
import com.zero.neon.game.common.RepeatTime
import com.zero.neon.game.enemy.ship.model.*
import java.io.Serializable

val stages = listOf(
    StageMessage(
        message = "Stage 1",
        durationMillis = 3
    ),
    StageMessage(
        message = "Get ready!",
        durationMillis = 3
    ),
    StageMessage(
        message = "GO!",
        durationMillis = 1
    ),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.None,
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_one,
            width = 30f,
            height = 40f,
            hp = 150f,
            impactPower = 50f,
            formation = Row(rowCount = 4),
            xOffsetSpeed = 0.8f,
            yOffsetSpeed = 0.8f,
            enemySpawnRate = RepeatTime.Millis(1000)
        ),
        3
    ),
    StageBreak(3),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.None,
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_one,
            width = 30f,
            height = 40f,
            hp = 150f,
            impactPower = 50f,
            formation = Row(rowCount = 3),
            xOffsetSpeed = 0.8f,
            yOffsetSpeed = 0.8f,
            enemySpawnRate = RepeatTime.Millis(1000)
        ),
        4
    ),
    StageBreak(3),
    StageMessage(
        message = "Stage 2",
        durationMillis = 3
    ),
    StageMessage(
        message = "Get ready!",
        durationMillis = 3
    ),
    StageMessage(
        message = "GO!",
        durationMillis = 1
    ),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.None,
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_two,
            width = 40f,
            height = 40f,
            hp = 180f,
            impactPower = 60f,
            formation = ZigZag(position = ZigZagInitialPosition.RIGHT),
            xOffsetSpeed = 0.5f,
            yOffsetSpeed = 0.4f,
            enemySpawnRate = RepeatTime.Millis(1000)
        ),
        15
    ),
    StageBreak(3),
    StageMessage(
        message = "Stage 3",
        durationMillis = 3
    ),
    StageMessage(
        message = "Get ready!",
        durationMillis = 3
    ),
    StageMessage(
        message = "GO!",
        durationMillis = 1
    ),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.Millis(2000),
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_three,
            width = 45f,
            height = 45f,
            hp = 220f,
            impactPower = 65f,
            formation = ZigZag(position = ZigZagInitialPosition.LEFT),
            xOffsetSpeed = 0.6f,
            yOffsetSpeed = 0.4f,
            enemySpawnRate = RepeatTime.Millis(900)
        ),
        15
    ),
    StageBreak(3),
    StageMessage(
        message = "Boss fight",
        durationMillis = 3
    ),
    StageMessage(
        message = "Get ready!",
        durationMillis = 3
    ),
    StageMessage(
        message = "GO!",
        durationMillis = 1
    ),
    StageBoss(enemyType = LevelOneBossType),
    StageBreak(5),
    StageMessage(
        message = "Rekt",
        durationMillis = 3
    ),
    StageMessage(
        message = "Stage 5",
        durationMillis = 3
    ),
    StageMessage(
        message = "Get ready!",
        durationMillis = 3
    ),
    StageMessage(
        message = "GO!",
        durationMillis = 1
    ),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.None,
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_four,
            width = 35f,
            height = 40f,
            hp = 220f,
            impactPower = 65f,
            formation = ZigZag(position = ZigZagInitialPosition.LEFT),
            xOffsetSpeed = 0.5f,
            yOffsetSpeed = 0.7f,
            enemySpawnRate = RepeatTime.Millis(900)
        ),
        15
    ),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.None,
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_five,
            width = 40f,
            height = 40f,
            hp = 230f,
            impactPower = 70f,
            formation = ZigZag(position = ZigZagInitialPosition.RIGHT),
            xOffsetSpeed = 0.5f,
            yOffsetSpeed = 0.5f,
            enemySpawnRate = RepeatTime.Millis(800)
        ),
        15
    ),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.None,
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_six,
            width = 45f,
            height = 45f,
            hp = 240f,
            impactPower = 75f,
            formation = ZigZag(position = ZigZagInitialPosition.LEFT),
            xOffsetSpeed = 0.6f,
            yOffsetSpeed = 0.6f,
            enemySpawnRate = RepeatTime.Millis(800)
        ),
        15
    ),
    StageGame(
        spaceRockSpawnRateMillis = RepeatTime.None,
        enemyType = RegularEnemyType(
            drawableId = R.drawable.enemy_level_seven,
            width = 50f,
            height = 50f,
            hp = 250f,
            impactPower = 80f,
            formation = ZigZag(position = ZigZagInitialPosition.RIGHT),
            xOffsetSpeed = 0.6f,
            yOffsetSpeed = 0.6f,
            enemySpawnRate = RepeatTime.Millis(800)
        ),
        15
    ),
    StageBreak(3),
    StageMessage(
        message = "End",
        durationMillis = 3
    ),
)

sealed class Stage(val durationSec: Int) : Serializable

data class StageMessage(val message: String, val durationMillis: Int) : Stage(durationMillis)
data class StageGame(
    val spaceRockSpawnRateMillis: RepeatTime = RepeatTime.None,
    val enemyType: EnemyType,
    val durationTimeSec: Int
) : Stage(durationTimeSec)

data class StageBoss(val enemyType: EnemyType) : Stage(1)

data class StageBreak(val durationMillis: Int) : Stage(durationMillis)
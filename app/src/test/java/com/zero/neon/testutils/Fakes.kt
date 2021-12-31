package com.zero.neon.testutils

import com.zero.neon.R
import com.zero.neon.game.common.RepeatTime
import com.zero.neon.game.enemy.ship.model.*
import com.zero.neon.game.ship.ship.Ship
import java.util.*

const val FAKE_SCREEN_WIDTH = 500f
const val FAKE_SCREEN_HEIGHT = 550f
val FAKE_TIME_MILLIS = System.currentTimeMillis()
val FAKE_UUID: String = UUID.randomUUID().toString()
val FAKE_REGULAR_ENEMY_TYPE: RegularEnemyType = RegularEnemyType(
    drawableId = R.drawable.enemy_level_five,
    width = 50f,
    height = 50f,
    hp = 200f,
    impactPower = 50f,
    formation = ZigZag(
        position = ZigZagInitialPosition.LEFT
    ),
    xOffsetSpeed = 0.5f,
    yOffsetSpeed = 0.5f,
    enemySpawnRate = RepeatTime.Millis(1000)
)
val FAKE_ENEMY: RegularEnemy = RegularEnemy(
    screenWidth = FAKE_SCREEN_WIDTH,
    screenHeight = FAKE_SCREEN_HEIGHT,
    xOffset = 100f,
    type = FAKE_REGULAR_ENEMY_TYPE
)
val FAKE_ENEMIES: List<Enemy> = listOf(
    FAKE_ENEMY
)
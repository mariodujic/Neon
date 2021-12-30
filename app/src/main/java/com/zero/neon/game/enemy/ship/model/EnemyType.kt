package com.zero.neon.game.enemy.ship.model

import androidx.annotation.DrawableRes
import com.zero.neon.game.common.RepeatTime

sealed class EnemyType(val spawnRate: RepeatTime)

data class RegularEnemyType(
    @DrawableRes val drawableId: Int,
    val width: Float,
    val height: Float,
    val hp: Float,
    val impactPower: Float,
    val formation: EnemyFormation,
    val xOffsetSpeed: Float,
    val yOffsetSpeed: Float,
    val enemySpawnRate: RepeatTime
) : EnemyType(spawnRate = enemySpawnRate)

object LevelOneBossType : EnemyType(spawnRate = RepeatTime.Once)
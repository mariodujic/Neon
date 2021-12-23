package com.zero.neon.game.stage

import com.zero.neon.game.enemy.ship.EnemySpawnAttributes

sealed interface StagePartition

class StageMessagePartition(val message: String) : StagePartition
class StageGamePartition(
    val spaceRockSpawnRateMillis: Int,
    val enemySpawnRateMillis: Int,
    val enemyEnemySpawnAttributes: EnemySpawnAttributes?
) : StagePartition

object StageBreakPartition : StagePartition
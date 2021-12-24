package com.zero.neon.game.stage

import com.zero.neon.game.enemy.ship.EnemySpawnAttributes

sealed interface StageAct

class StageMessageAct(val message: String) : StageAct
class StageGameAct(
    val spaceRockSpawnRateMillis: Int,
    val enemySpawnRateMillis: Int,
    val enemyEnemySpawnAttributes: EnemySpawnAttributes?
) : StageAct

object StageBreakAct : StageAct
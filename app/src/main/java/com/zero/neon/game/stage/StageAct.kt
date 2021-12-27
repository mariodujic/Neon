package com.zero.neon.game.stage

import com.zero.neon.game.enemy.ship.EnemyAttributes

sealed interface StageAct

class StageMessageAct(val message: String) : StageAct
class StageGameAct(
    val spaceRockSpawnRateMillis: Int = 0,
    val enemyAttributes: EnemyAttributes? = null
) : StageAct

object StageBreakAct : StageAct
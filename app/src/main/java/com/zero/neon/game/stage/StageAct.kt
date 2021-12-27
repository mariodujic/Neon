package com.zero.neon.game.stage

import com.zero.neon.game.common.RepeatTime
import com.zero.neon.game.enemy.ship.EnemyAttributes

sealed interface StageAct

class StageMessageAct(val message: String) : StageAct
class StageGameAct(
    val spaceRockSpawnRateMillis: RepeatTime = RepeatTime.None,
    val enemyAttributes: EnemyAttributes? = null
) : StageAct

object StageBreakAct : StageAct
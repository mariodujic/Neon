package com.zero.neon.game.stage

import com.zero.neon.utils.DateUtils

class StageController(private val dateUtils: DateUtils = DateUtils()) {

    private var stageStartSnapshotMillis: Long = dateUtils.currentTimeMillis()
    private var stageIndex = 0

    fun getGameStage(readyForNextStage: Boolean = false): Stage {
        val stageTimeExpired =
            stageStartSnapshotMillis + getStage().durationSec * 1000 < dateUtils.currentTimeMillis()
        val hasNextStage = stageIndex < Stage.values().lastIndex
        if (
            stageTimeExpired &&
            (getStage().stageAct !is StageBreakAct || readyForNextStage) &&
            hasNextStage
        ) {
            stageIndex++
            stageStartSnapshotMillis = dateUtils.currentTimeMillis()
        }
        return getStage()
    }

    private fun getStage(): Stage {
        return Stage.values()[stageIndex]
    }
}
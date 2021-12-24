package com.zero.neon.game.stage

class StageController {

    private var stageStartSnapshotMillis: Long = System.currentTimeMillis()
    private var stageIndex = 0

    fun getGameStage(readyForNextStage: Boolean): Stage {
        val currentStage = Stage.values()[stageIndex]
        val stageTimeExpired =
            stageStartSnapshotMillis + currentStage.durationSec * 1000 < System.currentTimeMillis()
        val hasNextStage = stageIndex < Stage.values().lastIndex
        if (
            stageTimeExpired &&
            (currentStage.stageAct !is StageBreakAct || readyForNextStage) &&
            hasNextStage
        ) {
            stageIndex++
            stageStartSnapshotMillis = System.currentTimeMillis()
        }
        return Stage.values()[stageIndex]
    }
}
package com.zero.neon.game.stage

import androidx.compose.runtime.saveable.Saver
import com.zero.neon.utils.DateUtils

class StageController(
    private val dateUtils: DateUtils = DateUtils(),
    private var stageIndex: Int = 0,
    private var stageStartSnapshotMillis: Long = dateUtils.currentTimeMillis()
) {

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

    companion object {
        fun saver(): Saver<StageController, *> = Saver(
            save = {
                Pair(it.stageIndex, it.stageStartSnapshotMillis)
            },
            restore = {
                StageController(stageIndex = it.first, stageStartSnapshotMillis = it.second)
            }
        )
    }
}
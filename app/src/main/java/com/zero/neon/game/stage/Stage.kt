package com.zero.neon.game.stage

enum class Stage(
    val spaceRockSpawnRateMillis: Int,
    val enemySpawnRateMillis: Int,
    private val endTimeSec: Long
) {
    ONE(500, 0, 10),
    TWO(0, 1000, 20),
    THREE(1000, 1000, 30),
    FOUR(0, 800, 40),
    FIVE(1000, 750, 50);

    companion object {
        fun getCurrentGameStage(currentTimeSec: Long): Stage {
            return values()
                .sortedBy { it.endTimeSec }
                .firstOrNull { currentTimeSec < it.endTimeSec }
                ?: values().last()
        }
    }
}
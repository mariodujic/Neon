package com.zero.neon.game.enemy.ship

import androidx.compose.ui.unit.Dp

data class EnemySpawnAttributes(
    val spawnPosition: EnemySpawnPosition,
    val xOffsetSpeed: Dp,
    val yOffsetSpeed: Dp
)
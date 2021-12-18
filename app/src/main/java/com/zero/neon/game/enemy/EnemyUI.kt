package com.zero.neon.game.enemy

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
data class EnemyUI(
    val enemyId: String,
    val width: Dp,
    val height: Dp,
    val xOffset: Dp,
    val yOffset: Dp,
    val hp: Int
)
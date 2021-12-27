package com.zero.neon.game.enemy.ship

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class EnemyUI(
    val enemyId: String,
    val width: Float,
    val height: Float,
    val xOffset: Float,
    val yOffset: Float,
    val hpBarWidth: Float
) : Serializable
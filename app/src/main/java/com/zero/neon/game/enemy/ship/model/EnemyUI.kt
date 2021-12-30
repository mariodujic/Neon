package com.zero.neon.game.enemy.ship.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class EnemyUI(
    val enemyId: String,
    val width: Float,
    val height: Float,
    val xOffset: Float,
    val yOffset: Float,
    val hpBarWidth: Float,
    @DrawableRes val drawableId: Int
) : Serializable
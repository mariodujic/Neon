package com.zero.neon.game.ship.laser

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class LaserUI(
    val id: String,
    val xOffset: Float,
    val yOffset: Float,
    val width: Float,
    val height: Float,
    val rotation: Float,
    @DrawableRes val drawableId: Int
) : Serializable
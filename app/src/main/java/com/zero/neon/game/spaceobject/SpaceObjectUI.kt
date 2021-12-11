package com.zero.neon.game.spaceobject

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
data class SpaceObjectUI(
    val id: String,
    val size: Dp,
    val xOffset: Dp,
    val yOffset: Dp,
    val rotation: Float,
    @DrawableRes val drawableId: Int,
)
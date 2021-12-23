package com.zero.neon.game.booster

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

@Immutable
data class BoosterUI(
    val xOffset: Dp,
    val yOffset: Dp,
    val size: Dp,
    @DrawableRes val drawableId: Int
)
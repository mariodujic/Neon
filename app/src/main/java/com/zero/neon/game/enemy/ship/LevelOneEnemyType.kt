package com.zero.neon.game.enemy.ship

import androidx.annotation.DrawableRes
import com.zero.neon.R

enum class LevelOneEnemyType(
    @DrawableRes val drawableId: Int,
    val width: Float,
    val height: Float,
    val hp: Float,
    val impactPower: Float
) {
    TIER_ONE(
        drawableId = R.drawable.enemy_ship_tier_one,
        width = 52f,
        height = 52f,
        hp = 250f,
        impactPower = 250f
    ),
    TIER_TWO(
        drawableId = R.drawable.enemy_ship_tier_two,
        width = 40f,
        height = 40f,
        hp = 250f,
        impactPower = 320f
    )
}
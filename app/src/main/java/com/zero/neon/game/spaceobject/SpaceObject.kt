package com.zero.neon.game.spaceobject

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp

interface SpaceObject {
    val id: String
    var xOffset: Dp
    var yOffset: Dp
    var size: Dp
    val drawableId: Int
    val destroyable: Boolean
    var hp: Int
    val impactPower: Int
    var rotation: Float

    fun spaceObjectRect(): Rect
    fun moveObject()
    fun onObjectImpact(impactPower: Int)
}
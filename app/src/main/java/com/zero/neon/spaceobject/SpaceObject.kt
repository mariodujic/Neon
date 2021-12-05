package com.zero.neon.spaceobject

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.Dp

interface SpaceObject {
    val id: String
    var xOffset: Dp
    var yOffset: Dp
    var size: Dp
    val drawableId: Int
    var floating: Boolean
    val rect: Rect

    fun moveObject()
    fun destroyObject()
}
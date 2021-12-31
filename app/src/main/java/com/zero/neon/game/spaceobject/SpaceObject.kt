package com.zero.neon.game.spaceobject

import androidx.compose.ui.geometry.Rect
import java.io.Serializable

interface SpaceObject : Serializable {
    val id: String
    var xOffset: Float
    var yOffset: Float
    var size: Float
    val drawableId: Int
    val destroyable: Boolean
    var hp: Float
    val impactPower: Int
    var rotation: Float

    fun spaceObjectRect(): Rect
    fun moveObject()
    fun onObjectImpact(impactPower: Float)
}
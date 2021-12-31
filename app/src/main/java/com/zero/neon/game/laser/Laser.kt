package com.zero.neon.game.laser

interface Laser {
    val id: String
    var xOffset: Float
    var yOffset: Float
    var width: Float
    var height: Float
    var rotation: Float
    var impactPower: Float
    val drawableId: Int
    val xOffsetMovementSpeed: Float
    val yOffsetMovementSpeed: Float
    var destroyed: Boolean

    fun moveLaser()
}
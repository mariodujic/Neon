package com.zero.neon.game.laser

interface Laser {
    val id: String
    var xOffset: Float
    var yOffset: Float
    var width: Float
    var height: Float
    var rotation: Float
    var impactPower: Int
    val drawableId: Int

    fun destroyLaser()
    fun moveLaser()
}
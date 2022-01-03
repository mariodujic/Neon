package com.zero.neon.game.spaceobject

import com.zero.neon.game.common.Millis
import java.util.*
import kotlin.random.Random

class SpaceObjectsController(
    private val screenWidth: Float,
    private val screenHeight: Float,
    initialSpaceObjects: List<SpaceObject>,
    private val setSpaceObjects: (List<SpaceObject>) -> Unit
) {

    var spaceObjects: List<SpaceObject> = initialSpaceObjects
        private set

    private val minRockSize = 20
    private val maxRockSize = 80

    val addSpaceRockId = UUID.randomUUID().toString()
    fun addSpaceRock() {
        val rockSize = Random.nextInt(minRockSize, maxRockSize)
        val rockXOffset = Random.nextInt(rockSize, screenWidth.toInt() - rockSize).toFloat()
        val spaceRock = SpaceRock(
            xOffset = rockXOffset,
            size = rockSize.toFloat(),
            screenHeight = screenHeight
        )
        spaceObjects = spaceObjects.toMutableList().apply { add(spaceRock) }
        updateSpaceObjectsUI()
    }

    val processSpaceObjectsId = UUID.randomUUID().toString()
    val processSpaceObjectsRepeatTime = Millis(5)
    fun processSpaceObjects() {
        spaceObjects.forEach { it.moveObject() }
        spaceObjects = spaceObjects.toMutableList().apply { removeAll { it.hp <= 0 } }
        updateSpaceObjectsUI()
    }

    fun hasSpaceObjects() = spaceObjects.isNotEmpty()

    private fun updateSpaceObjectsUI() {
        setSpaceObjects(spaceObjects)
    }
}
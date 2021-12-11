package com.zero.neon.game.spaceobject

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.random.Random

class SpaceObjectsManager(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val setSpaceObjectsUi: (List<SpaceObjectUI>) -> Unit
) {

    var spaceObjects: List<SpaceObject> = emptyList()
        private set
    private val mapper = SpaceObjectToSpaceObjectUIMapper()

    private val minRockSize = 20
    private val maxRockSize = 80
    private val boosterSize = 40

    val addSpaceRockId = UUID.randomUUID().toString()
    fun addSpaceRock() {
        val rockSize = Random.nextInt(minRockSize, maxRockSize)
        val rockXOffset = Random.nextInt(rockSize, screenWidthDp.value.toInt() - rockSize).dp
        val spaceRock = SpaceRock(
            xOffset = rockXOffset,
            size = rockSize.dp,
            screenHeight = screenHeightDp,
            onDestroyRock = { destroySpaceObject(it) }
        )
        spaceObjects = spaceObjects.toMutableList().apply { add(spaceRock) }
        updateSpaceObjectsUI()
    }

    val addBoosterId = UUID.randomUUID().toString()
    fun addBooster() {
        val boosterXOffset =
            Random.nextInt(boosterSize, screenWidthDp.value.toInt() - boosterSize).dp
        val booster = Booster(
            xOffset = boosterXOffset,
            size = boosterSize.dp,
            screenHeight = screenHeightDp,
            onDestroyBooster = { destroySpaceObject(it) }
        )
        spaceObjects = spaceObjects.toMutableList().apply { add(booster) }
        updateSpaceObjectsUI()
    }

    private fun destroySpaceObject(rockId: String) {
        spaceObjects = spaceObjects.toMutableList().apply { removeAll { it.id == rockId } }
        updateSpaceObjectsUI()
    }

    val moveSpaceObjectsId = UUID.randomUUID().toString()
    fun moveSpaceObjects() {
        spaceObjects.forEach { it.moveObject() }
        updateSpaceObjectsUI()
    }

    private fun updateSpaceObjectsUI() {
        setSpaceObjectsUi(spaceObjects.map { mapper(it) })
    }
}
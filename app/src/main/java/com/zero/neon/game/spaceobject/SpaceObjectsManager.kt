package com.zero.neon.game.spaceobject

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.random.Random

class SpaceObjectsManager(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val spaceObjects: () -> List<SpaceObject>,
    private val setSpaceObject: (List<SpaceObject>) -> Unit
) {

    private val minRockSize = 20
    private val maxRockSize = 80

    val spaceRockId = UUID.randomUUID().toString()
    fun addSpaceRock() {
        val rockSize = Random.nextInt(minRockSize, maxRockSize)
        val rockXOffset = Random.nextInt(rockSize, screenWidthDp.value.toInt() - rockSize).dp
        setSpaceObject(
            spaceObjects()
                .filter { it.floating }
                .toMutableList()
                .apply {
                    val spaceRock = SpaceRock(
                        xOffset = rockXOffset,
                        size = rockSize.dp,
                        screenHeight = screenHeightDp,
                        onDestroyRock = { destroySpaceObject(it) }
                    )
                    add(spaceRock)
                }
        )
    }

    val addBoosterId = UUID.randomUUID().toString()
    fun addBooster() {
        val size = 45
        val boosterXOffset = Random.nextInt(size, screenWidthDp.value.toInt() - size).dp
        setSpaceObject(
            spaceObjects()
                .filter { it.floating }
                .toMutableList()
                .apply {
                    val booster = Booster(
                        xOffset = boosterXOffset,
                        size = size.dp,
                        screenHeight = screenHeightDp,
                        onDestroyBooster = { destroySpaceObject(it) }
                    )
                    add(booster)
                }
        )
    }

    private fun destroySpaceObject(rockId: String) {
        setSpaceObject(
            spaceObjects().toMutableList().apply {
                removeAll { it.id == rockId }
            }
        )
    }

    val moveSpaceObjectsId = UUID.randomUUID().toString()
    fun moveSpaceObjects() {
        spaceObjects().forEach { it.moveObject() }
    }
}
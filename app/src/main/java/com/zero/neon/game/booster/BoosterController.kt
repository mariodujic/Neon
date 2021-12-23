package com.zero.neon.game.booster

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.random.Random

class BoosterController(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val updateBoosters: (List<BoosterUI>) -> Unit
) {

    var boosters: List<Booster> = emptyList()
        private set

    private val mapper = BoosterToBoosterUIMapper()
    private val boosterSize = 40

    val addBoosterId = UUID.randomUUID().toString()
    fun addBooster() {
        val boosterXOffset =
            Random.nextInt(boosterSize, screenWidthDp.value.toInt() - boosterSize).dp
        val booster = Booster(
            xOffset = boosterXOffset,
            size = boosterSize.dp,
            screenHeight = screenHeightDp,
            onDestroyBooster = { destroyBooster(it) }
        )
        boosters = boosters.toMutableList().apply { add(booster) }
        updateBoosters()
    }

    val moveBoostersId = UUID.randomUUID().toString()
    fun moveBoosters() {
        boosters.forEach { it.moveObject() }
        updateBoosters()
    }

    private fun destroyBooster(boosterId: String) {
        boosters = boosters.toMutableList().apply { removeAll { it.id == boosterId } }
        updateBoosters()
    }

    private fun updateBoosters() {
        updateBoosters(boosters.map { mapper(it) })
    }
}
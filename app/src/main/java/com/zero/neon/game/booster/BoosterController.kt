package com.zero.neon.game.booster

import com.zero.neon.utils.UuidUtils
import java.util.*
import kotlin.random.Random

class BoosterController(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    private val uuidUtils: UuidUtils = UuidUtils(),
    initialBoosters: List<Booster>,
    private val updateBoosters: (List<Booster>) -> Unit
) {

    var boosters: List<Booster> = initialBoosters
        private set

    private val boosterSize = 40

    val addBoosterId = UUID.randomUUID().toString()
    fun addBooster() {
        val boosterXOffset = Random.nextInt(boosterSize, screenWidthDp.toInt() - boosterSize)
        val booster = Booster(
            id = uuidUtils.getUuid(),
            xOffset = boosterXOffset.toFloat(),
            size = boosterSize.toFloat(),
            screenHeight = screenHeightDp
        )
        boosters = boosters.toMutableList().apply { add(booster) }
        updateBoosters()
    }

    val processBoostersId = UUID.randomUUID().toString()
    fun processBoosters() {
        boosters.forEach { it.moveObject() }
        boosters = boosters.toMutableList().apply { removeAll { it.collected } }
        updateBoosters()
    }

    fun hasBoosters() = boosters.isNotEmpty()

    private fun updateBoosters() {
        updateBoosters(boosters)
    }
}
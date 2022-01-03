package com.zero.neon.game.booster

import com.zero.neon.game.common.Millis
import com.zero.neon.utils.UuidUtils

class BoosterController(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val uuidUtils: UuidUtils,
    private val generateBooster: GenerateBooster = GenerateBooster(uuidUtils, screenHeight),
    initialBoosters: List<Booster>,
    private val updateBoosters: (List<Booster>) -> Unit
) {

    var boosters: List<Booster> = initialBoosters
        private set

    val addBoosterId = uuidUtils.getUuid()
    val addBoosterRepeatTime = Millis(4000)
    fun addBooster() {
        val booster = generateBooster(width = BOOSTER_SIZE, maxXOffset = screenWidth - BOOSTER_SIZE)
        boosters += booster
        updateBoosters()
    }

    val processBoostersId = uuidUtils.getUuid()
    val processBoostersRepeatTime = Millis(5)
    fun processBoosters() {
        boosters.forEach {
            if (it.collected) boosters -= it
            it.moveObject()
        }
        updateBoosters()
    }

    fun hasBoosters() = boosters.isNotEmpty()

    private fun updateBoosters() {
        updateBoosters(boosters)
    }

    companion object {
        const val BOOSTER_SIZE = 40f
    }
}
package com.zero.neon.game.booster

import com.zero.neon.utils.UuidUtils
import kotlin.random.Random

class GenerateBooster(private val uuidUtils: UuidUtils, private val screenHeight: Float) {

    operator fun invoke(width: Float, maxXOffset: Float): Booster {
        val boosterXOffset = Random.nextInt(width.toInt(), maxXOffset.toInt())
        return Booster(
            id = uuidUtils.getUuid(),
            xOffset = boosterXOffset.toFloat(),
            size = width,
            screenHeight = screenHeight
        )
    }
}
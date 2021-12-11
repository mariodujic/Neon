package com.zero.neon.game.constellation

import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class ConstellationController(
    private val stars: () -> List<Star>,
    private val setStars: (List<Star>) -> Unit
) {

    fun createStars(screenWidth: Int, screenHeight: Int, coroutineScope: CoroutineScope) {
        coroutineScope.launch(Dispatchers.IO) {
            val starList = mutableListOf<Star>()
            for (i in 0..30) {
                val starXOffset = Random.nextInt(0, screenWidth).dp
                val starYOffset = Random.nextInt(0, screenHeight).dp
                val starSize = Random.nextInt(1, 12).dp
                val star = Star(
                    xOffset = starXOffset,
                    yOffset = starYOffset,
                    maxYOffset = screenHeight.dp,
                    size = starSize
                )
                starList.add(star)
            }
            setStars(starList.toList())
        }
    }

    val animateStarsId = UUID.randomUUID().toString()
    fun animateStars() {
        stars().forEach { it.animateStar() }
    }
}
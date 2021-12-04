package com.zero.neon

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun rememberGameState(): GameState {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val coroutineScope = rememberCoroutineScope()
    return remember { GameState(screenWidthDp, screenHeightDp, coroutineScope) }
}

class GameState(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val coroutineScope: CoroutineScope
) {

    /**
     * Main loop
     */
    private val loopRefreshRate = 5L

    init {
        coroutineScope.launch {
            createStars()
            while (true) {
                moveShip()
                delay(loopRefreshRate)
            }
        }
        coroutineScope.launch {
            while (true) {
                fireLasers()
                delay(laserFireSpeedRate)
            }
        }
    }

    /**
     * Ship movement
     */
    private val shipMaxXOffset = screenWidthDp / 2
    private val shipXMovementSpeed = 2.dp

    private fun moveShip() {
        if (movingLeft && shipOffsetX > -shipMaxXOffset) shipOffsetX -= shipXMovementSpeed
        else movingLeft = false
        if (movingRight && shipOffsetX < shipMaxXOffset) shipOffsetX += shipXMovementSpeed
        else movingRight = false
    }

    var shipOffsetX by mutableStateOf(0.dp)
    val shipYRotation by derivedStateOf { shipOffsetX.value * 0.3f }

    private var movingLeft by mutableStateOf(false)
    private var movingRight by mutableStateOf(false)

    fun moveShipLeft(movingShipLeft: Boolean) {
        movingLeft = movingShipLeft
    }

    fun moveShipRight(movingShipRight: Boolean) {
        movingRight = movingShipRight
    }

    /**
     * Ship laser
     */
    var lasers by mutableStateOf<List<ShipLaser>>(listOf())
    private var laserFireSpeedRate: Long = 150

    private fun fireLasers() {
        lasers = lasers
            .filter { it.shooting }
            .toMutableList()
            .apply { add(ShipLaser(shipOffsetX, screenHeightDp, coroutineScope)) }
    }

    inner class ShipLaser(
        override var xOffset: Dp,
        yRange: Dp,
        coroutineScope: CoroutineScope
    ) : Laser {

        override var yOffset: Dp by mutableStateOf((-10).dp)
        override var shooting: Boolean by mutableStateOf(false)

        init {
            coroutineScope.launch {
                shooting = true
                while (yOffset > -yRange || !shooting) {
                    yOffset -= 7.dp
                    delay(loopRefreshRate)
                }
                shooting = false
            }
        }

        override fun destroyLaser() {
            shooting = false
        }
    }

    interface Laser {
        var xOffset: Dp
        var yOffset: Dp
        var shooting: Boolean

        fun destroyLaser()
    }

    /**
     * Constellation
     */
    var stars by mutableStateOf<List<Star>>(listOf())

    private fun createStars() {
        val starList = mutableListOf<Star>()
        for (i in 0..40) {
            val starXOffset = Random.nextInt(0, screenWidthDp.value.toInt()).dp
            val starYOffset = Random.nextInt(0, screenHeightDp.value.toInt()).dp
            val starSize = Random.nextInt(1, 12).dp
            val star = Star(starXOffset, starYOffset, starSize, coroutineScope)
            starList.add(star)
        }
        stars = starList.toList()
    }

    class Star(
        override var xOffset: Dp,
        override var yOffset: Dp,
        override var size: Dp,
        coroutineScope: CoroutineScope
    ) : SpaceObject {

        private val initialStarSize = size
        private var enlargeStar = false

        init {
            coroutineScope.launch {
                while (true) {
                    if (enlargeStar) {
                        if (size == initialStarSize) {
                            enlargeStar = false
                        } else {
                            size += 1.dp
                        }
                    } else {
                        if (size == 1.dp) {
                            enlargeStar = true
                        } else {
                            size -= 1.dp
                        }
                    }
                    delay(150)
                }
            }
        }
    }

    interface SpaceObject {
        var xOffset: Dp
        var yOffset: Dp
        var size: Dp
    }
}
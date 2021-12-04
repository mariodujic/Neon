package com.zero.neon

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.constellation.Star
import com.zero.neon.ship.ShipLaser
import com.zero.neon.spaceobject.Rock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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
            createStars(
                screenHeight = screenHeightDp.value.toInt(),
                screenWidth = screenWidthDp.value.toInt(),
                coroutineScope = coroutineScope
            )
            animateStars(coroutineScope)
            while (true) {
                moveShip()
                monitorLaserSpaceObjectsHit()
                delay(loopRefreshRate)
            }
        }
        coroutineScope.launch {
            while (true) {
                fireLasers()
                delay(laserFireSpeedRate)
            }
        }
        coroutineScope.launch {
            while (true) {
                createRock()
                delay(rockSpawnRateMillis)
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
        private set
    private val laserFireSpeedRate: Long = 550

    private fun fireLasers() {
        lasers = lasers
            .filter { it.shooting }
            .toMutableList()
            .apply {
                val laser = ShipLaser(
                    xOffset = shipOffsetX,
                    yRange = screenHeightDp,
                    screenWidth = screenWidthDp,
                    screenHeight = screenHeightDp,
                    onDestroyLaser = { destroyLaser(it) },
                    coroutineScope = coroutineScope
                )
                add(laser)
            }
    }

    private fun destroyLaser(laserId: String) {
        lasers = lasers
            .toMutableList()
            .apply {
                removeAll { it.id == laserId }
            }
    }

    private fun monitorLaserSpaceObjectsHit() {
        rocks.map { it.rockRect }.forEachIndexed { rockIndex, rockRect ->
            lasers.map { it.offset }.forEachIndexed { laserIndex, offset ->
                if (rockRect.contains(offset)) {
                    rocks[rockIndex].destroyRock()
                    lasers[laserIndex].destroyLaser()
                }
            }
        }
    }

    /**
     * Constellation
     */
    var stars by mutableStateOf<List<Star>>(listOf())
        private set

    private fun createStars(screenWidth: Int, screenHeight: Int, coroutineScope: CoroutineScope) {
        coroutineScope.launch(IO) {
            val starList = mutableListOf<Star>()
            for (i in 0..30) {
                val starXOffset = Random.nextInt(0, screenWidth).dp
                val starYOffset = Random.nextInt(0, screenHeight).dp
                val starSize = Random.nextInt(1, 12).dp
                val star = Star(xOffset = starXOffset, yOffset = starYOffset, size = starSize)
                starList.add(star)
            }
            stars = starList.toList()
        }
    }

    private fun animateStars(coroutineScope: CoroutineScope) {
        coroutineScope.launch(IO) {
            while (true) {
                stars.forEach { it.animateStar() }
                delay(50)
            }
        }
    }

    /**
     * Space objects
     */
    var rocks by mutableStateOf<List<Rock>>(listOf())
        private set
    private val maxRockSpawnRateMillis: Long = 500
    private var rockSpawnRateMillis: Long = 4000

    private fun createRock() {
        val rockXOffset = Random.nextInt(0, screenWidthDp.value.toInt()).dp
        rocks = rocks
            .filter { it.floating }
            .toMutableList()
            .apply {
                val rock = Rock(
                    xOffset = rockXOffset,
                    size = 50.dp,
                    screenHeight = screenHeightDp,
                    coroutineScope = coroutineScope,
                    onDestroyRock = { destroyRock(it) }
                )
                add(rock)
            }
        if (rockSpawnRateMillis > maxRockSpawnRateMillis) {
            rockSpawnRateMillis = (rockSpawnRateMillis * 0.9).toLong()
        }
    }

    private fun destroyRock(rockId: String) {
        rocks = rocks
            .toMutableList()
            .apply { removeAll { it.id == rockId } }
    }
}
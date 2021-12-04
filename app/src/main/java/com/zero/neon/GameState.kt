package com.zero.neon

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun rememberGameState(): GameState {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val coroutineScope = rememberCoroutineScope()
    return remember { GameState(screenWidthDp, screenHeightDp, coroutineScope) }
}

class GameState(
    screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val coroutineScope: CoroutineScope
) {

    /**
     * Main loop
     */
    private val loopRefreshRate = 5L

    init {
        coroutineScope.launch {
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
        if (movingRight && shipOffsetX < shipMaxXOffset) shipOffsetX += shipXMovementSpeed
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

        override var yOffset: Dp by mutableStateOf(0.dp)
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
}
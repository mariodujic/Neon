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
    val coroutineScope = rememberCoroutineScope()
    return remember { GameState(screenWidthDp, coroutineScope) }
}

class GameState(screenWidthDp: Dp, coroutineScope: CoroutineScope) {

    /**
     * Main loop
     */
    init {
        coroutineScope.launch {
            while (true) {
                if (movingLeft && shipOffsetX > -maxXMovement) shipOffsetX -= 2.dp
                if (movingRight && shipOffsetX < maxXMovement) shipOffsetX += 2.dp
                delay(5)
            }
        }
    }

    /**
     * Ship movement
     */
    private val maxXMovement = screenWidthDp / 2

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
}
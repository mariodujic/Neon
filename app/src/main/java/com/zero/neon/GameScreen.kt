package com.zero.neon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.zero.neon.ui.theme.Blue
import com.zero.neon.ui.theme.Pink

@Composable
fun GameScreen() {

    val gameState = rememberGameState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Blue, Pink)
                )
            )
    ) {
        GameWorld(
            shipXOffset = gameState.shipOffsetX,
            shipYRotation = gameState.shipYRotation,
            shipLaser = gameState.lasers,
            stars = gameState.stars,
            spaceRocks = gameState.spaceRocks,
            modifier = Modifier.weight(1f)
        )
        GameControls(
            onMoveLeft = { gameState.moveShipLeft(it) },
            onMoveRight = { gameState.moveShipRight(it) },
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}
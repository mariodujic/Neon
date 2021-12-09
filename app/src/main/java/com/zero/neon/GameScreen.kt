package com.zero.neon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zero.neon.controls.HpIndicator
import com.zero.neon.controls.MovementButtons
import com.zero.neon.controls.SettingsButton
import com.zero.neon.ui.theme.Blue
import com.zero.neon.ui.theme.Pink

@Composable
fun GameScreen() {

    val gameState = rememberGameState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Blue, Pink)))
    ) {
        HpIndicator(
            hp = 5, modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(300f)
        )
        SettingsButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(300f)
        ) { gameState.toggleGamePause() }
        Column(modifier = Modifier.fillMaxSize()) {
            GameWorld(
                shipSize = gameState.shipSize,
                shipXOffset = gameState.shipXOffset,
                shipYOffset = gameState.shipYOffset,
                shipYRotation = gameState.shipYRotation,
                shipLasers = gameState.lasers,
                stars = gameState.stars,
                spaceObjects = gameState.spaceObjects,
                modifier = Modifier.weight(1f)
            )
            MovementButtons(
                onMoveLeft = { gameState.moveShipLeft(it) },
                onMoveRight = { gameState.moveShipRight(it) },
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}
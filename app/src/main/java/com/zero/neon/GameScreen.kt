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
import com.zero.neon.game.controls.HpIndicator
import com.zero.neon.game.controls.MovementButtons
import com.zero.neon.game.controls.SettingsButton
import com.zero.neon.game.state.rememberGameState
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
        gameState.refreshHandler
        HpIndicator(
            hp = gameState.ship.hp,
            modifier = Modifier
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
                ship = gameState.ship,
                shipLasers = gameState.shipLasers,
                ultimateLasers = gameState.ultimateLasers,
                stars = gameState.stars,
                spaceObjects = gameState.spaceObjects,
                modifier = Modifier.weight(1f)
            )
            MovementButtons(
                onMoveLeft = { gameState.ship.moveLeft(it) },
                onMoveRight = { gameState.ship.moveRight(it) },
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }
}
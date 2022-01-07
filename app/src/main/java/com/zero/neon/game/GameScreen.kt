package com.zero.neon.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.zero.neon.common.theme.Blue
import com.zero.neon.common.theme.Pink
import com.zero.neon.game.audio.AudioPlayer
import com.zero.neon.game.controls.MovementButtons
import com.zero.neon.game.controls.SettingsButton
import com.zero.neon.game.controls.StatusIndicator
import com.zero.neon.game.state.rememberGameState
import com.zero.neon.game.world.GameWorld

@Composable
fun GameScreen(onGamePause: () -> Unit) {

    val gameState = rememberGameState()

    AudioPlayer(gameStatus = gameState.gameStatus)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Blue, Pink)))
    ) {
        StatusIndicator(
            gameTime = gameState.gameTimeIndicator,
            hp = gameState.ship.hp,
            mineralsEarnedTotal = gameState.mineralsEarnedTotal,
            modifier = Modifier
                .align(Alignment.TopStart)
                .zIndex(300f)
        )
        SettingsButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .zIndex(300f)
        ) {
            gameState.toggleGameStatus()
            onGamePause()
        }
        Text(
            text = gameState.gameMessage,
            modifier = Modifier.align(Alignment.Center),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.h3
        )
        Column(modifier = Modifier.fillMaxSize()) {
            GameWorld(
                ship = gameState.ship,
                shipLasers = gameState.shipLasers,
                ultimateLasers = gameState.ultimateLasers,
                stars = gameState.stars,
                spaceObjects = gameState.spaceObjects,
                boosters = gameState.boosters,
                enemies = gameState.enemies,
                enemyLasers = gameState.enemyLasers,
                minerals = gameState.minerals,
                explosions = gameState.explosions,
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
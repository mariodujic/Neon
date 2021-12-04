package com.zero.neon

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GameWorld(
    modifier: Modifier = Modifier,
    shipXOffset: Dp = 0.dp,
    shipYRotation: Float = 0f,
    shipLaser: List<GameState.ShipLaser>,
    stars: List<GameState.Star>
) {

    val rotation by animateFloatAsState(targetValue = shipYRotation)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        shipLaser.forEach { shipLaser ->
            Image(
                painterResource(id = R.drawable.laser_blue_7),
                contentScale = ContentScale.None,
                contentDescription = "",
                modifier = Modifier
                    .offset(x = shipLaser.xOffset, y = shipLaser.yOffset)
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        rotationY = rotation
                    }
            )
        }
        stars.forEach {
            Canvas(
                modifier = Modifier
                    .size(size = it.size)
                    .offset(x = it.xOffset, y = it.yOffset),
                onDraw = {
                    val colors = listOf(Color.White, Color.Transparent)
                    drawCircle(
                        radius = it.size.value,
                        brush = Brush.radialGradient(colors),
                        blendMode = BlendMode.Luminosity
                    )
                })
        }
        Image(
            painterResource(id = R.drawable.ship_blue),
            contentDescription = stringResource(id = R.string.ship),
            modifier = Modifier
                .width(120.dp)
                .offset(x = shipXOffset)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    rotationY = rotation
                }
        )
    }
}
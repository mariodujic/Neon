package com.zero.neon

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
    shipLaser: List<GameState.ShipLaser>
) {

    val rotation by animateFloatAsState(targetValue = shipYRotation)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        shipLaser.forEach { shipLaser ->
            Image(
                painterResource(id = R.drawable.laser_blue_7),
                contentScale = ContentScale.None,
                contentDescription = "",
                modifier = Modifier
                    .scale(0.9f)
                    .offset(x = shipLaser.xOffset, y = shipLaser.yOffset)
                    .align(Alignment.BottomCenter)
                    .graphicsLayer {
                        rotationY = rotation
                    }
            )
        }
        Image(
            painterResource(id = R.mipmap.ship),
            contentScale = ContentScale.None,
            contentDescription = stringResource(id = R.string.ship),
            modifier = Modifier
                .scale(0.9f)
                .offset(x = shipXOffset)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    rotationY = rotation
                }
        )
    }
}
package com.zero.neon

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.constellation.Star
import com.zero.neon.ship.Laser
import com.zero.neon.spaceobject.SpaceObject

@Composable
fun GameWorld(
    shipXOffset: Dp = 0.dp,
    shipYRotation: Float = 0f,
    shipLaser: List<Laser>,
    stars: List<Star>,
    spaceObjects: List<SpaceObject>,
    modifier: Modifier = Modifier
) {

    val rotation by animateFloatAsState(targetValue = shipYRotation)

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        shipLaser.forEach {
            Image(
                painterResource(id = R.drawable.laser_blue_7),
                contentScale = ContentScale.None,
                contentDescription = "",
                modifier = Modifier
                    .offset(x = it.xOffset, y = it.yOffset)
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
        spaceObjects.forEach {
            Image(
                painterResource(id = it.drawableId),
                contentDescription = "",
                modifier = Modifier
                    .size(it.size)
                    .offset(x = it.xOffset, y = it.yOffset)
            )
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
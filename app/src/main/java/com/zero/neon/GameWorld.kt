package com.zero.neon

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.zero.neon.game.constellation.Star
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.ship.weapons.Laser
import com.zero.neon.game.spaceobject.SpaceObject

@Composable
fun GameWorld(
    ship: Ship,
    shipLasers: List<Laser>,
    ultimateLasers: List<Laser>,
    stars: List<Star>,
    spaceObjects: List<SpaceObject>,
    modifier: Modifier = Modifier
) {

    val infiniteRotation = rememberInfiniteTransition()
    val rotationAngle by infiniteRotation.animateFloat(
        initialValue = 0F,
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        shipLasers.forEach {
            Image(
                painterResource(id = R.drawable.laser_blue_7),
                contentDescription = stringResource(id = R.string.laser),
                modifier = Modifier
                    .absoluteOffset(x = it.xOffset, y = it.yOffset)
                    .size(width = it.width, height = it.height)
                    .align(Alignment.BottomStart)
            )
        }
        ultimateLasers.forEach {
            Image(
                painterResource(id = R.drawable.laser_blue_11),
                contentDescription = stringResource(id = R.string.laser),
                modifier = Modifier
                    .absoluteOffset(x = it.xOffset, y = it.yOffset)
                    .size(width = it.width, height = it.height)
                    .align(Alignment.BottomStart)
                    .rotate(rotationAngle)
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
                contentDescription = stringResource(id = R.string.ship),
                modifier = Modifier
                    .size(it.size)
                    .offset(x = it.xOffset, y = it.yOffset)
            )
        }
        Image(
            painterResource(id = R.drawable.ship_blue),
            contentDescription = stringResource(id = R.string.ship),
            modifier = Modifier
                .width(ship.size)
                .offset(x = ship.xOffset, y = ship.yOffset)
        )
    }
}
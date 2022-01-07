package com.zero.neon.game.world

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.zero.neon.R
import com.zero.neon.common.theme.ShipShieldOne
import com.zero.neon.common.theme.ShipShieldTwo
import com.zero.neon.game.booster.BoosterUI
import com.zero.neon.game.constellation.Star
import com.zero.neon.game.enemy.ship.model.EnemyUI
import com.zero.neon.game.explosion.model.Explosion
import com.zero.neon.game.mineral.model.MineralUI
import com.zero.neon.game.ship.laser.LaserUI
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.spaceobject.SpaceObjectUI
import com.zero.neon.game.utils.rememberImageLoader

@Composable
fun GameWorld(
    ship: Ship,
    shipLasers: List<LaserUI>,
    ultimateLasers: List<LaserUI>,
    stars: List<Star>,
    spaceObjects: List<SpaceObjectUI>,
    boosters: List<BoosterUI>,
    enemies: List<EnemyUI>,
    enemyLasers: List<LaserUI>,
    minerals: List<MineralUI>,
    explosions: List<Explosion>,
    modifier: Modifier = Modifier
) {

    val imageLoader = rememberImageLoader()

    val infiniteTransition = rememberInfiniteTransition()
    val shipShieldColor by infiniteTransition.animateColor(
        initialValue = ShipShieldOne,
        targetValue = ShipShieldTwo,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        shipLasers.forEach {
            Image(
                painterResource(id = it.drawableId),
                contentDescription = stringResource(id = R.string.laser),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .absoluteOffset(x = it.xOffset.dp, y = it.yOffset.dp)
                    .size(width = it.width.dp, height = it.height.dp)
                    .align(Alignment.BottomStart)
            )
        }
        ultimateLasers.forEach {
            Image(
                painterResource(id = it.drawableId),
                contentDescription = stringResource(id = R.string.laser),
                modifier = Modifier
                    .absoluteOffset(x = it.xOffset.dp, y = it.yOffset.dp)
                    .size(width = it.width.dp, height = it.height.dp)
                    .align(Alignment.BottomStart)
                    .rotate(degrees = it.rotation)
            )
        }
        stars.forEach {
            Canvas(
                modifier = Modifier
                    .size(size = it.size.dp)
                    .offset(x = it.xOffset.dp, y = it.yOffset.dp),
                onDraw = {
                    val colors = listOf(Color.White, Color.Transparent)
                    drawCircle(
                        radius = it.size,
                        brush = Brush.radialGradient(colors),
                        blendMode = BlendMode.Luminosity
                    )
                }
            )
        }
        spaceObjects.forEach {
            Image(
                painterResource(id = it.drawableId),
                contentDescription = stringResource(id = R.string.space_object),
                modifier = Modifier
                    .size(it.size.dp)
                    .offset(x = it.xOffset.dp, y = it.yOffset.dp)
                    .rotate(degrees = it.rotation)
            )
        }
        boosters.forEach {
            Image(
                painterResource(id = it.drawableId),
                contentDescription = stringResource(id = R.string.booster),
                modifier = Modifier
                    .size(it.size.dp)
                    .offset(x = it.xOffset.dp, y = it.yOffset.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(ship.shieldSize.dp)
                .offset(x = ship.xOffset.dp, y = ship.yOffset.dp)
        ) {
            if (ship.shieldEnabled) {
                Canvas(
                    modifier = Modifier
                        .size(size = ship.width.dp)
                        .offset(y = (ship.height - ship.width).dp / 2),
                    onDraw = {
                        val colors =
                            listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Transparent,
                                shipShieldColor
                            )
                        drawCircle(
                            radius = ship.shieldSize,
                            brush = Brush.radialGradient(
                                colors = colors,
                                radius = ship.height * 2.5f
                            ),
                            blendMode = BlendMode.Hardlight
                        )
                    }
                )
            }
            Image(
                painterResource(id = ship.drawableId),
                contentDescription = stringResource(id = R.string.ship),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(ship.width.dp)
                    .height(ship.height.dp)
            )
        }
        enemies.forEach {
            Column(modifier = Modifier.offset(x = it.xOffset.dp, y = it.yOffset.dp)) {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .size(width = it.width.dp, height = 5.dp)
                        .background(Color.White.copy(alpha = 0.7f))
                ) {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .size(width = it.hpBarWidth.dp, height = 5.dp)
                            .background(Color.Red)
                    )
                }
                Image(
                    painterResource(id = it.drawableId),
                    contentDescription = stringResource(id = R.string.enemy),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(width = it.width.dp, height = it.height.dp)
                )
            }
        }
        minerals.forEach {
            Box(
                modifier = Modifier
                    .width(it.width.dp)
                    .offset(x = it.xOffset.dp, y = it.yOffset.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.mineral),
                    contentDescription = stringResource(id = R.string.mineral_content_description),
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(25.dp)
                        .alpha(alpha = it.alpha)
                )
            }
        }
        explosions.forEach {
            Image(
                painter = rememberImagePainter(data = R.drawable.explosion, imageLoader = imageLoader),
                contentDescription = stringResource(id = R.string.explosion_content_description),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .offset(it.xOffset.dp, it.yOffset.dp)
                    .size(it.size.dp)
            )
        }
        enemyLasers.forEach {
            Image(
                painterResource(id = it.drawableId),
                contentDescription = stringResource(id = R.string.enemy_laser),
                modifier = Modifier
                    .size(width = it.width.dp, height = it.height.dp)
                    .offset(x = it.xOffset.dp, y = it.yOffset.dp)
            )
        }
    }
}
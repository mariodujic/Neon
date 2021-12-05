package com.zero.neon

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.constellation.Star
import com.zero.neon.core.tinker
import com.zero.neon.ship.ShipLaser
import com.zero.neon.spaceobject.SpaceObject
import com.zero.neon.spaceobject.SpaceRock
import com.zero.neon.spaceobject.WeaponBooster
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

@Composable
fun rememberGameState(): GameState {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp
    val coroutineScope = rememberCoroutineScope()
    return remember { GameState(screenWidthDp, screenHeightDp, coroutineScope) }
}

class GameState(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val coroutineScope: CoroutineScope
) {

    private var gameContinuity: GameContinuity = GameContinuity.RUNNING

    init {
        coroutineScope.launch {
            createStars(
                screenHeight = screenHeightDp.value.toInt(),
                screenWidth = screenWidthDp.value.toInt(),
                coroutineScope = coroutineScope
            )
            launch(IO) {
                while (true) {
                    if (gameContinuity == GameContinuity.RUNNING) {
                        tinker(
                            id = animateStarsId,
                            triggerMillis = 50,
                            doWork = { animateStars() }
                        )
                        tinker(
                            id = moveShipId,
                            triggerMillis = 1,
                            doWork = { moveShip() }
                        )
                        tinker(
                            id = fireLaserId,
                            triggerMillis = 100,
                            doWork = { fireLasers() }
                        )
                        tinker(
                            id = moveLasersId,
                            triggerMillis = 5,
                            doWork = { moveLasers() }
                        )
                        tinker(
                            id = spaceRockId,
                            triggerMillis = 2000,
                            doWork = { addSpaceRock() }
                        )
                        tinker(
                            id = addWeaponBoosterId,
                            triggerMillis = 4000,
                            doWork = { addWeaponBooster() }
                        )
                        tinker(
                            id = moveSpaceObjectsId,
                            triggerMillis = 5,
                            doWork = { moveSpaceObjects() }
                        )
                    }
                    delay(1)
                }
            }
            while (true) {
                if (gameContinuity == GameContinuity.RUNNING) {
                    tinker(
                        id = monitorSpaceObjectHitsId,
                        triggerMillis = 1,
                        doWork = {
                            monitorLaserSpaceObjectsHit(
                                spaceRectObjects = spaceObjects.map { it.rect },
                                laserOffsets = lasers.map { it.offset }
                            )
                        }
                    )
                }
                delay(10)
            }
        }
    }

    /**
     * General settings
     */
    fun toggleGamePause() {
        gameContinuity = if (gameContinuity == GameContinuity.RUNNING) {
            GameContinuity.PAUSE
        } else GameContinuity.RUNNING
    }

    enum class GameContinuity {
        RUNNING,
        PAUSE
    }

    /**
     * Ship movement
     */
    val shipSize = 120.dp
    private val shipMaxXOffset = screenWidthDp / 2
    private val shipXMovementSpeed = 2.dp
    private val moveShipId = UUID.randomUUID().toString()

    private fun moveShip() {
        if (movingLeft && shipOffsetX > -shipMaxXOffset + shipSize / 6) shipOffsetX -= shipXMovementSpeed
        else movingLeft = false
        if (movingRight && shipOffsetX < shipMaxXOffset - shipSize / 6) shipOffsetX += shipXMovementSpeed
        else movingRight = false
    }

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

    /**
     * Ship laser
     */
    var lasers by mutableStateOf<List<ShipLaser>>(listOf())
        private set
    private val fireLaserId = UUID.randomUUID().toString()
    private fun fireLasers() {
        lasers = lasers
            .filter { it.shooting }
            .toMutableList()
            .apply {
                val laser = ShipLaser(
                    xOffset = shipOffsetX,
                    yRange = screenHeightDp,
                    screenWidth = screenWidthDp,
                    screenHeight = screenHeightDp,
                    onDestroyLaser = { destroyLaser(it) },
                    coroutineScope = coroutineScope
                )
                add(laser)
            }
    }

    private val moveLasersId = UUID.randomUUID().toString()
    private fun moveLasers() {
        lasers.forEach { it.moveLaser() }
    }

    private fun destroyLaser(laserId: String) {
        lasers = lasers
            .toMutableList()
            .apply {
                removeAll { it.id == laserId }
            }
    }

    private val monitorSpaceObjectHitsId = UUID.randomUUID().toString()
    private fun monitorLaserSpaceObjectsHit(
        spaceRectObjects: List<Rect>,
        laserOffsets: List<Offset>
    ) {
        spaceRectObjects.forEachIndexed { rockIndex, rockRect ->
            laserOffsets.forEachIndexed { laserIndex, offset ->
                if (rockRect.contains(offset)) {
                    spaceObjects[rockIndex].destroyObject()
                    lasers[laserIndex].destroyLaser()
                }
            }
        }
    }

    /**
     * Constellation
     */
    var stars by mutableStateOf<List<Star>>(listOf())
        private set

    private fun createStars(screenWidth: Int, screenHeight: Int, coroutineScope: CoroutineScope) {
        coroutineScope.launch(IO) {
            val starList = mutableListOf<Star>()
            for (i in 0..30) {
                val starXOffset = Random.nextInt(0, screenWidth).dp
                val starYOffset = Random.nextInt(0, screenHeight).dp
                val starSize = Random.nextInt(1, 12).dp
                val star = Star(xOffset = starXOffset, yOffset = starYOffset, size = starSize)
                starList.add(star)
            }
            stars = starList.toList()
        }
    }

    private val animateStarsId = UUID.randomUUID().toString()
    private fun animateStars() {
        stars.forEach { it.animateStar() }
    }

    /**
     * Space objects
     */
    var spaceObjects by mutableStateOf<List<SpaceObject>>(listOf())
        private set
    private val minRockSize = 20
    private val maxRockSize = 80
    private val spaceRockId = UUID.randomUUID().toString()

    private fun addSpaceRock() {
        val rockSize = Random.nextInt(minRockSize, maxRockSize)
        val rockXOffset = Random.nextInt(rockSize, screenWidthDp.value.toInt() - rockSize).dp
        spaceObjects = spaceObjects
            .filter { it.floating }
            .toMutableList()
            .apply {
                val spaceRock = SpaceRock(
                    xOffset = rockXOffset,
                    size = rockSize.dp,
                    screenHeight = screenHeightDp,
                    onDestroyRock = { destroyRock(it) }
                )
                add(spaceRock)
            }
    }

    private val addWeaponBoosterId = UUID.randomUUID().toString()
    private fun addWeaponBooster() {
        val size = 30
        val boosterXOffset = Random.nextInt(size, screenWidthDp.value.toInt() - size).dp
        spaceObjects = spaceObjects
            .filter { it.floating }
            .toMutableList()
            .apply {
                val spaceRock = WeaponBooster(
                    xOffset = boosterXOffset,
                    size = size.dp,
                    screenHeight = screenHeightDp,
                    onDestroyBooster = { destroyRock(it) }
                )
                add(spaceRock)
            }
    }

    private fun destroyRock(rockId: String) {
        spaceObjects = spaceObjects
            .toMutableList()
            .apply { removeAll { it.id == rockId } }
    }

    private val moveSpaceObjectsId = UUID.randomUUID().toString()
    private fun moveSpaceObjects() {
        spaceObjects.forEach { it.moveObject() }
    }
}
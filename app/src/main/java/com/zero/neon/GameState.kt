package com.zero.neon

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.constellation.Star
import com.zero.neon.core.tinker
import com.zero.neon.ship.Laser
import com.zero.neon.ship.ShipLaser
import com.zero.neon.ship.UltimateLaser
import com.zero.neon.spaceobject.Booster
import com.zero.neon.spaceobject.SpaceObject
import com.zero.neon.spaceobject.SpaceRock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
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

    private var gameContinuity = GameContinuity.RUNNING
    var refreshRate by mutableStateOf<Long>(0)

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
                            triggerMillis = 3,
                            doWork = { moveShip() }
                        )
                        tinker(
                            id = monitorShipSpaceObjectsCollisionId,
                            triggerMillis = 100,
                            doWork = { monitorShipSpaceObjectsCollision() }
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
                            id = moveUltimateLasersId,
                            triggerMillis = 40,
                            doWork = { moveUltimateLasers() }
                        )
                        tinker(
                            id = spaceRockId,
                            triggerMillis = 500,
                            doWork = { addSpaceRock() }
                        )
                        tinker(
                            id = addBoosterId,
                            triggerMillis = 4000,
                            doWork = { addBooster() }
                        )
                        tinker(
                            id = moveSpaceObjectsId,
                            triggerMillis = 5,
                            doWork = { moveSpaceObjects() }
                        )
                        tinker(
                            id = monitorSpaceObjectHitsId,
                            triggerMillis = 1,
                            doWork = {
                                monitorLaserSpaceObjectsHit()
                            }
                        )
                    }
                    refreshRate = System.currentTimeMillis()
                }
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
    var shipHp = 1000
    private val shipXMovementSpeed = 2.dp
    private val moveShipId = UUID.randomUUID().toString()

    private fun moveShip() {
        if (movingLeft && shipXOffset >= 0.dp - shipSize / 4) {
            shipXOffset -= shipXMovementSpeed
        } else movingLeft = false
        if (movingRight && shipXOffset <= screenWidthDp - (shipSize.value / 1.5).dp) {
            shipXOffset += shipXMovementSpeed
        } else movingRight = false
    }

    var shipXOffset = screenWidthDp / 2 - shipSize / 2
    var shipYOffset = screenHeightDp - shipSize
    private val spaceShipCollidePower = 100

    private var movingLeft = false
    private var movingRight = false

    fun moveShipLeft(movingShipLeft: Boolean) {
        movingLeft = movingShipLeft
    }

    fun moveShipRight(movingShipRight: Boolean) {
        movingRight = movingShipRight
    }

    private val monitorShipSpaceObjectsCollisionId = UUID.randomUUID().toString()
    private fun monitorShipSpaceObjectsCollision() {
        val shipRect = Rect(
            offset = Offset(x = shipXOffset.value, y = shipYOffset.value),
            size = Size(width = shipSize.value, height = shipSize.value)
        )
        spaceObjects.forEachIndexed { spaceObjectIndex, spaceObject ->
            val spaceRect = Rect(
                offset = Offset(x = spaceObject.xOffset.value, y = spaceObject.yOffset.value),
                size = Size(width = spaceObject.size.value, height = spaceObject.size.value)
            )
            if (spaceRect.overlaps(shipRect)) {
                spaceObjects[spaceObjectIndex].onObjectImpact(spaceShipCollidePower)
                shipHp -= spaceObject.impactPower

                if (spaceObject.drawableId == Booster.BoosterType.ULTIMATE_WEAPON_BOOSTER.drawableId) {
                    fireUltimateLaser()
                }
            }
        }
    }

    /**
     * Ship laser
     */
    var shipLasers: List<Laser> = listOf()
        private set
    private val fireLaserId = UUID.randomUUID().toString()
    private fun fireLasers() {
        shipLasers = shipLasers
            .filter { it.shooting }
            .toMutableList()
            .apply {
                val laser = ShipLaser(
                    xOffset = shipXOffset + shipSize / 2,
                    yRange = screenHeightDp,
                    onDestroyLaser = { destroyLaser(it) }
                )
                add(laser)
            }
    }

    private val moveLasersId = UUID.randomUUID().toString()
    private fun moveLasers() {
        shipLasers.forEach { it.moveLaser() }
    }

    private fun destroyLaser(laserId: String) {
        shipLasers = shipLasers.toMutableList().apply {
            removeAll { it.id == laserId }
        }
    }

    var ultimateLasers: List<Laser> = listOf()
        private set

    private fun fireUltimateLaser() {
        val ultimateLaserList = mutableListOf<UltimateLaser>()
        val laserCount = 10
        val horizontalLaserDistance = screenWidthDp / laserCount
        for (i in 0..laserCount) {
            val ultimateLaser = UltimateLaser(
                xOffset = horizontalLaserDistance * i,
                yRange = screenHeightDp,
                onDestroyLaser = { destroyUltimateLaser(it) }
            )
            ultimateLaserList.add(ultimateLaser)
        }
        ultimateLasers = ultimateLaserList
    }

    private val moveUltimateLasersId = UUID.randomUUID().toString()
    private fun moveUltimateLasers() {
        ultimateLasers.forEach { it.moveLaser() }
    }

    private fun destroyUltimateLaser(laserId: String) {
        ultimateLasers = ultimateLasers.toMutableList().apply {
            removeAll { it.id == laserId }
        }
    }

    private val monitorSpaceObjectHitsId = UUID.randomUUID().toString()
    private fun monitorLaserSpaceObjectsHit() {
        spaceObjects.forEachIndexed { spaceObjectIndex, spaceObject ->
            (shipLasers + ultimateLasers).forEachIndexed { laserIndex, laser ->
                val laserRect = Rect(
                    offset = Offset(
                        x = laser.xOffset.value,
                        y = laser.yOffset.value + screenHeightDp.value
                    ),
                    size = Size(width = laser.width.value, height = laser.height.value)
                )
                val spaceRect = Rect(
                    offset = Offset(x = spaceObject.xOffset.value, y = spaceObject.yOffset.value),
                    size = Size(width = spaceObject.size.value, height = spaceObject.size.value)
                )
                if (spaceObject.destroyable && spaceRect.overlaps(laserRect)) {
                    /**
                     * SpaceObject list throws IndexOutOfBoundsException if multiple lasers hit
                     * object fast. TODO Handle it without try-catch block.
                     */
                    try {
                        spaceObjects[spaceObjectIndex].onObjectImpact(laser.powerImpact)
                        shipLasers[laserIndex].destroyLaser()
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }
        }
    }

    /**
     * Constellation
     */
    var stars: List<Star> = listOf()
        private set

    private fun createStars(screenWidth: Int, screenHeight: Int, coroutineScope: CoroutineScope) {
        coroutineScope.launch(IO) {
            val starList = mutableListOf<Star>()
            for (i in 0..30) {
                val starXOffset = Random.nextInt(0, screenWidth).dp
                val starYOffset = Random.nextInt(0, screenHeight).dp
                val starSize = Random.nextInt(1, 12).dp
                val star = Star(
                    xOffset = starXOffset,
                    yOffset = starYOffset,
                    maxYOffset = screenHeight.dp,
                    size = starSize
                )
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
    var spaceObjects: List<SpaceObject> = listOf()
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
                    onDestroyRock = { destroySpaceObject(it) }
                )
                add(spaceRock)
            }
    }

    private val addBoosterId = UUID.randomUUID().toString()
    private fun addBooster() {
        val size = 45
        val boosterXOffset = Random.nextInt(size, screenWidthDp.value.toInt() - size).dp
        spaceObjects = spaceObjects
            .filter { it.floating }
            .toMutableList()
            .apply {
                val booster = Booster(
                    xOffset = boosterXOffset,
                    size = size.dp,
                    screenHeight = screenHeightDp,
                    onDestroyBooster = { destroySpaceObject(it) }
                )
                add(booster)
            }
    }

    private fun destroySpaceObject(rockId: String) {
        spaceObjects = spaceObjects.toMutableList().apply {
            removeAll { it.id == rockId }
        }
    }

    private val moveSpaceObjectsId = UUID.randomUUID().toString()
    private fun moveSpaceObjects() {
        spaceObjects.forEach { it.moveObject() }
    }
}
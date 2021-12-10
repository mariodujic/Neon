package com.zero.neon.game.state

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.game.constellation.ConstellationManager
import com.zero.neon.game.constellation.Star
import com.zero.neon.core.tinker
import com.zero.neon.game.ship.ship.ShipManager
import com.zero.neon.game.ship.weapons.Laser
import com.zero.neon.game.ship.weapons.LaserManager
import com.zero.neon.game.spaceobject.SpaceObject
import com.zero.neon.game.spaceobject.SpaceObjectsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

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
    var refreshHandler by mutableStateOf<Long>(0)

    /**
     * Constellation
     */
    var stars: List<Star> = emptyList()
        private set
    private val constellationManager = ConstellationManager(
        stars = { stars },
        setStars = { stars = it }
    )

    /**
     * Ship
     */
    private val shipManager = ShipManager(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp
    )
    val ship = shipManager.ship
    var shipLasers: List<Laser> = emptyList()
        private set
    var ultimateLasers: List<Laser> = emptyList()
        private set
    private val laserManager = LaserManager(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        shipLasers = { shipLasers },
        ultimateLasers = { ultimateLasers },
        setShipLasers = { shipLasers = it },
        setUltimateLasers = { ultimateLasers = it }
    )

    /**
     * Space objects
     */
    var spaceObjects: List<SpaceObject> = emptyList()
    private val spaceObjectsManager = SpaceObjectsManager(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        spaceObjects = { spaceObjects },
        setSpaceObject = { spaceObjects = it }
    )

    init {
        coroutineScope.launch {
            constellationManager.createStars(
                screenHeight = screenHeightDp.value.toInt(),
                screenWidth = screenWidthDp.value.toInt(),
                coroutineScope = coroutineScope
            )
            launch(IO) {
                while (true) {
                    if (gameContinuity == GameContinuity.RUNNING) {
                        tinker(
                            id = constellationManager.animateStarsId,
                            triggerMillis = 50,
                            doWork = { constellationManager.animateStars() }
                        )
                        tinker(
                            id = shipManager.moveShipId,
                            triggerMillis = 3,
                            doWork = { shipManager.moveShip() }
                        )
                        tinker(
                            id = shipManager.monitorShipSpaceObjectsCollisionId,
                            triggerMillis = 100,
                            doWork = {
                                shipManager.monitorShipSpaceObjectsCollision(spaceObjects = spaceObjects) {
                                    laserManager.fireUltimateLaser()
                                }
                            }
                        )
                        tinker(
                            id = laserManager.fireLaserId,
                            triggerMillis = 100,
                            doWork = { laserManager.fireLasers(ship = ship) }
                        )
                        tinker(
                            id = laserManager.moveShipLasersId,
                            triggerMillis = 5,
                            doWork = { laserManager.moveShipLasers() }
                        )
                        tinker(
                            id = laserManager.moveUltimateLasersId,
                            triggerMillis = 40,
                            doWork = { laserManager.moveUltimateLasers() }
                        )
                        tinker(
                            id = spaceObjectsManager.spaceRockId,
                            triggerMillis = 500,
                            doWork = { spaceObjectsManager.addSpaceRock() }
                        )
                        tinker(
                            id = spaceObjectsManager.addBoosterId,
                            triggerMillis = 4000,
                            doWork = { spaceObjectsManager.addBooster() }
                        )
                        tinker(
                            id = spaceObjectsManager.moveSpaceObjectsId,
                            triggerMillis = 5,
                            doWork = { spaceObjectsManager.moveSpaceObjects() }
                        )
                        tinker(
                            id = laserManager.monitorLaserSpaceObjectHitsId,
                            triggerMillis = 1,
                            doWork = {
                                laserManager.monitorLaserSpaceObjectsHit(spaceObjects = spaceObjects)
                            }
                        )
                    }
                    refreshHandler = System.currentTimeMillis()
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
}
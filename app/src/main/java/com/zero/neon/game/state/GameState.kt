package com.zero.neon.game.state

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.core.tinker
import com.zero.neon.game.constellation.ConstellationController
import com.zero.neon.game.constellation.Star
import com.zero.neon.game.enemy.EnemyController
import com.zero.neon.game.enemy.EnemyUI
import com.zero.neon.game.settings.GameStatus
import com.zero.neon.game.ship.ship.ShipController
import com.zero.neon.game.ship.weapons.LaserUI
import com.zero.neon.game.ship.weapons.LasersController
import com.zero.neon.game.spaceobject.SpaceObjectUI
import com.zero.neon.game.spaceobject.SpaceObjectsController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

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

    var gameStatus = GameStatus.RUNNING
        private set
    var refreshHandler by mutableStateOf<Long>(0)

    /**
     * Constellation
     */
    var stars: List<Star> = emptyList()
        private set
    private val constellationController = ConstellationController(
        stars = { stars },
        setStars = { stars = it }
    )

    /**
     * Ship
     */
    val shipController = ShipController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp
    )
    var shipLasers: List<LaserUI> = emptyList()
        private set
    var ultimateLasers: List<LaserUI> = emptyList()
        private set
    private val lasersController = LasersController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        setShipLasersUI = { shipLasers = it },
        setUltimateLasersUI = { ultimateLasers = it }
    )

    /**
     * Space objects
     */
    var spaceObjects: List<SpaceObjectUI> = emptyList()
    private val spaceObjectsController = SpaceObjectsController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        setSpaceObjectsUi = { spaceObjects = it }
    )

    /**
     * Enemies
     */
    var enemies: List<EnemyUI> = emptyList()
        private set
    private val enemyController = EnemyController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp
    ) { enemies = it }

    init {
        coroutineScope.launch {
            constellationController.createStars(
                screenHeight = screenHeightDp.value.toInt(),
                screenWidth = screenWidthDp.value.toInt(),
                coroutineScope = coroutineScope
            )
            launch(IO) {
                while (true) {
                    if (gameStatus == GameStatus.RUNNING) {
                        tinker(
                            id = constellationController.animateStarsId,
                            triggerMillis = 50,
                            doWork = { constellationController.animateStars() }
                        )
                        tinker(
                            id = shipController.moveShipId,
                            triggerMillis = 3,
                            doWork = { shipController.moveShip() }
                        )
                        tinker(
                            id = shipController.monitorShipSpaceObjectsCollisionId,
                            triggerMillis = 100,
                            doWork = {
                                shipController.monitorShipSpaceObjectsCollision(spaceObjects = spaceObjectsController.spaceObjects) {
                                    lasersController.fireUltimateLaser()
                                }
                            }
                        )
                        tinker(
                            id = lasersController.fireLaserId,
                            triggerMillis = 100,
                            doWork = { lasersController.fireLasers(ship = shipController.ship) }
                        )
                        tinker(
                            id = lasersController.moveShipLasersId,
                            triggerMillis = 5,
                            doWork = { lasersController.moveShipLasers() }
                        )
                        tinker(
                            id = lasersController.moveUltimateLasersId,
                            triggerMillis = 40,
                            doWork = { lasersController.moveUltimateLasers() }
                        )
                        tinker(
                            id = spaceObjectsController.addSpaceRockId,
                            triggerMillis = 500,
                            doWork = { spaceObjectsController.addSpaceRock() }
                        )
                        tinker(
                            id = enemyController.addEnemyId,
                            triggerMillis = 1000,
                            doWork = { enemyController.addEnemy() }
                        )
                        tinker(
                            id = spaceObjectsController.addBoosterId,
                            triggerMillis = 4000,
                            doWork = { spaceObjectsController.addBooster() }
                        )
                        tinker(
                            id = spaceObjectsController.moveSpaceObjectsId,
                            triggerMillis = 5,
                            doWork = { spaceObjectsController.moveSpaceObjects() }
                        )
                        tinker(
                            id = enemyController.moveEnemiesId,
                            triggerMillis = 5,
                            doWork = { enemyController.moveEnemies() }
                        )
                        tinker(
                            id = lasersController.monitorLaserSpaceObjectHitsId,
                            triggerMillis = 1,
                            doWork = {
                                lasersController.monitorLaserSpaceObjectsHit(
                                    spaceObjects = spaceObjectsController.spaceObjects,
                                    enemies = enemyController.enemies
                                )
                            }
                        )
                        tinker(
                            id = updateGameTimeId,
                            triggerMillis = 1000,
                            doWork = { updateGameTime() }
                        )
                    }
                    refreshHandler = System.currentTimeMillis()
                }
            }
        }
    }

    fun toggleGameStatus() {
        gameStatus = if (gameStatus == GameStatus.RUNNING) {
            GameStatus.PAUSE
        } else GameStatus.RUNNING
    }

    private var gameTime by mutableStateOf<Long>(0)
    private val updateGameTimeId = UUID.randomUUID().toString()
    private fun updateGameTime() {
        gameTime += 1
    }

    val gameTimeSec by derivedStateOf {
        val second = String.format("%02d", gameTime % 60)
        val minute = String.format("%02d", gameTime / (60) % 60)
        "$minute:$second"
    }
}
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
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.ship.ship.ShipController
import com.zero.neon.game.ship.weapons.LaserUI
import com.zero.neon.game.ship.weapons.LasersController
import com.zero.neon.game.spaceobject.SpaceObjectUI
import com.zero.neon.game.spaceobject.SpaceObjectsController
import com.zero.neon.game.stage.Stage.Companion.getCurrentGameStage
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
    val gameState = remember { GameStateImpl(screenWidthDp, screenHeightDp, coroutineScope) }
    gameState.refreshHandler
    return gameState
}

interface GameState {
    val gameStatus: GameStatus
    val stars: List<Star>
    val ship: Ship
    val shipLasers: List<LaserUI>
    val ultimateLasers: List<LaserUI>
    val spaceObjects: List<SpaceObjectUI>
    val enemies: List<EnemyUI>
    val gameTimeIndicator: String

    fun moveShipLeft(movingLeft: Boolean)
    fun moveShipRight(movingRight: Boolean)
    fun toggleGameStatus()
}

class GameStateImpl(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val coroutineScope: CoroutineScope
) : GameState {

    override var gameStatus = GameStatus.RUNNING
        private set
    var refreshHandler by mutableStateOf<Long>(0)

    /**
     * Constellation
     */
    override var stars: List<Star> = emptyList()
        private set
    private val constellationController = ConstellationController(
        stars = { stars },
        setStars = { stars = it }
    )

    /**
     * Ship
     */
    override var ship = Ship(
        xOffset = screenWidthDp / 2 - 85.dp / 2,
        yOffset = screenHeightDp - 140.dp
    )
        private set
    private val shipController = ShipController(
        screenWidthDp = screenWidthDp,
        ship = ship,
    ) { ship = it }

    override fun moveShipLeft(movingLeft: Boolean) {
        shipController.movingLeft = movingLeft
    }

    override fun moveShipRight(movingRight: Boolean) {
        shipController.movingRight = movingRight
    }

    override var shipLasers: List<LaserUI> = emptyList()
        private set
    override var ultimateLasers: List<LaserUI> = emptyList()
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
    override var spaceObjects: List<SpaceObjectUI> = emptyList()
        private set
    private val spaceObjectsController = SpaceObjectsController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        setSpaceObjectsUi = { spaceObjects = it }
    )

    /**
     * Enemies
     */
    override var enemies: List<EnemyUI> = emptyList()
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
                            id = shipController.monitorShipCollisionsId,
                            triggerMillis = 100,
                            doWork = {
                                shipController.monitorShipCollisions(
                                    spaceObjects = spaceObjectsController.spaceObjects,
                                    enemies = enemyController.enemies
                                ) { lasersController.fireUltimateLaser() }
                            }
                        )
                        tinker(
                            id = lasersController.fireLaserId,
                            triggerMillis = 100,
                            doWork = { lasersController.fireLasers(ship = ship) }
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
                            triggerMillis = gameStage.spaceRockSpawnRateMillis,
                            doWork = { spaceObjectsController.addSpaceRock() }
                        )
                        tinker(
                            id = enemyController.addEnemyId,
                            triggerMillis = gameStage.enemySpawnRateMillis,
                            doWork = { enemyController.addEnemy(gameStage.enemyEnemySpawnAttributes) }
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
                            id = monitorLoopInSecId,
                            triggerMillis = 1000,
                            doWork = { monitorLoopInSec() }
                        )
                    }
                    refreshHandler = System.currentTimeMillis()
                }
            }
        }
    }

    override fun toggleGameStatus() {
        gameStatus = if (gameStatus == GameStatus.RUNNING) {
            GameStatus.PAUSE
        } else GameStatus.RUNNING
    }

    private val monitorLoopInSecId = UUID.randomUUID().toString()
    private fun monitorLoopInSec() {
        updateGameTime()
        updateGameTimeIndicator()
        updateGameStage()

    }

    /**
     * Game time
     */
    private var gameTimeSec: Long = 0
    override var gameTimeIndicator: String = "00:00"
        private set

    private fun updateGameTime() {
        gameTimeSec += 1
    }

    private fun updateGameTimeIndicator() {
        val second = String.format("%02d", gameTimeSec % 60)
        val minute = String.format("%02d", gameTimeSec / (60) % 60)
        gameTimeIndicator = "$minute:$second"
    }

    /**
     * Game stage
     */
    private var gameStage = getCurrentGameStage(currentTimeSec = gameTimeSec)

    private fun updateGameStage() {
        gameStage = getCurrentGameStage(currentTimeSec = gameTimeSec)
    }
}
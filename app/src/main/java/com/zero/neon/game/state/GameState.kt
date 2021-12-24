package com.zero.neon.game.state

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.zero.neon.core.tinker
import com.zero.neon.game.booster.BoosterController
import com.zero.neon.game.booster.BoosterUI
import com.zero.neon.game.constellation.ConstellationController
import com.zero.neon.game.constellation.Star
import com.zero.neon.game.enemy.laser.EnemyLasersController
import com.zero.neon.game.enemy.ship.EnemyController
import com.zero.neon.game.enemy.ship.EnemyUI
import com.zero.neon.game.settings.GameStatus
import com.zero.neon.game.ship.laser.LaserUI
import com.zero.neon.game.ship.laser.LasersController
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.ship.ship.ShipController
import com.zero.neon.game.spaceobject.SpaceObjectUI
import com.zero.neon.game.spaceobject.SpaceObjectsController
import com.zero.neon.game.stage.Stage.Companion.getGameStage
import com.zero.neon.game.stage.StageGameAct
import com.zero.neon.game.stage.StageMessageAct
import com.zero.neon.utils.observeAsState
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
    val lifecycle by LocalLifecycleOwner.current.lifecycle.observeAsState()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_PAUSE) {
            gameState.setGameStatus(GameStatus.PAUSE)
        } else if (lifecycle == Lifecycle.Event.ON_RESUME) {
            gameState.setGameStatus(GameStatus.RUNNING)
        }
    }
    gameState.refreshHandler
    return gameState
}

interface GameState {
    val gameStatus: GameStatus
    val gameMessage: String
    val stars: List<Star>
    val ship: Ship
    val shipLasers: List<LaserUI>
    val ultimateLasers: List<LaserUI>
    val spaceObjects: List<SpaceObjectUI>
    val boosters: List<BoosterUI>
    val enemies: List<EnemyUI>
    val enemyLasers: List<LaserUI>
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

    fun setGameStatus(gameStatus: GameStatus) {
        this.gameStatus = gameStatus
    }

    override fun toggleGameStatus() {
        gameStatus = if (gameStatus == GameStatus.RUNNING) {
            GameStatus.PAUSE
        } else GameStatus.RUNNING
    }

    var refreshHandler by mutableStateOf<Long>(0)

    /**
     * Constellation
     *
     * These objects do not directly affect game play, are passive, and their purpose is
     * to improve game ambient.
     */
    override var stars: List<Star> = emptyList()
        private set
    private val constellationController = ConstellationController(
        stars = { stars },
        setStars = { stars = it }
    )

    /**
     * Ship
     *
     * User has horizontal control of the ship. Ship can take damage by colliding with
     * incoming objects, e.g. Enemy, EnemyLaser, SpaceRock. It can also collect different
     * boosters. By collecting Booster object, ship will gain special ability for a limited
     * period of time.
     */
    override var ship = Ship(
        xOffset = screenWidthDp / 2 - 85.dp / 2,
        yOffset = screenHeightDp + 240.dp
    )
        private set
    private val shipController = ShipController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
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
     *
     *  Floating objects that directly affect [ship]. Colliding ship with [spaceObjects] has
     *  negative impact on ship hp. Ship can destroy space objects.
     *
     *  @see com.zero.neon.game.spaceobject.SpaceObject
     */
    override var spaceObjects: List<SpaceObjectUI> = emptyList()
        private set
    private val spaceObjectsController = SpaceObjectsController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        setSpaceObjectsUi = { spaceObjects = it }
    )

    /**
     * Boosters
     *
     *  Floating objects that directly affect [ship]. Colliding ship with [boosters] give
     *  ship special ability.
     *
     *  @see com.zero.neon.game.booster.Booster
     */
    override var boosters: List<BoosterUI> = emptyList()
        private set
    private val boosterController = BoosterController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp,
        updateBoosters = { boosters = it }
    )

    /**
     * Enemies
     *
     *  Enemies directly affect [ship]. Enemy can collide with ship and enemy can fire lasers.
     *  Both enemy collision and laser his has negative impact on ships hp. Ship can destroy
     *  enemies.
     *
     *  @see com.zero.neon.game.enemy.ship.Enemy
     */
    override var enemies: List<EnemyUI> = emptyList()
        private set
    private val enemyController = EnemyController(
        screenWidthDp = screenWidthDp,
        screenHeightDp = screenHeightDp
    ) { enemies = it }

    override var enemyLasers: List<LaserUI> = emptyList()
        private set
    private val enemyLaserController = EnemyLasersController(
        screenHeightDp = screenHeightDp,
        screenWidthDp = screenWidthDp,
    ) { enemyLasers = it }

    init {
        coroutineScope.launch {
            constellationController.createStars(
                screenHeight = screenHeightDp.value.toInt(),
                screenWidth = screenWidthDp.value.toInt(),
                coroutineScope = coroutineScope
            )
            launch(IO) {
                /**
                 * Game loop
                 *
                 * Periodically runs any given work. Depending on a [gameStatus], loop can be
                 * on hold and resume any time.
                 */
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
                                    boosters = boosterController.boosters,
                                    enemies = enemyController.enemies,
                                    enemyLasers = enemyLaserController.enemyLasers
                                ) { lasersController.fireUltimateLaser() }
                            }
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
                        if (boosterController.hasBoosters()) {
                            tinker(
                                id = boosterController.moveBoostersId,
                                triggerMillis = 5,
                                doWork = { boosterController.moveBoosters() }
                            )
                        }
                        if (lasersController.hasUltimateLasers()) {
                            tinker(
                                id = lasersController.moveUltimateLasersId,
                                triggerMillis = 40,
                                doWork = { lasersController.moveUltimateLasers() }
                            )
                        }
                        if (lasersController.hasShipLasers()) {
                            tinker(
                                id = lasersController.moveShipLasersId,
                                triggerMillis = 5,
                                doWork = { lasersController.moveShipLasers() }
                            )
                        }
                        if (spaceObjectsController.hasSpaceObjects()) {
                            tinker(
                                id = spaceObjectsController.moveSpaceObjectsId,
                                triggerMillis = 5,
                                doWork = { spaceObjectsController.moveSpaceObjects() }
                            )
                        }
                        if (enemyLaserController.hasEnemyLasers()) {
                            tinker(
                                id = enemyLaserController.moveEnemyLasersId,
                                triggerMillis = 5,
                                doWork = { enemyLaserController.moveEnemyLasers() }
                            )
                        }
                        if (enemyController.hasEnemies()) {
                            tinker(
                                id = enemyController.moveEnemiesId,
                                triggerMillis = 5,
                                doWork = { enemyController.moveEnemies() }
                            )
                        }
                        if (
                            gameStage.stageAct is StageGameAct ||
                            enemyController.hasEnemies() ||
                            spaceObjectsController.hasSpaceObjects()
                        ) {
                            tinker(
                                id = lasersController.fireLaserId,
                                triggerMillis = 100,
                                doWork = { lasersController.fireLasers(ship = ship) }
                            )
                        }
                        if (gameStage.stageAct is StageGameAct) {
                            with(gameStage.stageAct as StageGameAct) {
                                tinker(
                                    id = spaceObjectsController.addSpaceRockId,
                                    triggerMillis = spaceRockSpawnRateMillis,
                                    doWork = { spaceObjectsController.addSpaceRock() }
                                )
                                tinker(
                                    id = enemyController.addEnemyId,
                                    triggerMillis = enemySpawnRateMillis,
                                    doWork = { enemyController.addEnemy(enemyEnemySpawnAttributes) }
                                )
                                tinker(
                                    id = enemyLaserController.fireEnemyLaserId,
                                    triggerMillis = 2000,
                                    doWork = { enemyLaserController.fireEnemyLasers(enemies = enemyController.enemies) }
                                )
                                tinker(
                                    id = boosterController.addBoosterId,
                                    triggerMillis = 4000,
                                    doWork = { boosterController.addBooster() }
                                )
                            }
                        }
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

    private val monitorLoopInSecId = UUID.randomUUID().toString()
    private fun monitorLoopInSec() {
        updateGameTime()
        updateGameTimeIndicator()
        updateGameStage()
    }

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
     *
     * Game consists of multiple stages. Each stage has its own game settings.
     *
     * @see com.zero.neon.game.stage.Stage
     */
    private var gameStage = getGameStage(true)
    override var gameMessage: String = ""
        private set

    private fun updateGameStage() {
        gameStage = getGameStage(
            readyForNextStage = !enemyController.hasEnemies() && !spaceObjectsController.hasSpaceObjects()
        )
        gameMessage = when (val stagePartition = gameStage.stageAct) {
            is StageMessageAct -> stagePartition.message
            else -> ""
        }
    }
}
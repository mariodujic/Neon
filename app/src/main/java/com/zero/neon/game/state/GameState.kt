package com.zero.neon.game.state

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import com.zero.neon.core.tinker
import com.zero.neon.game.booster.Booster
import com.zero.neon.game.booster.BoosterController
import com.zero.neon.game.booster.BoosterToBoosterUIMapper
import com.zero.neon.game.booster.BoosterUI
import com.zero.neon.game.constellation.ConstellationController
import com.zero.neon.game.constellation.Star
import com.zero.neon.game.enemy.laser.EnemyLaser
import com.zero.neon.game.enemy.laser.EnemyLasersController
import com.zero.neon.game.enemy.ship.*
import com.zero.neon.game.laser.Laser
import com.zero.neon.game.laser.LaserToLaserUIMapper
import com.zero.neon.game.settings.GameStatus
import com.zero.neon.game.ship.laser.LaserUI
import com.zero.neon.game.ship.laser.LasersController
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.ship.ship.ShipController
import com.zero.neon.game.spaceobject.SpaceObject
import com.zero.neon.game.spaceobject.SpaceObjectToSpaceObjectUIMapper
import com.zero.neon.game.spaceobject.SpaceObjectUI
import com.zero.neon.game.spaceobject.SpaceObjectsController
import com.zero.neon.game.stage.StageController
import com.zero.neon.game.stage.StageGameAct
import com.zero.neon.game.stage.StageMessageAct
import com.zero.neon.utils.observeAsState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun rememberGameState(): GameState {

    val configuration = LocalConfiguration.current
    val screenWidth = rememberSaveable { configuration.screenWidthDp.toFloat() }
    val screenHeight = rememberSaveable { configuration.screenHeightDp.toFloat() }
    val coroutineScope = rememberCoroutineScope()

    var stars: List<Star> by rememberSaveable { mutableStateOf(emptyList()) }
    val constellationController = remember {
        ConstellationController(
            stars = { stars },
            setStars = { stars = it }
        )
    }

    var ship by rememberSaveable {
        mutableStateOf(
            Ship(xOffset = screenWidth / 2 - 85f / 2, yOffset = screenHeight + 240f)
        )
    }
    val shipController = remember {
        ShipController(
            screenWidthDp = screenWidth,
            screenHeightDp = screenHeight,
            ship = ship,
        ) { ship = it }
    }

    var shipLasers: List<Laser> by remember { mutableStateOf(emptyList()) }
    var ultimateLasers: List<Laser> by remember { mutableStateOf(emptyList()) }
    val lasersController = remember {
        LasersController(
            screenWidthDp = screenWidth,
            screenHeightDp = screenHeight,
            initialShipLasers = shipLasers,
            initialUltimateLasers = ultimateLasers,
            setShipLasers = { shipLasers = it },
            setUltimateLasers = { ultimateLasers = it }
        )
    }

    var spaceObjects: List<SpaceObject> by rememberSaveable { mutableStateOf(emptyList()) }
    val spaceObjectsController = remember {
        SpaceObjectsController(
            screenWidthDp = screenWidth,
            screenHeightDp = screenHeight,
            initialSpaceObjects = spaceObjects,
            setSpaceObjects = { spaceObjects = it }
        )
    }

    var boosters: List<Booster> by rememberSaveable { mutableStateOf(emptyList()) }
    val boosterController = remember {
        BoosterController(
            screenWidthDp = screenWidth,
            screenHeightDp = screenHeight,
            initialBoosters = boosters,
            updateBoosters = { boosters = it }
        )
    }

    var enemies: List<Enemy> by rememberSaveable { mutableStateOf(emptyList()) }
    val enemyController = remember {
        EnemyController(
            screenWidthDp = screenWidth,
            screenHeightDp = screenHeight,
            initialEnemies = enemies,
            setEnemies = { enemies = it }
        )
    }

    var enemyLasers: List<EnemyLaser> by remember { mutableStateOf(emptyList()) }
    val enemyLaserController = remember {
        EnemyLasersController(
            screenHeightDp = screenHeight,
            initialEnemyLasers = enemyLasers,
        ) { enemyLasers = it }
    }

    var gameMessage by rememberSaveable { mutableStateOf("") }

    val stageController = rememberSaveable(saver = StageController.saver()) { StageController() }
    var gameStage by rememberSaveable { mutableStateOf(stageController.getGameStage(true)) }
    fun updateGameStage() {
        gameStage = stageController.getGameStage(
            readyForNextStage = !enemyController.hasEnemies() && !spaceObjectsController.hasSpaceObjects()
        )
        gameMessage = when (val stagePartition = gameStage.stageAct) {
            is StageMessageAct -> stagePartition.message
            else -> ""
        }
    }

    var gameStatus by rememberSaveable { mutableStateOf(GameStatus.RUNNING) }
    fun setGameStatus(_gameStatus: GameStatus) {
        gameStatus = _gameStatus
    }

    var gameTimeSec by rememberSaveable { mutableStateOf<Long>(0) }
    fun updateGameTime() {
        gameTimeSec += 1
    }

    var gameTimeIndicator by rememberSaveable { mutableStateOf("00:00") }
    fun updateGameTimeIndicator() {
        val second = String.format("%02d", gameTimeSec % 60)
        val minute = String.format("%02d", gameTimeSec / (60) % 60)
        gameTimeIndicator = "$minute:$second"
    }

    val monitorLoopInSecId = rememberSaveable { UUID.randomUUID().toString() }
    fun monitorLoopInSec() {
        updateGameTime()
        updateGameTimeIndicator()
        updateGameStage()
    }

    val lifecycle by LocalLifecycleOwner.current.lifecycle.observeAsState()
    LaunchedEffect(lifecycle) {
        if (lifecycle == Lifecycle.Event.ON_PAUSE || lifecycle == Lifecycle.Event.ON_STOP) {
            setGameStatus(GameStatus.PAUSE)
        } else if (lifecycle == Lifecycle.Event.ON_RESUME || lifecycle == Lifecycle.Event.ON_START) {
            setGameStatus(GameStatus.RUNNING)
        }
    }

    var refreshHandler by remember { mutableStateOf<Long>(0) }
    DisposableEffect(lifecycle) {
        var loopRunning = true
        val job = coroutineScope.launch {
            constellationController.createStars(
                screenHeight = screenHeight,
                screenWidth = screenWidth
            )
            launch(IO) {
                while (loopRunning) {
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
                            id = lasersController.monitorLaserCollisionId,
                            triggerMillis = 1,
                            doWork = {
                                lasersController.monitorLaserCollision(
                                    spaceObjects = spaceObjectsController.spaceObjects,
                                    enemies = enemyController.enemies
                                )
                            }
                        )
                        if (boosterController.hasBoosters()) {
                            tinker(
                                id = boosterController.processBoostersId,
                                triggerMillis = 5,
                                doWork = { boosterController.processBoosters() }
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
                                id = spaceObjectsController.processSpaceObjectsId,
                                triggerMillis = 5,
                                doWork = { spaceObjectsController.processSpaceObjects() }
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
                                id = enemyController.processEnemiesId,
                                triggerMillis = 5,
                                doWork = { enemyController.processEnemies() }
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
                                    triggerMillis = spaceRockSpawnRateMillis.timeMillis,
                                    doWork = { spaceObjectsController.addSpaceRock() }
                                )
                                enemyAttributes?.let {
                                    if (enemyAttributes is LevelOneEnemyAttributes) {
                                        tinker(
                                            id = enemyController.addEnemyId,
                                            triggerMillis = enemyAttributes.enemySpawnRateMillis.timeMillis,
                                            doWork = { enemyController.addEnemy(enemyAttributes) }
                                        )
                                        tinker(
                                            id = enemyLaserController.fireEnemyLaserId,
                                            triggerMillis = enemyAttributes.enemyFireRateMillis.timeMillis,
                                            doWork = { enemyLaserController.fireEnemyLasers(enemies = enemyController.enemies) }
                                        )
                                    }
                                }
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
                    } else if (gameStatus == GameStatus.STOP) {
                        break
                    }
                    refreshHandler = System.currentTimeMillis()
                }
            }
        }
        onDispose {
            loopRunning = false
            job.cancel()
        }
    }
    refreshHandler
    return GameState(
        gameStatus = gameStatus,
        gameMessage = gameMessage,
        stars = stars,
        ship = ship,
        shipLasers = shipLasers.map { lasersMapper(it) },
        ultimateLasers = ultimateLasers.map { lasersMapper(it) },
        spaceObjects = spaceObjects.map { spaceObjectsMapper(it) },
        boosters = boosters.map { boosterMapper(it) },
        enemies = enemies.map { enemyMapper(it) },
        enemyLasers = enemyLasers.map { lasersMapper(it) },
        gameTimeIndicator = gameTimeIndicator,
        moveShipLeft = { shipController.movingLeft = it },
        moveShipRight = { shipController.movingRight = it },
        toggleGameStatus = {
            gameStatus = if (gameStatus == GameStatus.RUNNING) {
                GameStatus.PAUSE
            } else GameStatus.RUNNING
        }
    )
}

data class GameState(
    val gameStatus: GameStatus,
    val gameMessage: String,
    val stars: List<Star>,
    val ship: Ship,
    val shipLasers: List<LaserUI>,
    val ultimateLasers: List<LaserUI>,
    val spaceObjects: List<SpaceObjectUI>,
    val boosters: List<BoosterUI>,
    val enemies: List<EnemyUI>,
    val enemyLasers: List<LaserUI>,
    val gameTimeIndicator: String,
    val moveShipLeft: (Boolean) -> Unit,
    val moveShipRight: (Boolean) -> Unit,
    val toggleGameStatus: () -> Unit
)


private val boosterMapper = BoosterToBoosterUIMapper()
private val enemyMapper = EnemyToEnemyUIMapper()
private val lasersMapper = LaserToLaserUIMapper()
private val spaceObjectsMapper = SpaceObjectToSpaceObjectUIMapper()
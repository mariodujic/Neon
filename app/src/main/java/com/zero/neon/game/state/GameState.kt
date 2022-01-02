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
import com.zero.neon.game.enemy.laser.EnemyLasersController
import com.zero.neon.game.enemy.ship.controller.EnemyController
import com.zero.neon.game.enemy.ship.mapper.EnemyToEnemyUIMapper
import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.enemy.ship.model.EnemyUI
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
import com.zero.neon.game.stage.StageBoss
import com.zero.neon.game.stage.StageController
import com.zero.neon.game.stage.StageGame
import com.zero.neon.game.stage.StageMessage
import com.zero.neon.utils.UuidUtils
import com.zero.neon.utils.observeAsState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun rememberGameState(): GameState {

    val configuration = LocalConfiguration.current
    val screenWidth = rememberSaveable { configuration.screenWidthDp.toFloat() }
    val screenHeight = rememberSaveable { configuration.screenHeightDp.toFloat() }
    val uuidUtils = remember { UuidUtils() }
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
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            ship = ship,
        ) { ship = it }
    }

    var shipLasers: List<Laser> by remember { mutableStateOf(emptyList()) }
    var ultimateLasers: List<Laser> by remember { mutableStateOf(emptyList()) }
    val lasersController = remember {
        LasersController(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            uuidUtils = uuidUtils,
            initialShipLasers = shipLasers,
            initialUltimateLasers = ultimateLasers,
            setShipLasers = { shipLasers = it },
            setUltimateLasers = { ultimateLasers = it }
        )
    }

    var spaceObjects: List<SpaceObject> by rememberSaveable { mutableStateOf(emptyList()) }
    val spaceObjectsController = remember {
        SpaceObjectsController(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            initialSpaceObjects = spaceObjects,
            setSpaceObjects = { spaceObjects = it }
        )
    }

    var boosters: List<Booster> by rememberSaveable { mutableStateOf(emptyList()) }
    val boosterController = remember {
        BoosterController(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            uuidUtils = uuidUtils,
            initialBoosters = boosters,
            updateBoosters = { boosters = it }
        )
    }

    var enemies: List<Enemy> by rememberSaveable { mutableStateOf(emptyList()) }
    val enemyController = remember {
        EnemyController(
            screenWidth = screenWidth,
            screenHeight = screenHeight,
            uuidUtils = uuidUtils,
            getShip = { ship },
            initialEnemies = enemies,
            setEnemies = { enemies = it }
        )
    }

    var enemyLasers: List<Laser> by remember { mutableStateOf(emptyList()) }
    val enemyLaserController = remember {
        EnemyLasersController(
            screenHeight = screenHeight,
            initialEnemyLasers = enemyLasers
        ) { enemyLasers = it }
    }

    var gameMessage by rememberSaveable { mutableStateOf("") }

    val stageController = rememberSaveable(saver = StageController.saver()) { StageController() }
    var gameStage by rememberSaveable { mutableStateOf(stageController.getGameStage(true)) }
    fun updateGameStage() {
        gameStage = stageController.getGameStage(
            readyForNextStage = !enemyController.hasEnemies() && !spaceObjectsController.hasSpaceObjects()
        )
        gameMessage = when (val stage = gameStage) {
            is StageMessage -> stage.message
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
                                    enemies = enemies,
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
                                    enemies = enemies
                                )
                            }
                        )
                        tinker(
                            id = enemyLaserController.fireEnemyLaserId,
                            triggerMillis = 1000,
                            doWork = { enemyLaserController.fireEnemyLasers(enemies = enemies) }
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
                                id = lasersController.processLasersId,
                                triggerMillis = 40,
                                doWork = { lasersController.processLasers() }
                            )
                        }
                        if (lasersController.hasShipLasers()) {
                            tinker(
                                id = lasersController.processShipLasersId,
                                triggerMillis = 5,
                                doWork = { lasersController.processShipLasers() }
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
                                id = enemyLaserController.processLasersId,
                                triggerMillis = 5,
                                doWork = { enemyLaserController.processLasers() }
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
                            gameStage is StageGame ||
                            enemyController.hasEnemies() ||
                            spaceObjectsController.hasSpaceObjects()
                        ) {
                            tinker(
                                id = lasersController.fireLaserId,
                                triggerMillis = 100,
                                doWork = { lasersController.fireLasers(ship = ship) }
                            )
                        }
                        if (gameStage is StageGame) {
                            val stage = gameStage as StageGame
                            tinker(
                                id = spaceObjectsController.addSpaceRockId,
                                triggerMillis = stage.spaceRockSpawnRateMillis.timeMillis,
                                doWork = { spaceObjectsController.addSpaceRock() }
                            )
                            tinker(
                                id = enemyController.addEnemyId,
                                triggerMillis = stage.enemyType.spawnRate.timeMillis,
                                doWork = { enemyController.addEnemy(stage.enemyType) }
                            )
                            tinker(
                                id = boosterController.addBoosterId,
                                triggerMillis = 4000,
                                doWork = { boosterController.addBooster() }
                            )
                        } else if (gameStage is StageBoss) {
                            val stage = gameStage as StageBoss
                            tinker(
                                id = enemyController.addBossId,
                                triggerMillis = stage.enemyType.spawnRate.timeMillis,
                                doWork = { enemyController.addEnemy(stage.enemyType) }
                            )
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
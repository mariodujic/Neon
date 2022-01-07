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
import com.zero.neon.game.common.Millis
import com.zero.neon.game.constellation.ConstellationController
import com.zero.neon.game.constellation.Star
import com.zero.neon.game.enemy.laser.EnemyLasersController
import com.zero.neon.game.enemy.ship.controller.EnemyController
import com.zero.neon.game.enemy.ship.mapper.EnemyToEnemyUIMapper
import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.enemy.ship.model.EnemyUI
import com.zero.neon.game.explosion.controller.ExplosionController
import com.zero.neon.game.explosion.model.Explosion
import com.zero.neon.game.laser.Laser
import com.zero.neon.game.laser.LaserToLaserUIMapper
import com.zero.neon.game.mineral.controller.MineralsController
import com.zero.neon.game.mineral.mapper.MineralToMineralUIMapper
import com.zero.neon.game.mineral.model.Mineral
import com.zero.neon.game.mineral.model.MineralUI
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

    var explosions: List<Explosion> by rememberSaveable { mutableStateOf(emptyList()) }
    val explosionsController = remember {
        ExplosionController(
            initialExplosions = explosions,
            updateExplosions = { explosions = it }
        )
    }

    var mineralsEarnedTotal: Int by rememberSaveable { mutableStateOf(0) }
    var minerals: List<Mineral> by rememberSaveable { mutableStateOf(emptyList()) }
    val mineralsController = remember {
        MineralsController(
            initialMinerals = minerals,
            updateMinerals = { minerals = it },
            updateMineralsEarnedTotal = { mineralsEarnedTotal += it }
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
            setEnemies = { enemies = it },
            addMinerals = { xOffset: Float, yOffset: Float, width: Float, mineralAmount: Int ->
                mineralsController.addMinerals(
                    xOffset = xOffset,
                    yOffset = yOffset,
                    width = width,
                    mineralAmount = mineralAmount
                )
            },
            addExplosion = { xOffset: Float, yOffset: Float, width: Float, height: Float ->
                explosionsController.addExplosion(
                    xOffset = xOffset,
                    yOffset = yOffset,
                    width = width,
                    height = height
                )
            }
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
    val monitorLoopRepeatTime = remember { Millis(1000) }
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
                            repeatTime = constellationController.animateStarsRepeatTime,
                            doWork = { constellationController.animateStars() }
                        )
                        tinker(
                            id = shipController.moveShipId,
                            repeatTime = shipController.moveShipRepeatTime,
                            doWork = { shipController.moveShip() }
                        )
                        tinker(
                            id = shipController.monitorShipCollisionsId,
                            repeatTime = shipController.monitorShipCollisionsRepeatTime,
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
                            repeatTime = lasersController.monitorLaserCollisionRepeatTime,
                            doWork = {
                                lasersController.monitorLaserCollision(
                                    spaceObjects = spaceObjectsController.spaceObjects,
                                    enemies = enemies
                                )
                            }
                        )
                        tinker(
                            id = enemyLaserController.fireEnemyLaserId,
                            repeatTime = enemyLaserController.fireEnemyLaserRepeatTime,
                            doWork = { enemyLaserController.fireEnemyLasers(enemies = enemies) }
                        )
                        tinker(
                            id = mineralsController.processMineralsId,
                            repeatTime = mineralsController.processMineralsRepeatTime,
                            doWork = { mineralsController.processMinerals() }
                        )
                        tinker(
                            id = explosionsController.processExplosionsId,
                            repeatTime = explosionsController.processExplosionsRepeatTime,
                            doWork = { explosionsController.processExplosions() }
                        )
                        if (boosterController.hasBoosters()) {
                            tinker(
                                id = boosterController.processBoostersId,
                                repeatTime = boosterController.processBoostersRepeatTime,
                                doWork = { boosterController.processBoosters() }
                            )
                        }
                        if (lasersController.hasUltimateLasers()) {
                            tinker(
                                id = lasersController.processLasersId,
                                repeatTime = lasersController.processLasersRepeatTime,
                                doWork = { lasersController.processLasers() }
                            )
                        }
                        if (lasersController.hasShipLasers()) {
                            tinker(
                                id = lasersController.processShipLasersId,
                                repeatTime = lasersController.processShipLasersRepeatTime,
                                doWork = { lasersController.processShipLasers() }
                            )
                        }
                        if (spaceObjectsController.hasSpaceObjects()) {
                            tinker(
                                id = spaceObjectsController.processSpaceObjectsId,
                                repeatTime = spaceObjectsController.processSpaceObjectsRepeatTime,
                                doWork = { spaceObjectsController.processSpaceObjects() }
                            )
                        }
                        if (enemyLaserController.hasEnemyLasers()) {
                            tinker(
                                id = enemyLaserController.processLasersId,
                                repeatTime = enemyLaserController.processLasersRepeatTime,
                                doWork = { enemyLaserController.processLasers() }
                            )
                        }
                        if (enemyController.hasEnemies()) {
                            tinker(
                                id = enemyController.processEnemiesId,
                                repeatTime = enemyController.processEnemiesRepeatTime,
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
                                repeatTime = lasersController.fireLaserRepeatTime,
                                doWork = { lasersController.fireLasers(ship = ship) }
                            )
                        }
                        if (gameStage is StageGame) {
                            val stage = gameStage as StageGame
                            tinker(
                                id = spaceObjectsController.addSpaceRockId,
                                repeatTime = stage.spaceRockSpawnRateMillis,
                                doWork = { spaceObjectsController.addSpaceRock() }
                            )
                            tinker(
                                id = enemyController.addEnemyId,
                                repeatTime = stage.enemyType.spawnRate,
                                doWork = { enemyController.addEnemy(stage.enemyType) }
                            )
                            tinker(
                                id = boosterController.addBoosterId,
                                repeatTime = boosterController.addBoosterRepeatTime,
                                doWork = { boosterController.addBooster() }
                            )
                        } else if (gameStage is StageBoss) {
                            val stage = gameStage as StageBoss
                            tinker(
                                id = stage.bossId,
                                repeatTime = stage.enemyType.spawnRate,
                                doWork = { enemyController.addEnemy(stage.enemyType) }
                            )
                        }
                        tinker(
                            id = monitorLoopInSecId,
                            repeatTime = monitorLoopRepeatTime,
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
        minerals = minerals.map { mineralToMineralUIMapper(it) },
        mineralsEarnedTotal = mineralsEarnedTotal.toString(),
        explosions = explosions,
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
    val minerals: List<MineralUI>,
    val mineralsEarnedTotal: String,
    val explosions: List<Explosion>,
    val moveShipLeft: (Boolean) -> Unit,
    val moveShipRight: (Boolean) -> Unit,
    val toggleGameStatus: () -> Unit
)


private val boosterMapper = BoosterToBoosterUIMapper()
private val enemyMapper = EnemyToEnemyUIMapper()
private val mineralToMineralUIMapper = MineralToMineralUIMapper()
private val lasersMapper = LaserToLaserUIMapper()
private val spaceObjectsMapper = SpaceObjectToSpaceObjectUIMapper()

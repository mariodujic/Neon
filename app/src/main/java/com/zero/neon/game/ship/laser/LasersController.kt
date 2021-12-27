package com.zero.neon.game.ship.laser

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.zero.neon.game.enemy.ship.Enemy
import com.zero.neon.game.laser.Laser
import com.zero.neon.game.ship.laser.ShipBoostedLaser.Companion.SHIP_BOOSTED_LASER_WIDTH
import com.zero.neon.game.ship.laser.ShipLaser.Companion.SHIP_LASER_WIDTH
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.ship.ship.ShipController.Companion.TRIPLE_LASER_SIDE_OFFSET
import com.zero.neon.game.spaceobject.SpaceObject
import com.zero.neon.utils.UuidUtils

class LasersController(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    private val uuidUtils: UuidUtils = UuidUtils(),
    initialShipLasers: List<Laser> = listOf(),
    initialUltimateLasers: List<Laser> = listOf(),
    private val setShipLasers: (List<Laser>) -> Unit,
    private val setUltimateLasers: (List<Laser>) -> Unit
) {

    private var shipLasers: List<Laser> = initialShipLasers
    private var ultimateLasers: List<Laser> = initialUltimateLasers

    val fireLaserId = uuidUtils.getUuid()
    fun fireLasers(ship: Ship) {

        val lasers = if (ship.laserBoosterEnabled) {
            val laser = ShipBoostedLaser(
                id = uuidUtils.getUuid(),
                xOffset = ship.xOffset + ship.width / 2 - SHIP_BOOSTED_LASER_WIDTH / 2,
                yOffset = -ship.height / 2,
                yRange = screenHeightDp,
                onDestroyLaser = { destroyShipLaser(it) }
            )
            if (ship.tripleLaserBoosterEnabled) {
                listOf(
                    laser.copy(xOffset = laser.xOffset - TRIPLE_LASER_SIDE_OFFSET),
                    laser.copy(xOffset = laser.xOffset),
                    laser.copy(xOffset = laser.xOffset + TRIPLE_LASER_SIDE_OFFSET)
                )
            } else {
                listOf(laser)
            }
        } else {
            val laser = ShipLaser(
                id = uuidUtils.getUuid(),
                xOffset = ship.xOffset + ship.width / 2 - SHIP_LASER_WIDTH / 2,
                yOffset = -ship.height / 2,
                yRange = screenHeightDp,
                onDestroyLaser = { destroyShipLaser(it) }
            )
            if (ship.tripleLaserBoosterEnabled) {
                listOf(
                    laser.copy(xOffset = laser.xOffset - TRIPLE_LASER_SIDE_OFFSET),
                    laser.copy(xOffset = laser.xOffset),
                    laser.copy(xOffset = laser.xOffset + TRIPLE_LASER_SIDE_OFFSET)
                )
            } else {
                listOf(laser)
            }
        }

        shipLasers = shipLasers + lasers
        updateShipLasersUI()
    }

    val moveShipLasersId = uuidUtils.getUuid()
    fun moveShipLasers() {
        shipLasers.forEach { it.moveLaser() }
        updateShipLasersUI()
    }

    fun hasShipLasers() = shipLasers.isNotEmpty()

    private fun destroyShipLaser(laserId: String) {
        val laser: Laser? = shipLasers.firstOrNull { it.id == laserId }
        laser?.let {
            shipLasers = shipLasers - it
            updateShipLasersUI()
        }
    }

    fun fireUltimateLaser() {
        val ultimateLaserList = mutableListOf<UltimateLaser>()
        val horizontalLaserDistance = screenWidthDp / ULTIMATE_LASERS_COUNT
        for (i in 0..ULTIMATE_LASERS_COUNT) {
            val ultimateLaser = UltimateLaser(
                id = uuidUtils.getUuid(),
                xOffset = horizontalLaserDistance * i,
                yRange = screenHeightDp,
                onDestroyLaser = { destroyUltimateLaser(it) }
            )
            ultimateLaserList.add(ultimateLaser)
        }
        ultimateLasers = ultimateLaserList
        updateUltimateLasersUI()
    }

    val moveUltimateLasersId = uuidUtils.getUuid()
    fun moveUltimateLasers() {
        ultimateLasers.forEach { it.moveLaser() }
        updateUltimateLasersUI()
    }

    fun hasUltimateLasers() = ultimateLasers.isNotEmpty()

    private fun destroyUltimateLaser(laserId: String) {
        ultimateLasers = ultimateLasers.toMutableList().apply {
            removeAll { it.id == laserId }
        }
        updateUltimateLasersUI()
    }

    val monitorLaserCollisionId = uuidUtils.getUuid()
    fun monitorLaserCollision(spaceObjects: List<SpaceObject>, enemies: List<Enemy>) {
        val lasers = shipLasers + ultimateLasers
        val spaceObjectRectList = spaceObjects.map { it.spaceObjectRect() }
        val enemyRectList = enemies.map { it.enemyRect() }
        lasers.forEach { laser ->

            val laserRect = Rect(
                offset = Offset(
                    x = laser.xOffset,
                    y = laser.yOffset + screenHeightDp - 50
                ),
                size = Size(width = laser.width, height = laser.height)
            )

            if (spaceObjectRectList.any { it.overlaps(laserRect) }) {
                val index = spaceObjectRectList.indexOfFirst { it.overlaps(laserRect) }
                spaceObjects[index].onObjectImpact(laser.impactPower)
                destroyShipLaser(laser.id)
                updateShipLasersUI()
            }
            if (enemyRectList.any { it.overlaps(laserRect) }) {
                val index = enemyRectList.indexOfFirst { it.overlaps(laserRect) }
                enemies[index].onObjectImpact(laser.impactPower)
                destroyShipLaser(laser.id)
                updateShipLasersUI()
            }
        }
    }

    private fun updateShipLasersUI() {
        setShipLasers(shipLasers)
    }

    private fun updateUltimateLasersUI() {
        setUltimateLasers(ultimateLasers)
    }

    companion object {
        const val ULTIMATE_LASERS_COUNT = 9
    }
}
package com.zero.neon.game.ship.laser

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.game.enemy.ship.Enemy
import com.zero.neon.game.laser.Laser
import com.zero.neon.game.laser.LaserToLaserUIMapper
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.spaceobject.SpaceObject
import java.util.*

class LasersController(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val setShipLasersUI: (List<LaserUI>) -> Unit,
    private val setUltimateLasersUI: (List<LaserUI>) -> Unit
) {

    private var shipLasers: List<Laser> = emptyList()
    private var ultimateLasers: List<Laser> = emptyList()
    private val mapper = LaserToLaserUIMapper()

    val fireLaserId = UUID.randomUUID().toString()
    fun fireLasers(ship: Ship) {

        val lasers = if (ship.laserBoosterEnabled) {
            val width = 8.dp
            val laser = ShipBoostedLaser(
                xOffset = ship.xOffset + ship.width / 2 - width / 2,
                yOffset = -ship.height / 2,
                yRange = screenHeightDp,
                width = width,
                onDestroyLaser = { destroyShipLaser(it) }
            )
            if (ship.tripleLaserBoosterEnabled) {
                val sideOffset = 20.dp
                arrayOf(
                    laser.copy(xOffset = laser.xOffset - sideOffset),
                    laser.copy(xOffset = laser.xOffset),
                    laser.copy(xOffset = laser.xOffset + sideOffset)
                )
            } else {
                arrayOf(laser)
            }
        } else {
            val width = 5.dp
            val laser = ShipLaser(
                xOffset = ship.xOffset + ship.width / 2 - width / 2,
                yOffset = -ship.height / 2,
                yRange = screenHeightDp,
                width = width,
                onDestroyLaser = { destroyShipLaser(it) }
            )
            if (ship.tripleLaserBoosterEnabled) {
                val sideOffset = 20.dp
                arrayOf(
                    laser.copy(xOffset = laser.xOffset - sideOffset),
                    laser.copy(xOffset = laser.xOffset),
                    laser.copy(xOffset = laser.xOffset + sideOffset)
                )
            } else {
                arrayOf(laser)
            }
        }

        shipLasers = shipLasers + lasers
        updateShipLasersUI()
    }

    val moveShipLasersId = UUID.randomUUID().toString()
    fun moveShipLasers() {
        shipLasers.forEach { it.moveLaser() }
        updateShipLasersUI()
    }

    private fun destroyShipLaser(laserId: String) {
        shipLasers = shipLasers.toMutableList().apply {
            removeAll { it.id == laserId }
        }
        updateShipLasersUI()
    }

    fun fireUltimateLaser() {
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
        updateUltimateLasersUI()
    }

    val moveUltimateLasersId = UUID.randomUUID().toString()
    fun moveUltimateLasers() {
        ultimateLasers.forEach { it.moveLaser() }
        updateUltimateLasersUI()
    }

    private fun destroyUltimateLaser(laserId: String) {
        ultimateLasers = ultimateLasers.toMutableList().apply {
            removeAll { it.id == laserId }
        }
        updateUltimateLasersUI()
    }

    val monitorLaserSpaceObjectHitsId = UUID.randomUUID().toString()
    fun monitorLaserSpaceObjectsHit(spaceObjects: List<SpaceObject>, enemies: List<Enemy>) {
        val lasers = shipLasers + ultimateLasers

        fun laserRect(laser: Laser) = Rect(
            offset = Offset(
                x = laser.xOffset.value,
                y = laser.yOffset.value + screenHeightDp.value - 50
            ),
            size = Size(width = laser.width.value, height = laser.height.value)
        )

        spaceObjects.forEachIndexed { spaceObjectIndex, spaceObject ->
            lasers.forEachIndexed { laserIndex, laser ->
                val spaceObjectRect = Rect(
                    center = Offset(
                        x = spaceObject.xOffset.value + spaceObject.size.value / 2,
                        y = spaceObject.yOffset.value + spaceObject.size.value / 2
                    ),
                    radius = spaceObject.size.value / 2
                )
                if (spaceObject.destroyable && spaceObjectRect.overlaps(laserRect(laser))) {
                    /**
                     * SpaceObject list throws IndexOutOfBoundsException if multiple lasers hit
                     * object fast. TODO Handle it without try-catch block.
                     */
                    try {
                        spaceObjects[spaceObjectIndex].onObjectImpact(laser.impactPower)
                        shipLasers[laserIndex].destroyLaser()
                        updateShipLasersUI()
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }
        }
        enemies.forEachIndexed { enemyIndex, enemy ->
            lasers.forEachIndexed { laserIndex, laser ->
                val enemyRect = Rect(
                    center = Offset(
                        x = enemy.xOffset.value + enemy.width.value / 2,
                        y = enemy.yOffset.value + enemy.height.value / 2
                    ),
                    radius = enemy.width.value / 2
                )
                if (enemyRect.overlaps(laserRect(laser))) {
                    try {
                        enemies[enemyIndex].onObjectImpact(laser.impactPower)
                        shipLasers[laserIndex].destroyLaser()
                        updateShipLasersUI()
                    } catch (e: IndexOutOfBoundsException) {
                    }
                }
            }
        }
    }

    private fun updateShipLasersUI() {
        setShipLasersUI(shipLasers.map { mapper(it) })
    }

    private fun updateUltimateLasersUI() {
        setUltimateLasersUI(ultimateLasers.map { mapper(it) })
    }
}
package com.zero.neon.game.ship.weapons

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.game.spaceobject.SpaceObject
import java.util.*

class LaserManager(
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
        shipLasers = shipLasers
            .filter { it.shooting }
            .toMutableList()
            .apply {
                val laser = ShipLaser(
                    xOffset = ship.xOffset + ship.size / 2,
                    yRange = screenHeightDp,
                    onDestroyLaser = { destroyShipLaser(it) }
                )
                add(laser)
            }
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
    fun monitorLaserSpaceObjectsHit(spaceObjects: List<SpaceObject>) {
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
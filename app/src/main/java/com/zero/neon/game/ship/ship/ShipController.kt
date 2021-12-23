package com.zero.neon.game.ship.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.game.enemy.ship.Enemy
import com.zero.neon.game.laser.Laser
import com.zero.neon.game.spaceobject.BoosterType
import com.zero.neon.game.spaceobject.SpaceObject
import java.util.*

class ShipController(
    private val screenWidthDp: Dp,
    private var ship: Ship,
    private val setShip: (Ship) -> Unit
) {

    private val spaceShipCollidePower = 100
    private val movementSpeed: Dp = 2.dp

    var movingLeft = false
    var movingRight = false

    val moveShipId = UUID.randomUUID().toString()
    fun moveShip() {
        if (movingLeft && ship.xOffset >= 0.dp - ship.width / 4) {
            updateXOffset(ship.xOffset - movementSpeed)
        } else movingLeft = false
        if (movingRight && ship.xOffset <= screenWidthDp - (ship.width.value / 1.5).dp) {
            updateXOffset(ship.xOffset + movementSpeed)
        } else movingRight = false
    }

    private var shieldBoosterStartMillis: Long = 0
    private val shieldBoosterTimeMillis: Long = 10000
    private var shieldEndDurationMillis: Long = 0
    private fun enableShield(enable: Boolean) {
        updateShieldEnabled(enable)
        if (enable) {
            shieldBoosterStartMillis = System.currentTimeMillis()
            shieldEndDurationMillis = shieldBoosterStartMillis + shieldBoosterTimeMillis
        }
    }

    private var laserBoosterStartMillis: Long = 0
    private val laserBoosterTimeMillis: Long = 15000
    private var laserBoosterEndDurationMillis: Long = 0
    private fun enableLaserBooster(enable: Boolean) {
        updateLaserBoosterEnabled(enable)
        if (enable) {
            laserBoosterStartMillis = System.currentTimeMillis()
            laserBoosterEndDurationMillis = laserBoosterStartMillis + laserBoosterTimeMillis
        }
    }

    private var tripleLaserBoosterStartMillis: Long = 0
    private val tripleLaserBoosterTimeMillis: Long = 20000
    private var tripleLaserBoosterEndDurationMillis: Long = 0
    private fun enableTripleLaserBooster(enable: Boolean) {
        updateTripleLaserBoosterEnabled(enable)
        if (enable) {
            tripleLaserBoosterStartMillis = System.currentTimeMillis()
            tripleLaserBoosterEndDurationMillis =
                tripleLaserBoosterStartMillis + tripleLaserBoosterTimeMillis
        }
    }

    val monitorShipCollisionsId = UUID.randomUUID().toString()
    fun monitorShipCollisions(
        spaceObjects: List<SpaceObject>,
        enemies: List<Enemy>,
        enemyLasers: List<Laser>,
        fileUltimateLaser: () -> Unit
    ) {
        val shipRect by lazy {
            Rect(
                offset = Offset(x = ship.xOffset.value, y = ship.yOffset.value),
                size = Size(width = ship.width.value, height = ship.height.value)
            )
        }
        val shipShieldRect by lazy {
            val shipRadius = ship.width.value / 2
            Rect(
                center = Offset(
                    x = ship.xOffset.value + shipRadius,
                    y = ship.yOffset.value + shipRadius
                ),
                radius = shipRadius
            )
        }

        spaceObjects.forEachIndexed { spaceObjectIndex, spaceObject ->
            val spaceRect by lazy {
                Rect(
                    offset = Offset(x = spaceObject.xOffset.value, y = spaceObject.yOffset.value),
                    size = Size(width = spaceObject.size.value, height = spaceObject.size.value)
                )
            }
            if (spaceRect.overlaps(if (ship.shieldEnabled) shipShieldRect else shipRect)) {
                spaceObjects[spaceObjectIndex].onObjectImpact(spaceShipCollidePower)

                val hpImpact = when (ship.shieldEnabled && spaceObject.impactPower > 0) {
                    true -> 0
                    false -> spaceObject.impactPower
                }
                updateHp(ship.hp - hpImpact)

                when (spaceObject.drawableId) {
                    BoosterType.ULTIMATE_WEAPON_BOOSTER.drawableId -> fileUltimateLaser()
                    BoosterType.SHIELD_BOOSTER.drawableId -> enableShield(enable = true)
                    BoosterType.LASER_BOOSTER.drawableId -> enableLaserBooster(enable = true)
                    BoosterType.TRIPLE_LASER_BOOSTER.drawableId -> enableTripleLaserBooster(enable = true)
                }
            }
        }
        enemies.forEachIndexed { enemyIndex, enemy ->
            val enemyRect by lazy {
                Rect(
                    offset = Offset(x = enemy.xOffset.value, y = enemy.yOffset.value),
                    size = Size(width = enemy.width.value, height = enemy.height.value)
                )
            }
            if (enemyRect.overlaps(if (ship.shieldEnabled) shipShieldRect else shipRect)) {
                enemies[enemyIndex].onObjectImpact(spaceShipCollidePower)

                val hpImpact = when (ship.shieldEnabled && enemy.impactPower > 0) {
                    true -> 0
                    false -> enemy.impactPower
                }
                updateHp(ship.hp - hpImpact)
            }
        }
        enemyLasers.forEachIndexed { enemyIndex, enemyLaser ->
            val enemyLaserRect by lazy {
                Rect(
                    offset = Offset(x = enemyLaser.xOffset.value, y = enemyLaser.yOffset.value),
                    size = Size(width = enemyLaser.width.value, height = enemyLaser.height.value)
                )
            }
            if (enemyLaserRect.overlaps(if (ship.shieldEnabled) shipShieldRect else shipRect)) {
                enemyLasers[enemyIndex].destroyLaser()

                val hpImpact = when (ship.shieldEnabled && enemyLaser.impactPower > 0) {
                    true -> 0
                    false -> enemyLaser.impactPower
                }
                updateHp(ship.hp - hpImpact)
            }
        }

        val currentTime = System.currentTimeMillis()
        if (shieldEndDurationMillis < currentTime) enableShield(enable = false)
        if (laserBoosterEndDurationMillis < currentTime) enableLaserBooster(enable = false)
        if (tripleLaserBoosterEndDurationMillis < currentTime) enableTripleLaserBooster(enable = false)
    }

    private fun updateShieldEnabled(enable: Boolean) {
        ship = ship.copy(shieldEnabled = enable)
        setShip(ship)
    }

    private fun updateLaserBoosterEnabled(enable: Boolean) {
        ship = ship.copy(
            laserBoosterEnabled = enable,
            drawableId = if (enable) R.drawable.ship_boosted_laser else R.drawable.ship_regular_laser
        )
        setShip(ship)
    }

    private fun updateTripleLaserBoosterEnabled(enable: Boolean) {
        ship = ship.copy(
            tripleLaserBoosterEnabled = enable,
            drawableId = R.drawable.ship_regular_laser
        )
        setShip(ship)
    }

    private fun updateXOffset(xOffset: Dp) {
        ship = ship.copy(xOffset = xOffset)
        setShip(ship)
    }

    private fun updateHp(hp: Int) {
        ship = ship.copy(hp = hp)
        setShip(ship)
    }
}
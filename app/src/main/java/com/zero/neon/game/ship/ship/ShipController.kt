package com.zero.neon.game.ship.ship

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import com.zero.neon.R
import com.zero.neon.game.booster.Booster
import com.zero.neon.game.booster.BoosterType
import com.zero.neon.game.common.Millis
import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.laser.Laser
import com.zero.neon.game.spaceobject.SpaceObject
import java.util.*

class ShipController(
    private val screenWidth: Float,
    screenHeight: Float,
    private var ship: Ship,
    private val setShip: (Ship) -> Unit
) {

    private val spaceShipCollidePower: Float = 100f
    private val movementSpeed: Float = 2f
    private val maxYOffset: Float = screenHeight - 140

    var movingLeft = false
    var movingRight = false

    val moveShipId = UUID.randomUUID().toString()
    val moveShipRepeatTime = Millis(3)
    fun moveShip() {
        if (ship.yOffset > maxYOffset) {
            updateYOffset(ship.yOffset - movementSpeed)
        }
        if (movingLeft && ship.xOffset >= 0 - ship.width / 4) {
            updateXOffset(ship.xOffset - movementSpeed)
        } else movingLeft = false
        if (movingRight && ship.xOffset <= screenWidth - ship.width / 1.5) {
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
    val monitorShipCollisionsRepeatTime = Millis(100)
    fun monitorShipCollisions(
        spaceObjects: List<SpaceObject>,
        boosters: List<Booster>,
        enemies: List<Enemy>,
        enemyLasers: List<Laser>,
        fileUltimateLaser: () -> Unit
    ) {
        val shipRect by lazy {
            Rect(
                offset = Offset(x = ship.xOffset, y = ship.yOffset),
                size = Size(width = ship.width, height = ship.height)
            )
        }
        val shipShieldRect by lazy {
            val shipRadius = ship.width / 2
            Rect(
                center = Offset(
                    x = ship.xOffset + shipRadius,
                    y = ship.yOffset + shipRadius
                ),
                radius = shipRadius
            )
        }

        spaceObjects.forEachIndexed { spaceObjectIndex, spaceObject ->
            val spaceRect by lazy {
                Rect(
                    offset = Offset(x = spaceObject.xOffset, y = spaceObject.yOffset),
                    size = Size(width = spaceObject.size, height = spaceObject.size)
                )
            }
            if (spaceRect.overlaps(if (ship.shieldEnabled) shipShieldRect else shipRect)) {
                spaceObjects[spaceObjectIndex].onObjectImpact(spaceShipCollidePower)

                val hpImpact: Int = when (ship.shieldEnabled && spaceObject.impactPower > 0) {
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

        boosters.forEachIndexed { boosterIndex, booster ->
            val boosterRect by lazy {
                Rect(
                    offset = Offset(x = booster.xOffset, y = booster.yOffset),
                    size = Size(width = booster.size, height = booster.size)
                )
            }
            if (boosterRect.overlaps(shipRect)) {
                boosters[boosterIndex].collect()
                updateHp(ship.hp - booster.impactPower)
                when (booster.type) {
                    BoosterType.ULTIMATE_WEAPON_BOOSTER -> fileUltimateLaser()
                    BoosterType.SHIELD_BOOSTER -> enableShield(enable = true)
                    BoosterType.LASER_BOOSTER -> enableLaserBooster(enable = true)
                    BoosterType.TRIPLE_LASER_BOOSTER -> enableTripleLaserBooster(enable = true)
                }
            }
        }
        enemies.forEachIndexed { enemyIndex, enemy ->
            val enemyRect by lazy {
                Rect(
                    offset = Offset(x = enemy.xOffset, y = enemy.yOffset),
                    size = Size(width = enemy.width, height = enemy.height)
                )
            }
            if (enemyRect.overlaps(if (ship.shieldEnabled) shipShieldRect else shipRect)) {
                enemies[enemyIndex].onObjectImpact(spaceShipCollidePower)

                val hpImpact: Int = when (ship.shieldEnabled && enemy.impactPower > 0) {
                    true -> 0
                    false -> enemy.impactPower.toInt()
                }
                updateHp(ship.hp - hpImpact)
            }
        }
        enemyLasers.forEachIndexed { enemyIndex, enemyLaser ->
            val enemyLaserRect by lazy {
                Rect(
                    offset = Offset(x = enemyLaser.xOffset, y = enemyLaser.yOffset),
                    size = Size(width = enemyLaser.width, height = enemyLaser.height)
                )
            }
            if (enemyLaserRect.overlaps(if (ship.shieldEnabled) shipShieldRect else shipRect)) {
                enemyLasers[enemyIndex].destroyed = true

                val hpImpact: Float = when (ship.shieldEnabled && enemyLaser.impactPower > 0) {
                    true -> 0f
                    false -> enemyLaser.impactPower
                }
                updateHp((ship.hp - hpImpact).toInt())
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

    private fun updateXOffset(xOffset: Float) {
        ship = ship.copy(xOffset = xOffset)
        setShip(ship)
    }

    private fun updateYOffset(yOffset: Float) {
        ship = ship.copy(yOffset = yOffset)
        setShip(ship)
    }

    private fun updateHp(hp: Int) {
        ship = ship.copy(hp = hp)
        setShip(ship)
    }

    companion object {
        const val TRIPLE_LASER_SIDE_OFFSET: Float = 20f
    }
}
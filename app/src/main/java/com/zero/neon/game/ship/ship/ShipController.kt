package com.zero.neon.game.ship.ship

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.game.spaceobject.BoosterType
import com.zero.neon.game.spaceobject.SpaceObject
import java.util.*

class ShipController(private val screenWidthDp: Dp, screenHeightDp: Dp) {

    private val width: Dp = 85.dp
    private val height: Dp = 90.dp
    private val shieldSize: Dp = height * 2
    private var shieldEnabled: Boolean by mutableStateOf(false)
    private var laserBoosterEnabled: Boolean by mutableStateOf(false)
    private var xOffset: Dp by mutableStateOf(screenWidthDp / 2 - width / 2)
    private val yOffset: Dp = screenHeightDp - 140.dp
    private var hp: Int by mutableStateOf(1000)
    val ship by derivedStateOf {
        Ship(
            width = width,
            height = height,
            shieldSize = shieldSize,
            shieldEnabled = shieldEnabled,
            laserBoosterEnabled = laserBoosterEnabled,
            xOffset = xOffset,
            yOffset = yOffset,
            hp = hp,
            drawableId = if (laserBoosterEnabled) R.drawable.ship_boosted_laser else R.drawable.ship_regular_laser
        )
    }

    private val spaceShipCollidePower = 100
    private val movementSpeed: Dp = 2.dp

    private var movingLeft = false
    private var movingRight = false

    fun moveShipLeft(movingLeft: Boolean) {
        this.movingLeft = movingLeft
    }

    fun moveShipRight(movingRight: Boolean) {
        this.movingRight = movingRight
    }

    val moveShipId = UUID.randomUUID().toString()
    fun moveShip() {
        if (movingLeft && xOffset >= 0.dp - width / 4) {
            xOffset -= movementSpeed
        } else movingLeft = false
        if (movingRight && xOffset <= screenWidthDp - (width.value / 1.5).dp) {
            xOffset += movementSpeed
        } else movingRight = false
    }

    private var shieldBoosterStartMillis: Long = 0
    private val shieldBoosterTimeMillis: Long = 10000
    private var shieldEndDurationMillis: Long = 0
    private fun enableShield(enable: Boolean) {
        shieldEnabled = enable
        if (enable) {
            shieldBoosterStartMillis = System.currentTimeMillis()
            shieldEndDurationMillis = shieldBoosterStartMillis + shieldBoosterTimeMillis
        }
    }

    private var laserBoosterStartMillis: Long = 0
    private val laserBoosterTimeMillis: Long = 15000
    private var laserBoosterEndDurationMillis: Long = 0
    private fun enableLaserBooster(enable: Boolean) {
        laserBoosterEnabled = enable
        if (enable) {
            laserBoosterStartMillis = System.currentTimeMillis()
            laserBoosterEndDurationMillis = laserBoosterStartMillis + laserBoosterTimeMillis
        }
    }

    val monitorShipSpaceObjectsCollisionId = UUID.randomUUID().toString()
    fun monitorShipSpaceObjectsCollision(
        spaceObjects: List<SpaceObject>,
        fileUltimateLaser: () -> Unit
    ) {
        val shipRect by lazy {
            Rect(
                offset = Offset(x = xOffset.value, y = yOffset.value),
                size = Size(width = width.value, height = height.value)
            )
        }
        val shipShieldRect by lazy {
            val shipRadius = width.value / 2
            Rect(
                center = Offset(
                    x = xOffset.value + shipRadius,
                    y = yOffset.value + shipRadius
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
            if (spaceRect.overlaps(if (shieldEnabled) shipShieldRect else shipRect)) {
                spaceObjects[spaceObjectIndex].onObjectImpact(spaceShipCollidePower)

                hp -= if (shieldEnabled && spaceObject.impactPower > 0) 0 else spaceObject.impactPower

                when (spaceObject.drawableId) {
                    BoosterType.ULTIMATE_WEAPON_BOOSTER.drawableId -> fileUltimateLaser()
                    BoosterType.SHIELD_BOOSTER.drawableId -> enableShield(enable = true)
                    BoosterType.LASER_BOOSTER.drawableId -> enableLaserBooster(enable = true)
                }
            }
        }

        val currentTime = System.currentTimeMillis()
        if (shieldEndDurationMillis < currentTime) enableShield(enable = false)
        if (laserBoosterEndDurationMillis < currentTime) enableLaserBooster(enable = false)
    }
}
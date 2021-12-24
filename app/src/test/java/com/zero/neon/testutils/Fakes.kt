package com.zero.neon.testutils

import androidx.compose.ui.unit.dp
import com.zero.neon.game.ship.laser.ShipBoostedLaser
import com.zero.neon.game.ship.laser.ShipBoostedLaser.Companion.SHIP_BOOSTED_LASER_WIDTH
import com.zero.neon.game.ship.laser.ShipLaser
import com.zero.neon.game.ship.laser.ShipLaser.Companion.SHIP_LASER_WIDTH
import com.zero.neon.game.ship.ship.Ship

val FAKE_SCREEN_WIDTH_DP = 500.dp
val FAKE_SCREEN_HEIGHT_DP = 550.dp
val FAKE_SHIP = Ship(xOffset = 0.dp, yOffset = 0.dp)
val FAKE_SHIP_LASER = with(FAKE_SHIP) {
    ShipLaser(
        id = FAKE_UUID,
        xOffset = xOffset + width / 2 - SHIP_LASER_WIDTH / 2,
        yOffset = -height / 2,
        yRange = FAKE_SCREEN_HEIGHT_DP,
        width = SHIP_LASER_WIDTH,
        onDestroyLaser = {}
    )
}
val FAKE_SHIP_BOOSTED_LASER = with(FAKE_SHIP) {
    ShipBoostedLaser(
        id = FAKE_UUID,
        xOffset = xOffset + width / 2 - SHIP_BOOSTED_LASER_WIDTH / 2,
        yOffset = -height / 2,
        yRange = FAKE_SCREEN_HEIGHT_DP,
        width = SHIP_BOOSTED_LASER_WIDTH,
        onDestroyLaser = {}
    )
}
const val FAKE_UUID = "a"
val FAKE_TIME_MILLIS = System.currentTimeMillis()
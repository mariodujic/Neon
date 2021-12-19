package com.zero.neon.game.enemy.laser

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.game.enemy.ship.Enemy
import com.zero.neon.game.laser.LaserToLaserUIMapper
import com.zero.neon.game.ship.laser.LaserUI
import java.util.*

class EnemyLasersController(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val setShipLasersUI: (List<LaserUI>) -> Unit
) {

    var enemyLasers: List<EnemyLaser> = emptyList()
        private set
    private val mapper = LaserToLaserUIMapper()

    val fireEnemyLaserId = UUID.randomUUID().toString()
    fun fireEnemyLasers(enemies: List<Enemy>) {
        if (enemies.isEmpty()) return
        val enemy = enemies.random()
        val laserWidth = 18.dp
        val laser = EnemyLaser(
            xOffset = enemy.xOffset + enemy.width / 2 - laserWidth / 2,
            yOffset = enemy.yOffset + enemy.height,
            yRange = screenHeightDp,
            width = laserWidth,
            onDestroyLaser = { destroyEnemyLaser(it) }
        )

        enemyLasers = enemyLasers
            .toMutableList()
            .apply {
                add(laser)
            }
        updateShipLasersUI()
    }

    val moveEnemyLasersId = UUID.randomUUID().toString()
    fun moveEnemyLasers() {
        enemyLasers.forEach { it.moveLaser() }
        updateShipLasersUI()
    }

    private fun destroyEnemyLaser(laserId: String) {
        enemyLasers = enemyLasers.toMutableList().apply {
            removeAll { it.id == laserId }
        }
        updateShipLasersUI()
    }

    private fun updateShipLasersUI() {
        setShipLasersUI(enemyLasers.map { mapper(it) })
    }
}
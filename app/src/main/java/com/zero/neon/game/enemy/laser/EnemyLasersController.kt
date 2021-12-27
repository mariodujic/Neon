package com.zero.neon.game.enemy.laser

import com.zero.neon.game.enemy.ship.Enemy
import java.util.*

class EnemyLasersController(
    private val screenHeightDp: Float,
    initialEnemyLasers: List<EnemyLaser>,
    private val setEnemyLasers: (List<EnemyLaser>) -> Unit
) {

    var enemyLasers: List<EnemyLaser> = initialEnemyLasers
        private set

    val fireEnemyLaserId = UUID.randomUUID().toString()
    fun fireEnemyLasers(enemies: List<Enemy>) {
        if (enemies.isEmpty()) return
        val enemy = enemies.random()
        val laserWidth = 18f
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

    fun hasEnemyLasers() = enemyLasers.isNotEmpty()

    private fun destroyEnemyLaser(laserId: String) {
        enemyLasers = enemyLasers.toMutableList().apply {
            removeAll { it.id == laserId }
        }
        updateShipLasersUI()
    }

    private fun updateShipLasersUI() {
        setEnemyLasers(enemyLasers)
    }
}
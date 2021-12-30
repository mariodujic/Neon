package com.zero.neon.game.enemy.laser

import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.laser.Laser
import java.util.*

class EnemyLasersController(
    private val screenHeight: Float,
    initialEnemyLasers: List<Laser>,
    private val setEnemyLasers: (List<Laser>) -> Unit
) {

    var enemyLasers: List<Laser> = initialEnemyLasers
        private set

    val fireEnemyLaserId = UUID.randomUUID().toString()
    fun fireEnemyLasers(enemies: List<Enemy>) {
        if (enemies.isEmpty()) return
        val enemy = enemies.random()
        val laser = enemy.generateLaser()
        enemyLasers = enemyLasers + laser
        updateShipLasers()
    }

    val processLasersId = UUID.randomUUID().toString()
    fun processLasers() {
        enemyLasers.forEach {
            it.moveLaser()
            if (it.yOffset > screenHeight || it.destroyed) destroyEnemyLaser(it)
        }
        updateShipLasers()
    }

    fun hasEnemyLasers() = enemyLasers.isNotEmpty()

    private fun destroyEnemyLaser(laser: Laser) {
        enemyLasers = enemyLasers - laser
    }

    private fun updateShipLasers() {
        setEnemyLasers(enemyLasers)
    }
}
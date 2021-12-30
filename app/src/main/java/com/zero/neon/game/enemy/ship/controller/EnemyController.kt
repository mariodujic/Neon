package com.zero.neon.game.enemy.ship.controller

import com.zero.neon.game.enemy.ship.factory.EnemyFactory
import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.enemy.ship.model.EnemyType
import com.zero.neon.game.ship.ship.Ship
import java.util.*

class EnemyController(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val enemyFactory: EnemyFactory = EnemyFactory(screenWidth, screenHeight),
    private val getShip: () -> Ship,
    initialEnemies: List<Enemy> = emptyList(),
    private val setEnemies: (List<Enemy>) -> Unit
) {

    var enemies: List<Enemy> = initialEnemies
        private set

    val addEnemyId = UUID.randomUUID().toString()
    fun addEnemy(type: EnemyType) {
        val enemies = enemyFactory(type = type, getShip = getShip)
        this.enemies += enemies
    }

    val processEnemiesId = UUID.randomUUID().toString()
    fun processEnemies() {
        enemies.forEach { it.move() }
        enemies = enemies.toMutableList().apply { removeAll { it.hp <= 0 } }
        updateEnemies()
    }

    fun hasEnemies() = enemies.isNotEmpty()

    private fun updateEnemies() {
        setEnemies(enemies)
    }
}
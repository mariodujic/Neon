package com.zero.neon.game.enemy.ship.controller

import com.zero.neon.game.enemy.ship.factory.EnemyFactory
import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.enemy.ship.model.EnemyType
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.utils.UuidUtils
import java.util.*

class EnemyController(
    private val screenWidth: Float,
    private val screenHeight: Float,
    uuidUtils: UuidUtils = UuidUtils(),
    private val enemyFactory: EnemyFactory = EnemyFactory(screenWidth, screenHeight),
    private val getShip: () -> Ship,
    initialEnemies: List<Enemy> = emptyList(),
    private val setEnemies: (List<Enemy>) -> Unit
) {

    private var enemies: List<Enemy> = initialEnemies

    val addEnemyId = uuidUtils.getUuid()
    val addBossId = uuidUtils.getUuid()
    fun addEnemy(type: EnemyType) {
        val enemies = enemyFactory(type = type, getShip = getShip)
        this.enemies += enemies
        updateEnemies()
    }

    val processEnemiesId = uuidUtils.getUuid()
    fun processEnemies() {
        enemies.forEach {
            if (it.hp <= 0) enemies -= it
            it.move()
        }
        updateEnemies()
    }

    fun hasEnemies() = enemies.isNotEmpty()

    private fun updateEnemies() {
        setEnemies(enemies)
    }
}
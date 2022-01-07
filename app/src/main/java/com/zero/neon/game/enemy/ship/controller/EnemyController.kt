package com.zero.neon.game.enemy.ship.controller

import com.zero.neon.game.common.Millis
import com.zero.neon.game.enemy.ship.factory.EnemyFactory
import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.enemy.ship.model.EnemyType
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.utils.UuidUtils

class EnemyController(
    private val screenWidth: Float,
    private val screenHeight: Float,
    uuidUtils: UuidUtils,
    private val enemyFactory: EnemyFactory = EnemyFactory(screenWidth, screenHeight),
    private val getShip: () -> Ship,
    initialEnemies: List<Enemy> = emptyList(),
    private val setEnemies: (List<Enemy>) -> Unit,
    private val addMinerals: (xOffset: Float, yOffset: Float, width: Float, mineralAmount: Int) -> Unit,
    private val addExplosion: (xOffset: Float, yOffset: Float, width: Float, height: Float) -> Unit
) {

    private var enemies: List<Enemy> = initialEnemies

    val addEnemyId = uuidUtils.getUuid()
    fun addEnemy(type: EnemyType) {
        val enemies = enemyFactory(type = type, getShip = getShip)
        this.enemies += enemies
        updateEnemies()
    }

    val processEnemiesId = uuidUtils.getUuid()
    val processEnemiesRepeatTime = Millis(5)
    fun processEnemies() {
        enemies.forEach {
            it.process()
            if (it.destroyed) {
                enemies -= it
                addMinerals(
                    it.xOffset,
                    it.yOffset + it.height / 2,
                    it.width,
                    it.minerals
                )
                addExplosion(
                    it.xOffset + it.width / 2,
                    it.yOffset + it.height / 2,
                    it.width,
                    it.height
                )
            } else if (it.outOfScreen) {
                enemies -= it
            }
        }
        updateEnemies()
    }

    fun hasEnemies() = enemies.isNotEmpty()

    private fun updateEnemies() {
        setEnemies(enemies)
    }
}
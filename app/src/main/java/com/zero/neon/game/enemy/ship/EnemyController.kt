package com.zero.neon.game.enemy.ship

import com.zero.neon.game.ship.ship.Ship
import java.util.*

class EnemyController(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    private val getShip: () -> Ship,
    initialEnemies: List<Enemy> = emptyList(),
    private val setEnemies: (List<Enemy>) -> Unit
) {

    var enemies: List<Enemy> = initialEnemies
        private set

    val addEnemyId = UUID.randomUUID().toString()
    fun addEnemy(enemyType: EnemyType) {
        val enemy: Enemy? = if (enemyType is LevelOneEnemyType) {
            LevelOneEnemy(
                screenWidthDp = screenWidthDp,
                screenHeightDp = screenHeightDp,
                type = enemyType
            )
        } else if (enemyType is LevelOneBossType && enemies.isEmpty()) {
            LevelOneBoss(
                screenWidthDp = screenWidthDp,
                screenHeightDp = screenHeightDp,
                getShip = getShip
            )
        } else null
        enemy?.let {
            enemies = enemies + it
            updateEnemies()
        }
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
package com.zero.neon.game.enemy.ship

import java.util.*

class EnemyController(
    private val screenWidthDp: Float,
    private val screenHeightDp: Float,
    initialEnemies: List<Enemy> = emptyList(),
    private val setEnemies: (List<Enemy>) -> Unit
) {

    var enemies: List<Enemy> = initialEnemies
        private set

    val addEnemyId = UUID.randomUUID().toString()
    fun addEnemy(levelOneEnemyAttributes: LevelOneEnemyAttributes) {
        val enemy = LevelOneEnemy(
            screenWidthDp = screenWidthDp,
            screenHeightDp = screenHeightDp,
            attributes = levelOneEnemyAttributes
        )
        enemies = enemies.toMutableList().apply {
            add(enemy)
        }
        updateEnemies()
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
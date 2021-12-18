package com.zero.neon.game.enemy

import androidx.compose.ui.unit.Dp
import java.util.*

class EnemyController(
    private val screenWidthDp: Dp,
    private val screenHeightDp: Dp,
    private val setEnemies: (List<EnemyUI>) -> Unit
) {

    var enemies: List<Enemy> = listOf()
        private set
    private val mapper = EnemyToEnemyUIMapper()

    val addEnemyId = UUID.randomUUID().toString()
    fun addEnemy() {
        val enemy = RegularEnemy(
            screenWidthDp = screenWidthDp,
            screenHeightDp = screenHeightDp,
            onDestroyEnemy = { onDestroyEnemy(it) })
        enemies = enemies.toMutableList().apply {
            add(enemy)
        }
        updateEnemies()
    }

    val moveEnemiesId = UUID.randomUUID().toString()
    fun moveEnemies() {
        enemies.forEach { it.move() }
        updateEnemies()
    }

    private fun onDestroyEnemy(enemyId: String) {
        enemies = enemies.toMutableList().apply {
            removeAll { it.enemyId == enemyId }
        }
        updateEnemies()
    }

    private fun updateEnemies() {
        setEnemies(enemies.map { mapper(it) })
    }
}
package com.zero.neon.game.enemy.ship

import com.zero.neon.game.ship.ship.Ship
import java.util.*

class EnemyController(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val formationXOffset: FormationXOffset = FormationXOffset(screenWidth),
    private val getShip: () -> Ship,
    initialEnemies: List<Enemy> = emptyList(),
    private val setEnemies: (List<Enemy>) -> Unit
) {

    var enemies: List<Enemy> = initialEnemies
        private set

    val addEnemyId = UUID.randomUUID().toString()
    fun addEnemy(type: EnemyType) {
        val enemies: MutableList<Enemy> = mutableListOf()
        if (type is LevelOneEnemyType) {
            when (type.formation) {
                is ZigZag -> {
                    val enemy = RegularEnemy(
                        screenWidth = screenWidth,
                        screenHeight = screenHeight,
                        xOffset = formationXOffset.zigZagXOffset(type.formation),
                        type = type
                    )
                    enemies += enemy
                }
                is Row -> {
                    for (i in 1..type.formation.rowCount) {
                        val enemy = RegularEnemy(
                            screenWidth = screenWidth,
                            screenHeight = screenHeight,
                            xOffset = formationXOffset.rectangleXOffset(
                                formation = type.formation,
                                previousEnemy = enemies.lastOrNull(),
                                enemyWidth = type.width
                            ),
                            type = type
                        )
                        enemies += enemy
                    }
                }
            }
        } else if (type is LevelOneBossType && enemies.isEmpty()) {
            val boss = Boss(
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                getShip = getShip
            )
            enemies += boss
        }
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
package com.zero.neon.game.enemy.ship.factory

import com.zero.neon.game.enemy.ship.model.*
import com.zero.neon.game.ship.ship.Ship

class EnemyFactory(
    private val screenWidth: Float,
    private val screenHeight: Float,
    private val formationXOffset: FormationXOffset = FormationXOffset(screenWidth)
) {

    operator fun invoke(type: EnemyType, getShip: () -> Ship): List<Enemy> {
        val enemies: MutableList<Enemy> = mutableListOf()
        if (type is RegularEnemyType) {
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
        return enemies
    }
}
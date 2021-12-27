package com.zero.neon.game.enemy.ship

class EnemyToEnemyUIMapper {

    operator fun invoke(enemy: Enemy): EnemyUI {
        return with(enemy) {
            EnemyUI(
                enemyId = enemyId,
                width = width,
                height = height,
                xOffset = xOffset,
                yOffset = yOffset,
                hpBarWidth = width / initialHp * hp
            )
        }
    }
}
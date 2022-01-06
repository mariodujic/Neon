package com.zero.neon.game.mineral.mapper

import com.zero.neon.game.mineral.model.Mineral
import com.zero.neon.game.mineral.model.MineralUI

class MineralToMineralUIMapper {

    operator fun invoke(mineral: Mineral): MineralUI {
        return with(mineral) {
            MineralUI(
                xOffset = xOffset,
                yOffset = yOffset,
                width = width,
                alpha = alpha
            )
        }
    }
}
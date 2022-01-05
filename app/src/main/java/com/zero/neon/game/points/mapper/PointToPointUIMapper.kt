package com.zero.neon.game.points.mapper

import com.zero.neon.game.points.model.Point
import com.zero.neon.game.points.model.PointUI

class PointToPointUIMapper {

    operator fun invoke(point: Point): PointUI {
        return with(point) {
            PointUI(
                xOffset = xOffset,
                yOffset = yOffset,
                width = width,
                value = value,
                alpha = alpha
            )
        }
    }
}
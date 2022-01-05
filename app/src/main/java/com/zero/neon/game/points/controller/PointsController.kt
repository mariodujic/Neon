package com.zero.neon.game.points.controller

import com.zero.neon.game.common.Millis
import com.zero.neon.game.points.model.Point
import java.util.*

class PointsController(
    initialPoints: List<Point>,
    private val updatePoints: (List<Point>) -> Unit,
    private val updateMineralsEarnedTotal: (Int) -> Unit
) {

    private var points: List<Point> = initialPoints

    fun addPoint(xOffset: Float, yOffset: Float, width: Float, value: Int) {
        val point = Point(
            xOffset = xOffset,
            yOffset = yOffset,
            width = width,
            value = value
        )
        points += point
        updatePoints(points)
        updateMineralsEarnedTotal(value)
    }

    val processPointsId = UUID.randomUUID().toString()
    val processPointsRepeatTime = Millis(5)
    fun processPoints() {
        points.forEach {
            it.processPoint()
            if (it.removed) points -= it
        }
        updatePoints(points)
    }
}
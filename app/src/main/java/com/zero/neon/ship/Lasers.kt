package com.zero.neon.ship

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class ShipLaser(
    override var xOffset: Dp,
    private val yRange: Dp,
    private val screenWidth: Dp,
    private val screenHeight: Dp,
    private val onDestroyLaser: (laserId: String) -> Unit,
    coroutineScope: CoroutineScope
) : Laser {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset: Dp by mutableStateOf((-10).dp)
    override var shooting: Boolean by mutableStateOf(false)
    val offset by derivedStateOf {
        Offset(
            x = xOffset.value + (screenWidth / 2).value,
            y = yOffset.value + screenHeight.value
        )
    }

    fun moveLaser() {
        shooting = true
        if (yOffset > -yRange || !shooting) {
            yOffset -= 7.dp
        } else {
            shooting = false
        }
    }

    override fun destroyLaser() {
        shooting = false
        onDestroyLaser(id)
    }
}

interface Laser {
    val id: String
    var xOffset: Dp
    var yOffset: Dp
    var shooting: Boolean

    fun destroyLaser()
}
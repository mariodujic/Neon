package com.zero.neon.spaceobject

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class Rock(
    override var xOffset: Dp,
    override var size: Dp,
    screenHeight: Dp,
    coroutineScope: CoroutineScope,
    private val onDestroyRock: (rockId: String) -> Unit
) : SpaceObject {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset by mutableStateOf(1.dp)
    var floating by mutableStateOf(false)
    val rockRect by derivedStateOf {
        Rect(
            offset = Offset(x = xOffset.value, y = yOffset.value),
            size = Size(width = size.value, height = size.value)
        )
    }

    init {
        coroutineScope.launch {
            floating = true
            while (yOffset < screenHeight + 100.dp && floating) {
                yOffset += 1.dp
                delay(5)
            }
            floating = false
        }
    }

    fun destroyRock() {
        floating = false
        onDestroyRock(id)
    }
}

interface SpaceObject {
    val id: String
    var xOffset: Dp
    var yOffset: Dp
    var size: Dp
}
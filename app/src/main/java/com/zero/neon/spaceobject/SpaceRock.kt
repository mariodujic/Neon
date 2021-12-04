package com.zero.neon.spaceobject

import androidx.annotation.DrawableRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class SpaceRock(
    override var xOffset: Dp,
    override var size: Dp,
    screenHeight: Dp,
    coroutineScope: CoroutineScope,
    private val onDestroyRock: (rockId: String) -> Unit
) : SpaceObject {

    override val id: String = UUID.randomUUID().toString()
    override var yOffset by mutableStateOf(1.dp)
    private val randomDrawableIndex = Random.nextInt(0, RockType.values().size)
    override val drawableId: Int = RockType.values().get(randomDrawableIndex).drawableId
    var floating by mutableStateOf(false)
    val rockRect by derivedStateOf {
        Rect(
            offset = Offset(x = xOffset.value, y = yOffset.value),
            size = Size(width = size.value, height = size.value)
        )
    }

    init {
        coroutineScope.launch(Dispatchers.IO) {
            floating = true
            while (yOffset < screenHeight + 100.dp && floating) {
                yOffset += 1.dp
                delay(5)
            }
            floating = false
            destroyRock()
        }
    }

    fun destroyRock() {
        floating = false
        onDestroyRock(id)
    }

    enum class RockType(@DrawableRes val drawableId: Int) {
        ROCK_ONE(R.drawable.space_rock_1),
        ROCK_TWO(R.drawable.space_rock_2),
        ROCK_THREE(R.drawable.space_rock_3),
        ROCK_FOUR(R.drawable.space_rock_4)
    }
}
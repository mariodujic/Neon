package com.zero.neon.constellation

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Star(var xOffset: Dp, var yOffset: Dp, var size: Dp, coroutineScope: CoroutineScope) {

    private val initialStarSize = size
    private var enlargeStar = false

    init {
        coroutineScope.launch {
            while (true) {
                if (enlargeStar) {
                    if (size == initialStarSize) {
                        enlargeStar = false
                    } else {
                        size += 1.dp
                    }
                } else {
                    if (size == 1.dp) {
                        enlargeStar = true
                    } else {
                        size -= 1.dp
                    }
                }
                delay(150)
            }
        }
    }
}
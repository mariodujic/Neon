package com.zero.neon.game.controls

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.zero.neon.R

@Composable
fun MovementButtons(
    onMoveLeft: (Boolean) -> Unit,
    onMoveRight: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {

    val buttonSize = dimensionResource(id = R.dimen.button_size)
    val buttonPadding = dimensionResource(id = R.dimen.button_padding)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(buttonPadding)
            .fillMaxWidth()
    ) {

        Box(modifier = Modifier.size(buttonSize)) {
            Image(
                painter = painterResource(id = R.drawable.move_left_purple_button),
                contentDescription = stringResource(id = R.string.game_left_button),
                modifier = Modifier
                    .size(buttonSize)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onMoveLeft(true)
                                this.awaitRelease()
                                onMoveLeft(false)
                            }
                        )
                    }
            )
        }

        Box(modifier = Modifier.size(buttonSize)) {
            Image(
                painterResource(id = R.drawable.move_right_purple_button),
                contentDescription = stringResource(id = R.string.game_right_button),
                modifier = Modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                onMoveRight(true)
                                this.awaitRelease()
                                onMoveRight(false)
                            }
                        )
                    }
            )
        }
    }
}
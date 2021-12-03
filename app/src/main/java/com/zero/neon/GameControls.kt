package com.zero.neon.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zero.neon.R

@Composable
fun GameControls(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {

        Image(
            painterResource(id = R.mipmap.left_button),
            contentScale = ContentScale.None,
            contentDescription = stringResource(id = R.string.game_left_button),
            modifier = Modifier
                .scale(0.7f)
                .clickable {}
        )

        Image(
            painterResource(id = R.mipmap.right_button),
            contentScale = ContentScale.None,
            contentDescription = stringResource(id = R.string.game_right_button),
            modifier = Modifier
                .scale(0.7f)
                .clickable {}
        )
    }
}
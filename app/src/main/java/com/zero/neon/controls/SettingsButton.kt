package com.zero.neon.controls

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zero.neon.R

@Composable
fun SettingsButton(modifier: Modifier = Modifier, onSettings: () -> Unit) {

    val buttonPadding = dimensionResource(id = R.dimen.button_padding)

    Image(
        painter = painterResource(id = R.drawable.settings_button),
        contentDescription = stringResource(id = R.string.game_settings_button),
        modifier = modifier
            .padding(top = buttonPadding * 2, end = buttonPadding)
            .size(60.dp)
            .clickable { onSettings() }
    )
}
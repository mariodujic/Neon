package com.zero.neon.gamepause

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.common.theme.Blue
import com.zero.neon.common.theme.Pink

@Composable
fun GamePauseDialog(onRestartGame: () -> Unit) {
    Card(
        backgroundColor = Blue,
        border = BorderStroke(2.dp, Pink)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(22.dp)
        ) {
            Text(
                text = stringResource(id = R.string.game_pause_dialog_title),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onRestartGame) {
                Text(
                    text = stringResource(id = R.string.restart_game_button),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}
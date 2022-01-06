package com.zero.neon.game.controls

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zero.neon.R

@Composable
fun StatusIndicator(
    gameTime: String,
    hp: Int,
    mineralsEarnedTotal: String,
    modifier: Modifier = Modifier
) {

    val buttonPaddingEnd = dimensionResource(id = R.dimen.button_padding)
    val buttonPaddingTop = buttonPaddingEnd * 2
    val height = 60.dp

    Column(modifier = modifier.padding(start = buttonPaddingEnd, top = buttonPaddingTop)) {
        Box(modifier = modifier.height(height = height)) {
            Image(
                painter = painterResource(id = R.drawable.hp_indicator),
                contentDescription = stringResource(id = R.string.game_hp_indicator),
                modifier = Modifier.align(Alignment.Center)
            )
            Text(
                text = "${hp}hp",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp, end = 14.dp)
            )
            Text(
                text = gameTime,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = 8.dp, bottom = 9.dp)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mineral),
                contentDescription = stringResource(id = R.string.mineral_content_description),
                tint = Color.Unspecified,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = mineralsEarnedTotal,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.h5
            )
        }
    }
}
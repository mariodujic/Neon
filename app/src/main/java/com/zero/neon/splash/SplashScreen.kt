package com.zero.neon.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zero.neon.R
import com.zero.neon.common.theme.Blue
import com.zero.neon.common.theme.Pink
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onStartGame: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(1200)
        onStartGame()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(Blue, Pink)))
    ) {

        val textXOffset = remember { Animatable(-350f) }
        LaunchedEffect(Unit) {
            textXOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 1000)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.splash_image),
            contentDescription = stringResource(id = R.string.splash),
            modifier = Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.clip(RectangleShape)) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.offset(x = textXOffset.value.dp)
            )
        }
    }
}
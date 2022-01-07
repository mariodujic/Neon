package com.zero.neon.game.utils

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun rememberImageLoader(): ImageLoader {
    val context = LocalContext.current
    return remember {
        ImageLoader(context)
            .newBuilder()
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(context))
                } else {
                    add(GifDecoder())
                }
            }.build()
    }
}
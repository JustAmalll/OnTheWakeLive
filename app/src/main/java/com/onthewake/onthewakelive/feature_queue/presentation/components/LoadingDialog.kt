package com.onthewake.onthewakelive.feature_queue.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.*
import com.onthewake.onthewakelive.R

@Composable
fun LoadingDialog() {

    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.Transparent
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(40.dp)
            ) {
                val compositionResult: LottieCompositionResult =
                    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loader))

                val progress by animateLottieCompositionAsState(
                    composition = compositionResult.value,
                    isPlaying = true,
                    iterations = LottieConstants.IterateForever,
                    speed = 1.0f
                )

                LottieAnimation(
                    composition = compositionResult.value,
                    progress = { progress },
                    modifier = Modifier.size(260.dp)
                )
            }
        }
    }
}
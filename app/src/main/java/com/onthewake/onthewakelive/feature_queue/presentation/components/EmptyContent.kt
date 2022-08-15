package com.onthewake.onthewakelive.feature_queue.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.onthewake.onthewakelive.R

@Composable
fun EmptyContent(modifier: Modifier = Modifier) {

    val compositionResult: LottieCompositionResult =
        rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.cat))

    val progress by animateLottieCompositionAsState(
        composition = compositionResult.value,
        isPlaying = true,
        iterations = LottieConstants.IterateForever,
        speed = 1.0f
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LottieAnimation(
            composition = compositionResult.value,
            progress = { progress },
            modifier = Modifier.size(240.dp)
        )

        Text(
            text = stringResource(id = R.string.empty_queue_error),
            fontSize = 17.sp
        )
        Spacer(modifier = Modifier.height(80.dp))
    }

}
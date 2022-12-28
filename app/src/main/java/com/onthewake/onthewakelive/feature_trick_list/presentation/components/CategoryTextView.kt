package com.onthewake.onthewakelive.feature_trick_list.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CategoryTextView(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 16.dp, bottom = 6.dp)
    )
}
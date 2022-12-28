package com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SelectableItem(
    title: String,
    subtitle: String,
    selected: Boolean = false,
    onClick: () -> Unit
) {

    val primary = MaterialTheme.colorScheme.primary
    val onSurface = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .border(
                    width = 1.dp,
                    color = if (selected) primary else onSurface,
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .clip(RoundedCornerShape(size = 10.dp))
                .clickable(onClick = onClick)
        ) {
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 10.dp),
                text = title.replaceFirstChar { it.uppercaseChar() },
                style = TextStyle(
                    color = if (selected) primary else onSurface,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                modifier = Modifier.padding(start = 12.dp, bottom = 10.dp, end = 50.dp),
                text = subtitle.replaceFirstChar { it.uppercaseChar() },
                style = TextStyle(color = if (selected) primary else onSurface),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp),
            onClick = onClick,
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Selectable Item Icon",
                tint = if (selected) primary else onSurface
            )
        }
    }
}
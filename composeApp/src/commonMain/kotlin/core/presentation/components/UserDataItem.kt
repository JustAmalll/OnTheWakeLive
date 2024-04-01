package core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.not_specified
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UserDataItem(
    title: String,
    value: String?,
    onActionButtonClicked: (() -> Unit)? = null,
    showDivider: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = title,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                modifier = Modifier.padding(top = 2.dp),
                text = if (value.isNullOrEmpty()) {
                    stringResource(resource = Res.string.not_specified)
                } else {
                    value
                }
            )
        }
        if (onActionButtonClicked != null && !value.isNullOrEmpty()) {
            IconButton(onClick = onActionButtonClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
    if (showDivider) {
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
    }
}
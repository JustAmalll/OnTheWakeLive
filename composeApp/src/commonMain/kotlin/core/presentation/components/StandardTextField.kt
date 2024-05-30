package core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.ui.theme.gradientBackground

@Composable
fun StandardTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    prefix: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        label?.let {
            Text(
                modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
                text = label,
                color = Color(0xFF606060),
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(TextFieldDefaults.MinHeight)
                .gradientBackground(radius = 16.dp),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(size = 16.dp),
            singleLine = true,
            placeholder = placeholder?.let {
                { Text(text = it) }
            },
            enabled = enabled,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingIcon,
            prefix = prefix,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                errorCursorColor = Color.White
            )
        )
        supportingText?.let {
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 2.dp),
                text = it,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color(0xFF606060)
                },
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp
            )
        }
    }
}
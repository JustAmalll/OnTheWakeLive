package queue.presentation.list.components

import LocalToggleBackgroundBlur
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import core.presentation.ui.theme.gradientBackground
import core.utils.Constants
import core.utils.Constants.INSTAGRAM_URL
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.ic_profile_outlined
import onthewakelive.composeapp.generated.resources.instagram
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import user_profile.domain.model.UserProfile

@OptIn(ExperimentalResourceApi::class)
@Composable
fun QueueItemDetailsDialog(
    userProfile: UserProfile,
    onDismissRequest: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val localToggleBackgroundBlur = LocalToggleBackgroundBlur.current

    val dismissDialog = remember {
        {
            localToggleBackgroundBlur()
            onDismissRequest()
        }
    }
    LaunchedEffect(true) {
        localToggleBackgroundBlur()
    }

    Dialog(onDismissRequest = dismissDialog) {
        Box(modifier = Modifier.gradientBackground(radius = 16.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1D1B20)),
                    contentAlignment = Alignment.Center
                ) {
                    userProfile.photo?.let { photo ->
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(onClick = {}),
                            model = "${Constants.BASE_URL}/storage/$photo",
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } ?: Icon(
                        painter = painterResource(Res.drawable.ic_profile_outlined),
                        contentDescription = null
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = userProfile.firstName,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = userProfile.lastName,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                userProfile.instagram?.let { instagram ->
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        onClick = { uriHandler.openUri(uri = "$INSTAGRAM_URL/$instagram") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1D3E65),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(resource = Res.string.instagram),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }
            IconButton(
                modifier = Modifier
                    .align(alignment = Alignment.TopEnd)
                    .padding(all = 16.dp),
                onClick = dismissDialog
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        }
    }
}
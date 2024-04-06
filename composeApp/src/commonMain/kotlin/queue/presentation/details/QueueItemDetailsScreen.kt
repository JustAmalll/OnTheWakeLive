package queue.presentation.details

import LocalIsUserAdmin
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.benasher44.uuid.Uuid
import core.presentation.components.UserDataItem
import core.utils.Constants.INSTAGRAM_URL
import full_size_photo.presentation.FullSizePhotoAssembly
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.details
import onthewakelive.composeapp.generated.resources.instagram
import onthewakelive.composeapp.generated.resources.phone_number
import onthewakelive.composeapp.generated.resources.telegram
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import queue.presentation.details.QueueItemDetailsEvent.OnNavigateBackClicked
import queue.presentation.details.QueueItemDetailsViewModel.QueueItemDetailsAction.NavigateBack
import queue.presentation.details.QueueItemDetailsViewModel.QueueItemDetailsAction.NavigateToFullSizePhotoScreen

data class QueueItemDetailsAssembly(val userId: Uuid) : Screen {

    @Composable
    override fun Content() {
        val viewModel: QueueItemDetailsViewModel = koinInject(
            parameters = { parametersOf(userId) }
        )
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(key1 = true) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateBack -> navigator?.pop()

                    is NavigateToFullSizePhotoScreen -> navigator?.push(
                        FullSizePhotoAssembly(photo = action.photo)
                    )
                }
            }
        }

        QueueItemDetailsScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun QueueItemDetailsScreen(
    state: QueueItemDetailsState,
    onEvent: (QueueItemDetailsEvent) -> Unit
) {
    val isUserAdmin = LocalIsUserAdmin.current
    val uriHandler = LocalUriHandler.current
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    state.userProfile?.let { userProfile ->
        Scaffold(
            topBar = {
                Column(modifier = Modifier.background(color = surfaceColor)) {
                    CenterAlignedTopAppBar(
                        title = { Text(text = stringResource(resource = Res.string.details)) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor),
                        navigationIcon = {
                            IconButton(onClick = { onEvent(OnNavigateBackClicked) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 16.dp)
                            .clip(shape = MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
//                        UserPhoto(
//                            photo = state.userProfile.photo,
//                            onClick = { onEvent(OnPhotoClicked) }
//                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = userProfile.firstName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userProfile.lastName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(all = 16.dp)
            ) {
                UserDataItem(
                    title = stringResource(resource = Res.string.instagram),
                    value = userProfile.instagram,
                    onActionButtonClicked = {
                        uriHandler.openUri(
                            uri = "$INSTAGRAM_URL/${state.userProfile.instagram}"
                        )
                    }
                )
                UserDataItem(
                    title = stringResource(resource = Res.string.telegram),
                    value = userProfile.telegram,
                    showDivider = isUserAdmin
                )
                if (isUserAdmin) {
                    UserDataItem(
                        title = stringResource(resource = Res.string.phone_number),
                        value = "+${userProfile.phoneNumber}",
                        showDivider = false
                    )
                }
            }
        }
    }
}
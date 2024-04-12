package user_profile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import auth.presentation.login.LoginAssembly
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import core.presentation.components.SplashLoadingScreen
import core.presentation.components.UserDataItem
import core.presentation.components.UserPhoto
import core.utils.Constants
import full_size_photo.presentation.FullSizePhotoAssembly
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.instagram
import onthewakelive.composeapp.generated.resources.phone_number
import onthewakelive.composeapp.generated.resources.profile
import onthewakelive.composeapp.generated.resources.telegram
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import user_profile.presentation.edit_profile.EditUserProfileAssembly
import user_profile.presentation.profile.UserProfileEvent.OnEditProfileClicked
import user_profile.presentation.profile.UserProfileEvent.OnLogoutClicked
import user_profile.presentation.profile.UserProfileEvent.OnUserPhotoClicked
import user_profile.presentation.profile.UserProfileViewModel.UserProfileAction.NavigateToEditProfileScreen
import user_profile.presentation.profile.UserProfileViewModel.UserProfileAction.NavigateToFullSizePhotoScreen
import user_profile.presentation.profile.UserProfileViewModel.UserProfileAction.NavigateToLoginScreen
import user_profile.presentation.profile.UserProfileViewModel.UserProfileAction.ShowError

object UserProfileTab : Tab {

    @Composable
    override fun Content() {
        val viewModel: UserProfileViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow.parent
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateToLoginScreen -> navigator?.replaceAll(LoginAssembly())
                    NavigateToEditProfileScreen -> navigator?.push(EditUserProfileAssembly())

                    is NavigateToFullSizePhotoScreen -> navigator?.push(
                        FullSizePhotoAssembly(photo = action.photo)
                    )

                    is ShowError -> snackBarHostState.showSnackbar(action.message)
                }
            }
        }
        UserProfileScreen(
            state = state,
            snackBarHostState = snackBarHostState,
            onEvent = viewModel::onEvent
        )
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(resource = Res.string.profile)
            val icon = rememberVectorPainter(image = Icons.Default.Person)

            return remember { TabOptions(index = 1u, title = title, icon = icon) }
        }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun UserProfileScreen(
    state: UserProfileState,
    snackBarHostState: SnackbarHostState,
    onEvent: (UserProfileEvent) -> Unit
) {
    val uriHandler = LocalUriHandler.current
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)

    if (state.userProfile != null) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                Column(modifier = Modifier.background(color = surfaceColor)) {
                    TopAppBar(
                        title = { Text(text = stringResource(resource = Res.string.profile)) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceColor),
                        actions = {
                            IconButton(onClick = { onEvent(OnLogoutClicked) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Logout,
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
                        UserPhoto(
                            photo = state.userProfile.photo,
                            onClick = { onEvent(OnUserPhotoClicked) }
                        )
                        Column(
                            modifier = Modifier
                                .padding(start = 12.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = state.userProfile.firstName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.userProfile.lastName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        IconButton(onClick = { onEvent(OnEditProfileClicked) }) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
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
                    value = state.userProfile.instagram,
                    onActionButtonClicked = {
                        uriHandler.openUri(
                            uri = "${Constants.INSTAGRAM_URL}/${state.userProfile.instagram}"
                        )
                    }
                )
                UserDataItem(
                    title = stringResource(resource = Res.string.telegram),
                    value = state.userProfile.telegram
                )
                UserDataItem(
                    title = stringResource(resource = Res.string.phone_number),
                    value = "+${state.userProfile.phoneNumber}",
                    showDivider = false
                )
            }
        }
    } else {
        SplashLoadingScreen()
    }
}
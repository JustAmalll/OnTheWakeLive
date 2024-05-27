package user_profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import auth.presentation.login.LoginAssembly
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import coil3.compose.AsyncImage
import core.presentation.components.SplashLoadingScreen
import core.presentation.components.StandardButton
import core.presentation.components.StandardTextField
import core.presentation.ui.theme.gradientBackground
import core.presentation.utils.clickableWithoutIndication
import core.presentation.utils.rememberBitmapFromBytes
import core.presentation.utils.rememberImagePickerLauncher
import core.utils.Constants
import full_size_photo.presentation.FullSizePhotoAssembly
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.edit
import onthewakelive.composeapp.generated.resources.first_name
import onthewakelive.composeapp.generated.resources.ic_delete
import onthewakelive.composeapp.generated.resources.ic_profile_outlined
import onthewakelive.composeapp.generated.resources.instagram
import onthewakelive.composeapp.generated.resources.last_name
import onthewakelive.composeapp.generated.resources.phone_number
import onthewakelive.composeapp.generated.resources.profile
import onthewakelive.composeapp.generated.resources.telegram
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import user_profile.presentation.UserProfileEvent.OnDeleteUserPhotoClicked
import user_profile.presentation.UserProfileEvent.OnEditProfileClicked
import user_profile.presentation.UserProfileEvent.OnLogoutClicked
import user_profile.presentation.UserProfileEvent.OnTelegramChanged
import user_profile.presentation.UserProfileViewModel.UserProfileAction.NavigateToFullSizePhotoScreen
import user_profile.presentation.UserProfileViewModel.UserProfileAction.NavigateToLoginScreen
import user_profile.presentation.UserProfileViewModel.UserProfileAction.ShowError

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
            val icon = painterResource(resource = Res.drawable.ic_profile_outlined)

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
    val imagePicker = rememberImagePickerLauncher {
        onEvent(UserProfileEvent.OnUserPhotoSelected(byteArray = it))
    }

    if (!state.isLoading) {
        Scaffold(
            modifier = Modifier.padding(bottom = 6.dp),
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(resource = Res.string.profile)) },
                    actions = {
                        IconButton(onClick = { onEvent(OnLogoutClicked) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null
                            )
                        }
                    },
                )
            },
            bottomBar = {
                if (state.hasChanges) {
                    Box(
                        modifier = Modifier.background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.background
                                )
                            ),
                            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                        )
                    ) {
                        StandardButton(
                            modifier = Modifier
                                .padding(all = 16.dp)
                                .padding(bottom = 26.dp),
                            text = stringResource(resource = Res.string.edit),
                            onClick = { onEvent(OnEditProfileClicked) }
                        )
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(top = 8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .clickable(onClick = imagePicker::launch)
                            .gradientBackground(),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            state.newPhotoBytes != null ->
                                rememberBitmapFromBytes(bytes = state.newPhotoBytes)?.let {
                                    Image(
                                        modifier = Modifier.fillMaxSize(),
                                        bitmap = it,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }

                            state.photo != null -> AsyncImage(
                                modifier = Modifier.fillMaxSize(),
                                model = "${Constants.BASE_URL}/storage/${state.photo}",
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )

                            else -> Icon(
                                modifier = Modifier.size(64.dp),
                                painter = painterResource(Res.drawable.ic_profile_outlined),
                                contentDescription = null
                            )
                        }
                    }
                    if (state.photo != null || state.newPhotoBytes != null) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clickableWithoutIndication { onEvent(OnDeleteUserPhotoClicked) }
                                .clip(CircleShape)
                                .gradientBackground()
                                .align(Alignment.BottomEnd)
                                .border(
                                    border = BorderStroke(width = 1.dp, color = Color.White),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.padding(all = 2.dp),
                                painter = painterResource(resource = Res.drawable.ic_delete),
                                contentDescription = null
                            )
                        }
                    }
                }
                StandardTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = state.firstName,
                    onValueChange = { onEvent(UserProfileEvent.OnFirstNameChanged(it)) },
                    label = stringResource(resource = Res.string.first_name)
                )
                StandardTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = state.lastName,
                    onValueChange = { onEvent(UserProfileEvent.OnLastNameChanged(it)) },
                    label = stringResource(resource = Res.string.last_name)
                )
                StandardTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = state.phoneNumber,
                    onValueChange = {},
                    label = stringResource(resource = Res.string.phone_number),
                    isPhoneNumberTextField = true,
                    enabled = false
                )
                StandardTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = state.instagram,
                    onValueChange = { onEvent(UserProfileEvent.OnInstagramChanged(it)) },
                    label = stringResource(resource = Res.string.instagram),
                    supportingText = "Поле, не требующее обязательного заполнения"
                )
                StandardTextField(
                    modifier = Modifier.padding(top = 16.dp),
                    value = state.telegram,
                    onValueChange = { onEvent(OnTelegramChanged(it)) },
                    label = stringResource(resource = Res.string.telegram),
                    supportingText = "Поле, не требующее обязательного заполнения"
                )
            }
        }
    } else {
        SplashLoadingScreen()
    }
}
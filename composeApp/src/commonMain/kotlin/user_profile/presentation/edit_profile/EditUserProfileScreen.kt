package user_profile.presentation.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil3.compose.AsyncImage
import core.presentation.components.StandardTextField
import core.presentation.utils.ImageCropper
import core.presentation.utils.rememberBitmapFromBytes
import core.presentation.utils.rememberImagePickerLauncher
import core.utils.Constants
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.edit
import onthewakelive.composeapp.generated.resources.edit_profile
import onthewakelive.composeapp.generated.resources.first_name
import onthewakelive.composeapp.generated.resources.instagram
import onthewakelive.composeapp.generated.resources.last_name
import onthewakelive.composeapp.generated.resources.phone_number
import onthewakelive.composeapp.generated.resources.telegram
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnDeleteUserPhotoClicked
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnEditProfileClicked
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnFirstNameChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnInstagramChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnLastNameChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnNavigateBackClicked
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnTelegramChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnUserPhotoCropped
import user_profile.presentation.edit_profile.EditUserProfileViewModel.EditUserProfileAction.NavigateBack
import user_profile.presentation.edit_profile.EditUserProfileViewModel.EditUserProfileAction.ShowError

class EditUserProfileAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: EditUserProfileViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateBack -> navigator?.pop()
                    is ShowError -> snackBarHostState.showSnackbar(action.message)
                }
            }
        }
        EditUserProfileScreen(
            state = state,
            snackBarHostState = snackBarHostState,
            onEvent = viewModel::onEvent
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun EditUserProfileScreen(
    state: EditUserProfileState,
    snackBarHostState: SnackbarHostState,
    onEvent: (EditUserProfileEvent) -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val focusManager = LocalFocusManager.current

    val imagePicker = rememberImagePickerLauncher {
        onEvent(EditUserProfileEvent.OnUserPhotoSelected(byteArray = it))
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(resource = Res.string.edit_profile)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = surfaceColor),
                navigationIcon = {
                    IconButton(onClick = { onEvent(OnNavigateBackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.padding(top = 30.dp)) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .clickable(onClick = imagePicker::launch)
                        .background(surfaceColor),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        state.newPhotoBytes != null -> {
                            rememberBitmapFromBytes(bytes = state.newPhotoBytes)?.let {
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    bitmap = it,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        state.photo != null -> AsyncImage(
                            modifier = Modifier.fillMaxSize(),
                            model = "${Constants.BASE_URL}/storage/${state.photo}",
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                        else -> Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Default.Person,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentDescription = null
                        )
                    }
                }
                if (state.photo != null || state.newPhotoBytes != null) {
                    Box(
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        IconButton(
                            modifier = Modifier.size(24.dp),
                            onClick = { onEvent(OnDeleteUserPhotoClicked) },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            )
                        ) {
                            Icon(
                                modifier = Modifier.padding(all = 2.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            StandardTextField(
                modifier = Modifier.padding(top = 30.dp),
                value = state.firstName,
                onValueChange = { onEvent(OnFirstNameChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(resource = Res.string.first_name),
                error = state.firsNameError
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.lastName,
                onValueChange = { onEvent(OnLastNameChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                label = stringResource(resource = Res.string.last_name),
                error = state.lastNameError
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
                value = state.telegram,
                onValueChange = { onEvent(OnTelegramChanged(it)) },
                label = stringResource(resource = Res.string.telegram)
            )
            StandardTextField(
                modifier = Modifier.padding(top = 16.dp),
                value = state.instagram,
                onValueChange = { onEvent(OnInstagramChanged(it)) },
                label = stringResource(resource = Res.string.instagram)
            )
            Button(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 16.dp),
                onClick = {
                    onEvent(OnEditProfileClicked)
                    focusManager.clearFocus()
                }
            ) {
                Text(text = stringResource(resource = Res.string.edit))
            }
        }
    }
    if (state.showImageCropper && state.newPhotoBytes != null) {
        ImageCropper(
            photo = state.newPhotoBytes,
            onImageCropped = { onEvent(OnUserPhotoCropped(it)) }
        )
    }
}
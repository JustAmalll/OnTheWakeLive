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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import core.presentation.components.StandardTextField
import core.presentation.utils.rememberBitmapFromBytes
import core.presentation.utils.rememberImagePickerLauncher
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
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnEditProfileClicked
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnFirstNameChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnInstagramChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnLastNameChanged
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnNavigateBackClicked
import user_profile.presentation.edit_profile.EditUserProfileEvent.OnTelegramChanged
import user_profile.presentation.edit_profile.EditUserProfileViewModel.EditUserProfileAction.NavigateBack
import user_profile.presentation.edit_profile.EditUserProfileViewModel.EditUserProfileAction.ShowError

class EditUserProfileAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: EditUserProfileViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateBack -> navigator?.pop()
                    is ShowError -> {}
                }
            }
        }

        EditUserProfileScreen(state = state, onEvent = viewModel::onEvent)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun EditUserProfileScreen(
    state: EditUserProfileState,
    onEvent: (EditUserProfileEvent) -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val focusManager = LocalFocusManager.current

    val imagePicker = rememberImagePickerLauncher {
        onEvent(EditUserProfileEvent.OnUserPhotoSelected(byteArray = it))
    }

    Scaffold(
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
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 30.dp)
                    .size(140.dp)
                    .clip(CircleShape)
                    .clickable(onClick = imagePicker::launch)
                    .background(surfaceColor),
                contentAlignment = Alignment.Center
            ) {
                rememberBitmapFromBytes(bytes = state.photo)?.let {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                } ?: Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Default.Person,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
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
                isPhoneNumberTextField = true
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
                    .padding(top = 16.dp),
                onClick = {
                    onEvent(OnEditProfileClicked)
                    focusManager.clearFocus()
                }
            ) {
                Text(text = stringResource(resource = Res.string.edit))
            }
        }
    }
}
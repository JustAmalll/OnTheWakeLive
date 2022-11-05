package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.StandardTextField
import com.onthewake.onthewakelive.dataStore
import com.onthewake.onthewakelive.util.CropActivityResultContract
import com.onthewake.onthewakelive.util.MaskVisualTransformation
import com.onthewake.onthewakelive.util.UserProfileSerializer.defaultValue
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    imageLoader: ImageLoader,
    navController: NavHostController
) {

    val state = viewModel.state
    val snackBarHostState = remember { SnackbarHostState() }
    val isImageLoading = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val profilePictureUri = viewModel.profilePictureUri.value
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    val bgColor = MaterialTheme.colorScheme.background

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = surfaceColor, darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = bgColor, darkIcons = !darkTheme
        )
    }

    val dataStore = remember {
        context.dataStore.data
    }.collectAsState(initial = defaultValue)

    LaunchedEffect(key1 = true) {
        viewModel.snackBarEvent.collectLatest { message ->
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.navigateUp.collectLatest {
            navController.popBackStack()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.firstName.value = dataStore.value.firstName
        viewModel.lastName.value = dataStore.value.lastName
        viewModel.phoneNumber.value = dataStore.value.phoneNumber
        viewModel.userProfilePictureUri.value = dataStore.value.profilePictureUri
        viewModel.instagram.value = dataStore.value.instagram
        viewModel.telegram.value = dataStore.value.telegram
        viewModel.dateOfBirth.value = dataStore.value.dateOfBirth
    }

    val cropActivityLauncher = rememberLauncherForActivityResult(
        contract = CropActivityResultContract(16f, 16f)
    ) { viewModel.onEvent(EditProfileUiEvent.CropImage(it)) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { cropActivityLauncher.launch(it) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.edit_profile)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.arrow_back_icon)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgColor)
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier
                                .padding(top = 30.dp)
                                .size(140.dp),
                            shape = RoundedCornerShape(40.dp),
                            onClick = { galleryLauncher.launch("image/*") },
                            colors = CardDefaults.cardColors(
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (!isImageLoading.value) {
                                    Icon(
                                        modifier = Modifier.size(30.dp),
                                        imageVector = Icons.Default.Person,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        contentDescription = stringResource(
                                            id = R.string.person_icon
                                        )
                                    )
                                }
                                if (isImageLoading.value) {
                                    CircularProgressIndicator(modifier = Modifier.size(42.dp))
                                }
                                Image(
                                    modifier = Modifier.fillMaxSize(),
                                    painter = rememberAsyncImagePainter(
                                        model = profilePictureUri
                                            ?: viewModel.userProfilePictureUri.value,
                                        imageLoader = imageLoader,
                                        onLoading = { isImageLoading.value = true },
                                        onError = { isImageLoading.value = false },
                                        onSuccess = { isImageLoading.value = false }
                                    ),
                                    contentDescription = stringResource(id = R.string.user_picture)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        StandardTextField(
                            value = viewModel.firstName.value,
                            onValueChange = {
                                viewModel.onEvent(EditProfileUiEvent.EditProfileFirstNameChanged(it))
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next
                            ),
                            label = stringResource(id = R.string.first_name),
                            isError = state.profileFirsNameError != null,
                            errorText = state.profileFirsNameError
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StandardTextField(
                            value = viewModel.lastName.value,
                            onValueChange = {
                                viewModel.onEvent(EditProfileUiEvent.EditProfileLastNameChanged(it))
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Next
                            ),
                            label = stringResource(id = R.string.last_name),
                            isError = state.profileLastNameError != null,
                            errorText = state.profileLastNameError
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StandardTextField(
                            value = viewModel.phoneNumber.value,
                            onValueChange = {
                                viewModel.onEvent(
                                    EditProfileUiEvent.EditProfilePhoneNumberChanged(it)
                                )
                            },
                            label = stringResource(id = R.string.phone_number),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            isError = state.profilePhoneNumberError != null,
                            errorText = state.profilePhoneNumberError
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StandardTextField(
                            value = viewModel.telegram.value,
                            onValueChange = {
                                viewModel.onEvent(EditProfileUiEvent.EditProfileTelegramChanged(it))
                            },
                            label = stringResource(id = R.string.telegram)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StandardTextField(
                            value = viewModel.instagram.value,
                            onValueChange = {
                                viewModel.onEvent(EditProfileUiEvent.EditProfileInstagramChanged(it))
                            },
                            label = stringResource(id = R.string.instagram)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = viewModel.dateOfBirth.value,
                            onValueChange = {
                                if (it.length <= 8) {
                                    viewModel.onEvent(
                                        EditProfileUiEvent.EditProfileDateOfBirthChanged(it)
                                    )
                                }
                            },
                            isError = state.profileDateOfBirthError != null,
                            label = {
                                Text(
                                    text = stringResource(id = R.string.edit_profile_date_of_birth)
                                )
                            },
                            visualTransformation = MaskVisualTransformation("##/##/####"),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        )
                        if (state.profileDateOfBirthError != null) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = state.profileDateOfBirthError,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp,
                                textAlign = TextAlign.End
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (dataStore.value.firstName == viewModel.firstName.value &&
                                    dataStore.value.lastName == viewModel.lastName.value &&
                                    dataStore.value.phoneNumber == viewModel.phoneNumber.value &&
                                    viewModel.profilePictureUri.value == null &&
                                    dataStore.value.instagram == viewModel.instagram.value &&
                                    dataStore.value.telegram == viewModel.telegram.value &&
                                    dataStore.value.dateOfBirth == viewModel.dateOfBirth.value
                                ) {
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            context.getString(R.string.nothing_to_update)
                                        )
                                    }
                                } else {
                                    viewModel.onEvent(EditProfileUiEvent.EditProfile)
                                    focusManager.clearFocus()
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = stringResource(id = R.string.edit))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}








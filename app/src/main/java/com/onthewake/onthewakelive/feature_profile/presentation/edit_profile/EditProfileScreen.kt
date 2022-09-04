package com.onthewake.onthewakelive.feature_profile.presentation.edit_profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.DefaultTextField
import com.onthewake.onthewakelive.util.CropActivityResultContract

@ExperimentalMaterial3Api
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    imageLoader: ImageLoader
) {

    val state = viewModel.state

    val imageUri = viewModel.chosenImageUri.value

    val cropActivityLauncher = rememberLauncherForActivityResult(
        contract = CropActivityResultContract(16f, 16f)
    ) {
        viewModel.onEvent(EditProfileUiEvent.CropImage(it))
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        cropActivityLauncher.launch(it)
    }

    Scaffold(
        topBar = {
            TopAppBar(elevation = 0.dp) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(all = 5.dp),
                        text = "Profile",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.Card(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .size(140.dp),
                    shape = RoundedCornerShape(40.dp),
                    onClick = {
                        galleryLauncher.launch("image/*")
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = Icons.Default.Person,
                        contentDescription = "Person Icon"
                    )
                    imageUri?.let { uri ->
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = rememberAsyncImagePainter(
                                model = uri,
                                imageLoader = imageLoader
                            ),
                            contentDescription = "Post Image"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                DefaultTextField(
                    value = state.profileFirsName,
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
                DefaultTextField(
                    value = state.profileLastName,
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
                DefaultTextField(
                    value = state.profilePhoneNumber,
                    onValueChange = {
                        viewModel.onEvent(EditProfileUiEvent.EditProfilePhoneNumberChanged(it))
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
                DefaultTextField(
                    value = state.profileTelegram,
                    onValueChange = {
                        viewModel.onEvent(EditProfileUiEvent.EditProfileTelegramChanged(it))
                    },
                    label = stringResource(id = R.string.telegram)
                )
                Spacer(modifier = Modifier.height(16.dp))
                DefaultTextField(
                    value = state.profileInstagram,
                    onValueChange = {
                        viewModel.onEvent(EditProfileUiEvent.EditProfileInstagramChanged(it))
                    },
                    label = stringResource(id = R.string.instagram)
                )
            }
        }
    }
}
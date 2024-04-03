package full_size_photo.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.utils.rememberBitmapFromBytes

@Suppress("ArrayInDataClass")
data class FullSizePhotoAssembly(val photo: ByteArray) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        FullSizePhotoScreen(
            photo = photo,
            onNavigateBackClicked = { navigator?.pop() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FullSizePhotoScreen(
    photo: ByteArray,
    onNavigateBackClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBackClicked) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        rememberBitmapFromBytes(bytes = photo)?.let { bitmap ->
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                bitmap = bitmap,
                contentDescription = null
            )
        }
    }
}
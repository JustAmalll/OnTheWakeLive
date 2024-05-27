package activate_subscription

import activate_subscription.ActivateSubscriptionEvent.OnActivateSubscriptionClicked
import activate_subscription.ActivateSubscriptionEvent.OnChangeSelectedUserClicked
import activate_subscription.ActivateSubscriptionEvent.OnNavigateBackClicked
import activate_subscription.ActivateSubscriptionEvent.OnSearchQueueChanged
import activate_subscription.ActivateSubscriptionEvent.OnUserPhotoClicked
import activate_subscription.ActivateSubscriptionEvent.OnUserSelected
import activate_subscription.ActivateSubscriptionViewModel.ActivateSubscriptionAction.NavigateBack
import activate_subscription.ActivateSubscriptionViewModel.ActivateSubscriptionAction.NavigateToFullSizePhotoScreen
import activate_subscription.ActivateSubscriptionViewModel.ActivateSubscriptionAction.ShowError
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization.Companion.Sentences
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.components.StandardButton
import core.presentation.components.StandardTextField
import full_size_photo.presentation.FullSizePhotoAssembly
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.activate
import onthewakelive.composeapp.generated.resources.activate_subscription
import onthewakelive.composeapp.generated.resources.edit
import onthewakelive.composeapp.generated.resources.search
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import queue.presentation.admin.components.UserItem

class ActivateSubscriptionAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: ActivateSubscriptionViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = true) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateBack -> navigator?.pop()

                    is NavigateToFullSizePhotoScreen -> navigator?.push(
                        FullSizePhotoAssembly(photo = action.photo)
                    )

                    is ShowError -> snackBarHostState.showSnackbar(action.message)
                }
            }
        }

        ActivateSubscriptionScreen(
            state = state,
            snackBarHostState = snackBarHostState,
            onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun ActivateSubscriptionScreen(
    state: ActivateSubscriptionState,
    snackBarHostState: SnackbarHostState,
    onEvent: (ActivateSubscriptionEvent) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(resource = Res.string.activate_subscription))
                },
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
                .padding(all = 24.dp)
        ) {
            if (state.selectedUser != null) {
                UserItem(
                    firstName = state.selectedUser.firstName,
                    lastName = state.selectedUser.lastName,
                    photo = state.selectedUser.photo,
                    onPhotoClicked = { onEvent(OnUserPhotoClicked(photo = it)) }
                )
                TextButton(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .align(Alignment.End),
                    onClick = { onEvent(OnChangeSelectedUserClicked) }
                ) {
                    Text(text = stringResource(resource = Res.string.edit))
                }
            } else {
                StandardTextField(
                    value = state.searchQuery,
                    onValueChange = { onEvent(OnSearchQueueChanged(it)) },
                    placeholder = stringResource(resource = Res.string.search),
                    keyboardOptions = KeyboardOptions(capitalization = Sentences),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                )
                LazyColumn(
                    modifier = Modifier.heightIn(max = 440.dp),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(key = { it.userId }, items = state.searchedUsers) { user ->
                        UserItem(
                            firstName = user.firstName,
                            lastName = user.lastName,
                            photo = user.photo,
                            onPhotoClicked = { onEvent(OnUserPhotoClicked(photo = it)) },
                            onItemClicked = { onEvent(OnUserSelected(user = user)) }
                        )
                    }
                }
            }
            StandardButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = { onEvent(OnActivateSubscriptionClicked) },
                enabled = state.selectedUser != null,
                text = stringResource(resource = Res.string.activate)
            )
        }
    }
}
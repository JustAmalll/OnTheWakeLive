package queue.presentation.admin

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.components.StandardButton
import core.presentation.components.StandardTextField
import full_size_photo.presentation.FullSizePhotoAssembly
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.add
import onthewakelive.composeapp.generated.resources.add_to_queue
import onthewakelive.composeapp.generated.resources.edit
import onthewakelive.composeapp.generated.resources.first_name
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import queue.domain.model.Line
import queue.presentation.admin.QueueAdminEvent.OnChangeSelectedUserClicked
import queue.presentation.admin.QueueAdminEvent.OnFirstNameChanged
import queue.presentation.admin.QueueAdminEvent.OnUserPhotoClicked
import queue.presentation.admin.QueueAdminEvent.OnUserSelected
import queue.presentation.admin.QueueAdminViewModel.QueueAdminAction.NavigateBack
import queue.presentation.admin.QueueAdminViewModel.QueueAdminAction.NavigateToFullSizePhotoScreen
import queue.presentation.admin.components.UserItem

data class QueueAdminAssembly(val line: Line) : Screen {

    @Composable
    override fun Content() {
        val viewModel: QueueAdminViewModel = koinInject(
            parameters = { parametersOf(line) }
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

        QueueAdminScreen(state = state, onEvent = viewModel::onEvent)
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun QueueAdminScreen(
    state: QueueAdminState,
    onEvent: (QueueAdminEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(resource = Res.string.add_to_queue))
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(QueueAdminEvent.OnNavigateBackClicked) }) {
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
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Line.entries.forEachIndexed { index, line ->
                    SegmentedButton(
                        selected = state.line == line,
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = 2),
                        label = { Text(text = stringResource(resource = line.displayName)) },
                        onClick = { onEvent(QueueAdminEvent.OnLineSelected(line = line)) }
                    )
                }
            }

            if (state.selectedUser != null) {
                UserItem(
                    firstName = state.selectedUser.firstName,
                    lastName = state.selectedUser.lastName,
                    photo = state.selectedUser.photo,
                    onPhotoClicked = {
                        state.selectedUser.photo?.let { onEvent(OnUserPhotoClicked(photo = it)) }
                    }
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
                    value = state.firstName,
                    onValueChange = { onEvent(OnFirstNameChanged(it)) },
                    label = stringResource(resource = Res.string.first_name),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
                LazyColumn(
                    modifier = Modifier.heightIn(max = 400.dp),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(key = { it.userId }, items = state.searchedUsers) { user ->
                        UserItem(
                            firstName = user.firstName,
                            lastName = user.lastName,
                            photo = user.photo,
                            onPhotoClicked = {
                                user.photo?.let { onEvent(OnUserPhotoClicked(photo = it)) }
                            },
                            onItemClicked = { onEvent(OnUserSelected(user = user)) }
                        )
                    }
                }
            }
            StandardButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = { onEvent(QueueAdminEvent.OnAddUserClicked) },
                enabled = state.firstName.isNotEmpty() || state.selectedUser != null,
                isLoading = state.isUserSearching,
                text = stringResource(resource = Res.string.add)
            )
        }
    }
}
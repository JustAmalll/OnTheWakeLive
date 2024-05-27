package queue.presentation.list

import LocalIsUserAdmin
import LocalUserId
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncProblem
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import full_size_photo.presentation.FullSizePhotoAssembly
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.ic_home
import onthewakelive.composeapp.generated.resources.queue
import onthewakelive.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import paywall.presentation.form.PaywallAssembly
import queue.domain.model.Line
import queue.presentation.admin.QueueAdminAssembly
import queue.presentation.list.QueueEvent.LeaveQueueConfirmationDialogDismissRequest
import queue.presentation.list.QueueEvent.OnJoinClicked
import queue.presentation.list.QueueEvent.OnLeaveQueueConfirmed
import queue.presentation.list.QueueEvent.OnQueueItemClicked
import queue.presentation.list.QueueEvent.OnQueueItemDetailsDialogDismissRequest
import queue.presentation.list.QueueEvent.OnQueueLeaved
import queue.presentation.list.QueueEvent.OnQueueReordered
import queue.presentation.list.QueueEvent.OnSaveReorderedQueueClicked
import queue.presentation.list.QueueEvent.OnUserPhotoClicked
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToFullSizePhotoScreen
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToPaywallScreen
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueAdminScreen
import queue.presentation.list.QueueViewModel.QueueAction.ShowError
import queue.presentation.list.QueueViewModel.QueueAction.ShowPermissionError
import queue.presentation.list.components.EmptyQueueContent
import queue.presentation.list.components.LeaveQueueConfirmationDialog
import queue.presentation.list.components.QueueItem
import queue.presentation.list.components.QueueItemDetailsDialog
import queue.presentation.list.components.SwipeToDeleteContainer
import queue.presentation.list.components.TabRow
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

object QueueTab : Tab {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val factory = rememberPermissionsControllerFactory()

        val viewModel: QueueViewModel = koinInject(
            parameters = { parametersOf(factory.createPermissionsController()) }
        )
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current?.parent
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = Unit) {
            viewModel.onEvent(QueueEvent.ConnectToSession)
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    is NavigateToQueueAdminScreen -> navigator?.push(
                        QueueAdminAssembly(line = action.line)
                    )

                    is NavigateToFullSizePhotoScreen -> navigator?.push(
                        FullSizePhotoAssembly(photo = action.photo)
                    )

                    is ShowError -> {
                        val result = snackBarHostState.showSnackbar(
                            message = action.errorMessage,
                            actionLabel = action.actionLabel,
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(QueueEvent.ConnectToSession)
                        }
                    }

                    NavigateToPaywallScreen -> navigator?.push(PaywallAssembly())
                    is ShowPermissionError -> {
                        val result = snackBarHostState.showSnackbar(
                            message = action.errorMessage,
                            actionLabel = getString(Res.string.settings),
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.permissionsController.openAppSettings()
                        }
                    }
                }
            }
        }
        BindEffect(permissionsController = viewModel.permissionsController)

        QueueScreen(
            state = state,
            snackBarHostState = snackBarHostState,
            onEvent = viewModel::onEvent
        )
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(resource = Res.string.queue)
            val icon = painterResource(resource = Res.drawable.ic_home)

            return remember { TabOptions(index = 0u, title = title, icon = icon) }
        }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalResourceApi::class
)
@Composable
private fun QueueScreen(
    state: QueueState,
    snackBarHostState: SnackbarHostState,
    onEvent: (QueueEvent) -> Unit
) {
    val userId = LocalUserId.current
    val isUserAdmin = LocalIsUserAdmin.current
    val haptic = LocalHapticFeedback.current

    val lazyListState = rememberLazyListState()
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 2 })

    state.userProfile?.let {
        QueueItemDetailsDialog(
            userProfile = state.userProfile,
            onDismissRequest = { onEvent(OnQueueItemDetailsDialogDismissRequest) }
        )
    }

    val reorderableLazyListState = rememberReorderableLazyColumnState(lazyListState) { from, to ->
        onEvent(
            OnQueueReordered(
                line = if (pagerState.currentPage == 0) Line.LEFT else Line.RIGHT,
                from = from.index,
                to = to.index
            )
        )
        haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
    }

    if (state.showLeaveQueueConfirmationDialog) {
        LeaveQueueConfirmationDialog(
            isUserAdmin = isUserAdmin,
            onDismissRequest = { onEvent(LeaveQueueConfirmationDialogDismissRequest) },
            onLeaveQueue = { onEvent(OnLeaveQueueConfirmed) }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(resource = Res.string.queue),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (!state.isConnected) {
                                onEvent(QueueEvent.ConnectToSession)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (state.isConnected) {
                                Icons.Default.Sync
                            } else {
                                Icons.Default.SyncProblem
                            },
                            tint = if (state.isConnected) {
                                Color.Green
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedContent(
                modifier = Modifier.padding(bottom = 16.dp),
                targetState = state.isQueueReordered
            ) { isQueueReordered ->
                if (isQueueReordered) {
                    ExtendedFloatingActionButton(
                        onClick = { onEvent(OnSaveReorderedQueueClicked) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        text = { Text("Save") }
                    )
                } else {
                    FloatingActionButton(
                        onClick = {
                            if (!state.isLoading && !state.isSessionStarting) {
                                userId?.let {
                                    onEvent(
                                        OnJoinClicked(
                                            userId = userId,
                                            line = Line.entries[pagerState.currentPage],
                                            isUserAdmin = isUserAdmin
                                        )
                                    )
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        AnimatedContent(
                            targetState = state.isLoading || state.isSessionStarting
                        ) { isLoading ->
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(pagerState = pagerState)

            HorizontalPager(state = pagerState) { page ->
                val queue = remember(state.leftQueue, state.rightQueue) {
                    if (page == 0) state.leftQueue else state.rightQueue
                }

                if (queue.isEmpty()) {
                    EmptyQueueContent()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 24.dp),
                        state = lazyListState,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp,
                            top = 2.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(key = { it.id }, items = queue) { item ->
                            val swipeEnabled = remember(
                                state.isLoading,
                                state.isConnected,
                                userId,
                                isUserAdmin
                            ) {
                                (isUserAdmin || userId == item.userId)
                                        && !state.isLoading && state.isConnected
                            }

                            ReorderableItem(
                                reorderableLazyListState = reorderableLazyListState,
                                key = item.id
                            ) {
                                SwipeToDeleteContainer(
                                    swipeEnabled = swipeEnabled,
                                    onDelete = { onEvent(OnQueueLeaved(item.id)) }
                                ) {
                                    QueueItem(
                                        firstName = item.firstName,
                                        lastName = item.lastName,
                                        photo = item.photo,
                                        showDraggableHandle = isUserAdmin,
                                        onPhotoClicked = { onEvent(OnUserPhotoClicked(it)) },
                                        onItemClicked = {
                                            item.userId?.let { onEvent(OnQueueItemClicked(it)) }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
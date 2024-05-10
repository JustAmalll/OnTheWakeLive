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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

import core.utils.filter
import full_size_photo.presentation.FullSizePhotoAssembly
import kotlinx.collections.immutable.ImmutableList
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.queue
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import paywall.presentation.PaywallAssembly
import queue.domain.model.Line
import queue.domain.model.QueueItem
import queue.presentation.admin.QueueAdminAssembly
import queue.presentation.details.QueueItemDetailsAssembly
import queue.presentation.list.QueueEvent.LeaveQueueConfirmationDialogDismissRequest
import queue.presentation.list.QueueEvent.OnJoinClicked
import queue.presentation.list.QueueEvent.OnLeaveQueueConfirmed
import queue.presentation.list.QueueEvent.OnQueueItemClicked
import queue.presentation.list.QueueEvent.OnQueueLeaved
import queue.presentation.list.QueueEvent.OnQueueReordered
import queue.presentation.list.QueueEvent.OnSaveReorderedQueueClicked
import queue.presentation.list.QueueEvent.OnUserPhotoClicked
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToFullSizePhotoScreen
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToPaywallScreen
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueAdminScreen
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueItemDetails
import queue.presentation.list.QueueViewModel.QueueAction.ShowError
import queue.presentation.list.components.EmptyQueueContent
import queue.presentation.list.components.LeaveQueueConfirmationDialog
import queue.presentation.list.components.QueueItem
import queue.presentation.list.components.SwipeToDeleteContainer
import queue.presentation.list.components.TabRow
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyColumnState

object QueueTab : Tab {

    @Composable
    override fun Content() {
        val viewModel: QueueViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current?.parent
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = Unit) {
            viewModel.onEvent(QueueEvent.OnViewAppeared)
        }

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    is NavigateToQueueItemDetails -> navigator?.push(
                        QueueItemDetailsAssembly(userId = action.userId)
                    )

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
                            viewModel.onEvent(QueueEvent.OnReconnectClicked)
                        }
                    }

                    NavigateToPaywallScreen -> navigator?.push(PaywallAssembly())
                }
            }
        }
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
            val icon = rememberVectorPainter(image = Icons.Default.Home)

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
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 2 })

    if (state.showLeaveQueueConfirmationDialog) {
        LeaveQueueConfirmationDialog(
            isUserAdmin = state.isUserAdmin,
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        },
        floatingActionButton = {
            AnimatedContent(targetState = state.isQueueReordered) { isQueueReordered ->
                if (isQueueReordered) {
                    ExtendedFloatingActionButton(
                        onClick = { onEvent(OnSaveReorderedQueueClicked) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null
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
                        }
                    ) {
                        AnimatedContent(
                            targetState = state.isLoading || state.isSessionStarting
                        ) { isLoading ->
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
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
                when (page) {
                    0 -> QueueContent(
                        queue = remember(state.queue) {
                            state.queue.filter { it.line == Line.LEFT }
                        },
                        onQueueItemClicked = { onEvent(OnQueueItemClicked(it)) },
                        onQueueLeaved = { onEvent(OnQueueLeaved(it)) },
                        onUserPhotoClicked = { onEvent(OnUserPhotoClicked(it)) },
                        onQueueReordered = { from, to ->
                            onEvent(OnQueueReordered(from = from, to = to))
                        }
                    )

                    1 -> QueueContent(
                        queue = remember(state.queue) {
                            state.queue.filter { it.line == Line.RIGHT }
                        },
                        onQueueItemClicked = { onEvent(OnQueueItemClicked(it)) },
                        onQueueLeaved = { onEvent(OnQueueLeaved(it)) },
                        onUserPhotoClicked = { onEvent(OnUserPhotoClicked(it)) },
                        onQueueReordered = { from, to ->
                            onEvent(OnQueueReordered(from = from, to = to))
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun QueueContent(
    queue: ImmutableList<QueueItem>,
    onQueueItemClicked: (Int) -> Unit,
    onQueueLeaved: (Int) -> Unit,
    onUserPhotoClicked: (String) -> Unit,
    onQueueReordered: (from: Int, to: Int) -> Unit
) {
    val isUserAdmin = LocalIsUserAdmin.current
    val userId = LocalUserId.current

    val haptic = LocalHapticFeedback.current
    val lazyListState = rememberLazyListState()

    val reorderableLazyColumnState = rememberReorderableLazyColumnState(lazyListState) { from, to ->
        onQueueReordered(from.index, to.index)
        haptic.performHapticFeedback(hapticFeedbackType = HapticFeedbackType.LongPress)
    }

    if (queue.isEmpty()) {
        EmptyQueueContent()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(key = { it.id }, items = queue) { item ->
                ReorderableItem(
                    reorderableLazyListState = reorderableLazyColumnState,
                    key = item.id
                ) {
                    SwipeToDeleteContainer(
                        swipeEnabled = isUserAdmin || userId == item.userId,
                        onDelete = { onQueueLeaved(item.id) }
                    ) {
                        QueueItem(
                            firstName = item.firstName,
                            lastName = item.lastName,
                            photo = item.photo,
                            showDraggableHandle = isUserAdmin,
                            onItemClicked = { item.userId?.let(onQueueItemClicked) },
                            onPhotoClicked = { item.photo?.let(onUserPhotoClicked) }
                        )
                    }
                }
            }
        }
    }
}
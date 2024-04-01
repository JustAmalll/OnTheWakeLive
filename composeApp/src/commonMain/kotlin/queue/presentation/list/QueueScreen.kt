package queue.presentation.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.utils.filter
import kotlinx.collections.immutable.ImmutableList
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.queue
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import queue.domain.module.Line
import queue.domain.module.QueueItem
import queue.presentation.details.QueueItemDetailsAssembly
import queue.presentation.list.QueueEvent.LeaveQueueConfirmationDialogDismissRequest
import queue.presentation.list.QueueEvent.OnJoinClicked
import queue.presentation.list.QueueEvent.OnLeaveQueueConfirmed
import queue.presentation.list.QueueEvent.OnQueueItemClicked
import queue.presentation.list.QueueEvent.OnQueueLeaved
import queue.presentation.list.QueueEvent.OnUserPhotoClicked
import queue.presentation.list.QueueViewModel.QueueAction.NavigateToQueueItemDetails
import queue.presentation.list.components.LeaveQueueConfirmationDialog
import queue.presentation.list.components.QueueItem
import queue.presentation.list.components.SwipeToDeleteContainer
import queue.presentation.list.components.TabRow

object QueueAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: QueueViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                when (action) {
                    is NavigateToQueueItemDetails -> navigator?.push(
                        QueueItemDetailsAssembly(queueItemId = action.queueItemId)
                    )
                }
            }
        }

        QueueScreen(state = state, onEvent = viewModel::onEvent)
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
    onEvent: (QueueEvent) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 2 })

    if (state.showLeaveQueueConfirmationDialog) {
        LeaveQueueConfirmationDialog(
            isUserAdmin = state.isUserAdmin,
            onDismissRequest = { onEvent(LeaveQueueConfirmationDialogDismissRequest) },
            onLeaveQueue = { onEvent(OnLeaveQueueConfirmed) }
        )
    }

    Scaffold(
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
            FloatingActionButton(
                onClick = {
                    onEvent(OnJoinClicked(line = Line.entries[pagerState.currentPage]))
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { paddingValue ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
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
                        onUserPhotoClicked = { onEvent(OnUserPhotoClicked(it)) }
                    )

                    1 -> QueueContent(
                        queue = remember(state.queue) {
                            state.queue.filter { it.line == Line.RIGHT }
                        },
                        onQueueItemClicked = { onEvent(OnQueueItemClicked(it)) },
                        onQueueLeaved = { onEvent(OnQueueLeaved(it)) },
                        onUserPhotoClicked = { onEvent(OnUserPhotoClicked(it)) }
                    )
                }
            }
        }
    }
}


@Composable
private fun QueueContent(
    queue: ImmutableList<QueueItem>,
    onQueueItemClicked: (String) -> Unit,
    onQueueLeaved: (String) -> Unit,
    onUserPhotoClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(key = { it.id }, items = queue) { item ->
            SwipeToDeleteContainer(
                swipeEnabled = true,
                onDelete = { onQueueLeaved(item.id) }
            ) {
                QueueItem(
                    firstName = item.firstName,
                    lastName = item.lastName,
                    profilePictureUri = item.profilePictureUri,
                    onItemClicked = { onQueueItemClicked(item.id) },
                    onUserPhotoClicked = onUserPhotoClicked
                )
            }
        }
    }
}
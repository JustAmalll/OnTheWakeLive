package com.onthewake.onthewakelive.feature_queue.presentation.queue_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.StandardImageView
import com.onthewake.onthewakelive.dataStore
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.AdminDialog
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.EmptyContent
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.util.Constants.FIRST_ADMIN_USER_ID
import com.onthewake.onthewakelive.util.Constants.SECOND_ADMIN_USER_ID
import com.onthewake.onthewakelive.util.UserProfileSerializer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@ExperimentalMaterial3Api
@ExperimentalPagerApi
@Composable
fun QueueScreen(
    viewModel: QueueViewModel = hiltViewModel(),
    navController: NavHostController,
    imageLoader: ImageLoader
) {

    val state = viewModel.state.value
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = 2)
    val showDialog = viewModel.showDialog
    val haptic = LocalHapticFeedback.current
    val snackBarHostState = remember { SnackbarHostState() }
    val userId = viewModel.userId

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)

    val dataStore = remember {
        context.dataStore.data
    }.collectAsState(initial = UserProfileSerializer.defaultValue)

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = surfaceColor, darkIcons = !darkTheme
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.snackBarEvent.collectLatest { message ->
            snackBarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.snackBarWithActionEvent.collectLatest { deletedItem ->
            val result = snackBarHostState.showSnackbar(
                message = if (userId == FIRST_ADMIN_USER_ID || userId == SECOND_ADMIN_USER_ID)
                    context.getString(R.string.admin_delete_message)
                else context.getString(R.string.delete_message),
                actionLabel = context.getString(R.string.undo),
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.addToQueue(
                    leftQueue = deletedItem.leftQueue,
                    firstName = deletedItem.firstName,
                    timestamp = deletedItem.timestamp
                )
            }
        }
    }

    if (showDialog.value) {
        AdminDialog(
            showDialog = { showDialog.value = it },
            onAddClicked = { leftQueue, firstName ->
                viewModel.addToQueue(
                    leftQueue = leftQueue.toString(),
                    firstName = firstName,
                    timestamp = System.currentTimeMillis()
                )
            },
            queue = state.queue
        )
    }

    if (state.isQueueLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(40.dp)
            ) {
                CircularProgressIndicator()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.queue),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = surfaceColor,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = if (userId == FIRST_ADMIN_USER_ID || userId == SECOND_ADMIN_USER_ID)
                    Modifier.padding(bottom = 0.dp) else Modifier.padding(bottom = 76.dp),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    if (!viewModel.isAdding.value) {
                        if (userId == FIRST_ADMIN_USER_ID || userId == SECOND_ADMIN_USER_ID) {
                            showDialog.value = true
                        } else {
                            if (pagerState.currentPage == 0) viewModel.addToQueue(
                                leftQueue = "true",
                                firstName = dataStore.value.firstName,
                                timestamp = System.currentTimeMillis()
                            )
                            else viewModel.addToQueue(
                                leftQueue = "false",
                                firstName = dataStore.value.firstName,
                                timestamp = System.currentTimeMillis()
                            )
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_icon),
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) viewModel.disconnect()
                else if (event == Lifecycle.Event.ON_CREATE) viewModel.connectToQueue()
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Tabs(pagerState = pagerState)

            // Content
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> QueueLeftContent(
                        state = state,
                        userId = userId,
                        onDetailsClicked = { queueItemId ->
                            navController.navigate(
                                Screen.QueueDetailsScreen.passItemId(itemId = queueItemId)
                            )
                        },
                        onSwipeToDelete = { viewModel.deleteQueueItem(it) },
                        imageLoader = imageLoader
                    )
                    1 -> QueueRightContent(
                        state = state,
                        userId = userId,
                        onDetailsClicked = { queueItemId ->
                            navController.navigate(
                                Screen.QueueDetailsScreen.passItemId(itemId = queueItemId)
                            )
                        },
                        onSwipeToDelete = { viewModel.deleteQueueItem(it) },
                        imageLoader = imageLoader
                    )
                }
            }
        }
    }
}


@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {

    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val list = listOf(
        context.getString(R.string.left_line) to Icons.Default.ArrowBack,
        context.getString(R.string.right_line) to Icons.Default.ArrowForward
    )

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                icon = {
                    Icon(
                        imageVector = list[index].second,
                        contentDescription = null,
                        tint = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                text = {
                    Text(
                        list[index].first,
                        color = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    scope.launch { pagerState.animateScrollToPage(index) }
                }
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun QueueLeftContent(
    state: QueueState,
    userId: String?,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.queue.none { it.leftQueue == "true" } && !state.isQueueLoading) {
            EmptyContent(modifier = Modifier.align(Alignment.Center))
        }
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            reverseLayout = true
        ) {
            items(state.queue) { item ->
                if (item.leftQueue == "true") {
                    QueueItem(
                        queueItem = item,
                        imageLoader = imageLoader,
                        userId = userId,
                        onDetailsClicked = onDetailsClicked,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun QueueRightContent(
    state: QueueState,
    userId: String?,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val queue = state.queue.sortedWith(compareByDescending { it.timestamp })
        if (queue.none { it.leftQueue == "false" } && !state.isQueueLoading) {
            EmptyContent(modifier = Modifier.align(Alignment.Center))
        }
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            reverseLayout = true
        ) {
            items(queue) { item ->
                if (item.leftQueue == "false") {
                    QueueItem(
                        queueItem = item,
                        imageLoader = imageLoader,
                        userId = userId,
                        onDetailsClicked = onDetailsClicked,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun QueueItem(
    queueItem: Queue,
    userId: String?,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    val swipeToDelete = SwipeAction(
        icon = {
            Icon(
                modifier = Modifier.padding(end = 20.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_icon),
                tint = MaterialTheme.colorScheme.onError
            )
        },
        background = MaterialTheme.colorScheme.error,
        onSwipe = {
            onSwipeToDelete(queueItem.id)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    )

    Surface(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .padding(vertical = 6.dp)
            .clip(shape = RoundedCornerShape(12.dp))
    ) {
        if (queueItem.userId == userId ||
            userId == FIRST_ADMIN_USER_ID ||
            userId == SECOND_ADMIN_USER_ID
        ) {
            SwipeableActionsBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        if (queueItem.userId != FIRST_ADMIN_USER_ID &&
                            queueItem.userId != SECOND_ADMIN_USER_ID
                        ) onDetailsClicked(queueItem.id)
                    },
                startActions = listOf(swipeToDelete)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(shape = MaterialTheme.shapes.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (queueItem.userId != FIRST_ADMIN_USER_ID &&
                        queueItem.userId != SECOND_ADMIN_USER_ID
                    ) {
                        StandardImageView(
                            imageLoader = imageLoader,
                            model = queueItem.profilePictureFileName
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = queueItem.firstName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = queueItem.lastName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = queueItem.firstName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    if (queueItem.userId != FIRST_ADMIN_USER_ID &&
                        queueItem.userId != SECOND_ADMIN_USER_ID
                    ) onDetailsClicked(queueItem.id)
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(shape = MaterialTheme.shapes.medium),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (queueItem.userId != FIRST_ADMIN_USER_ID &&
                        queueItem.userId != SECOND_ADMIN_USER_ID
                    ) {
                        StandardImageView(
                            imageLoader = imageLoader,
                            model = queueItem.profilePictureFileName
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = queueItem.firstName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = queueItem.lastName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = queueItem.firstName,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

package com.onthewake.onthewakelive.feature_queue.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.pager.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_queue.presentation.components.CustomDialog
import com.onthewake.onthewakelive.feature_queue.presentation.components.EmptyContent
import com.onthewake.onthewakelive.feature_queue.presentation.components.LoadingDialog
import com.onthewake.onthewakelive.ui.theme.BackgroundColor
import com.onthewake.onthewakelive.ui.theme.ItemBgColor
import com.onthewake.onthewakelive.ui.theme.Primary
import com.onthewake.onthewakelive.ui.theme.darkThemeBgColor
import com.onthewake.onthewakelive.util.Constants.ADMIN_USER_ID
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@ExperimentalPagerApi
@Composable
fun QueueScreen(
    viewModel: QueueViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = 2)
    val showDialog = remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val userId = viewModel.userId

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Primary, darkIcons = false
        )
        systemUiController.setNavigationBarColor(
            color = if (darkTheme) darkThemeBgColor else Color.White,
            darkIcons = !darkTheme
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.snackBarEvent.collectLatest { message ->
            snackBarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(key1 = true) {

        viewModel.snackBarWithActionEvent.collectLatest { deletedItem ->

            val result = snackBarHostState.showSnackbar(
                message = context.getString(R.string.deleted_message),
                actionLabel = context.getString(R.string.undo)
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    viewModel.addToQueue(
                        leftQueue = deletedItem.leftQueue,
                        firstName = deletedItem.firstName,
                        timestamp = deletedItem.timestamp
                    )
                }
                SnackbarResult.Dismissed -> {}
            }
        }
    }

    if (showDialog.value) {
        CustomDialog(
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

    if (state.isQueueLoading) LoadingDialog()

    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackBarHostState),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (userId == ADMIN_USER_ID) {
                        showDialog.value = true
                    } else {
                        viewModel.firstName?.let { firstName ->
                            if (pagerState.currentPage == 0) viewModel.addToQueue(
                                leftQueue = "true",
                                firstName = firstName,
                                timestamp = System.currentTimeMillis()
                            )
                            else viewModel.addToQueue(
                                leftQueue = "false",
                                firstName = firstName,
                                timestamp = System.currentTimeMillis()
                            )
                        }
                    }
                },
                backgroundColor = Primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_icon),
                    tint = Color.White
                )
            }
        },
        topBar = {
            TopAppBar(backgroundColor = Primary, elevation = 0.dp) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier.padding(all = 5.dp),
                        text = stringResource(id = R.string.queue),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    ) { paddingValues ->

        val lifecycleOwner = LocalLifecycleOwner.current

        DisposableEffect(key1 = lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) viewModel.connectToQueue()
                else if (event == Lifecycle.Event.ON_STOP) viewModel.disconnect()
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .background(MaterialTheme.colors.BackgroundColor),
        ) {
            Tabs(pagerState = pagerState)

            // Content
            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> QueueLeftContent(
                        state = state,
                        onSwipeToDelete = { viewModel.deleteQueueItem(it) },
                        userId = userId
                    )
                    1 -> QueueRightContent(
                        state = state,
                        onSwipeToDelete = { viewModel.deleteQueueItem(it) },
                        userId = userId
                    )
                }
            }

        }
    }
}

@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {

    val context = LocalContext.current

    val list = listOf(
        context.getString(R.string.left_line) to Icons.Default.ArrowBack,
        context.getString(R.string.right_line) to Icons.Default.ArrowForward
    )

    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Primary,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = Color.White
            )
        }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                icon = {
                    Icon(imageVector = list[index].second, contentDescription = null)
                },
                text = {
                    Text(
                        list[index].first,
                        color = if (pagerState.currentPage == index) Color.White else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@Composable
fun QueueLeftContent(
    state: QueueState,
    onSwipeToDelete: (String) -> Unit,
    userId: String?
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
                        queueItemFirstName = item.firstName,
                        queueItemId = item.id,
                        queueItemUserId = item.userId,
                        userId = userId,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
        }
    }
}

@Composable
fun QueueRightContent(
    state: QueueState,
    onSwipeToDelete: (String) -> Unit,
    userId: String?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.queue.none { it.leftQueue == "false" } && !state.isQueueLoading) {
            EmptyContent(modifier = Modifier.align(Alignment.Center))
        }
        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            reverseLayout = true
        ) {
            items(state.queue) { item ->
                if (item.leftQueue == "false") {
                    QueueItem(
                        queueItemFirstName = item.firstName,
                        queueItemId = item.id,
                        queueItemUserId = item.userId,
                        userId = userId,
                        onSwipeToDelete = onSwipeToDelete
                    )
                }
            }
        }
    }
}

@Composable
fun QueueItem(
    queueItemFirstName: String,
    queueItemId: String,
    queueItemUserId: String,
    userId: String?,
    onSwipeToDelete: (String) -> Unit
) {

    val swipeToDelete = SwipeAction(
        icon = {
            Icon(
                modifier = Modifier.padding(end = 20.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.delete_icon),
                tint = Color.White
            )
        },
        background = Color.Red,
        onSwipe = { onSwipeToDelete(queueItemId) },
    )

    Surface(
        elevation = 4.dp,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        if (queueItemUserId == userId || userId == ADMIN_USER_ID) {
            SwipeableActionsBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colors.ItemBgColor),
                startActions = listOf(swipeToDelete),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.ItemBgColor)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(text = queueItemFirstName)
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colors.ItemBgColor
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.ItemBgColor)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(text = queueItemFirstName)
                }
            }
        }
    }
}
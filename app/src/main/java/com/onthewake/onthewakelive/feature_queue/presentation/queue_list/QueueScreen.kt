@file:OptIn(ExperimentalMaterial3Api::class)

package com.onthewake.onthewakelive.feature_queue.presentation.queue_list

import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
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
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.AnimatedShimmer
import com.onthewake.onthewakelive.core.presentation.StandardImageView
import com.onthewake.onthewakelive.dataStore
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.AdminDialog
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.EmptyContent
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.TabLayout
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.util.Constants.ADMIN_IDS
import com.onthewake.onthewakelive.util.UserProfileSerializer
import com.onthewake.onthewakelive.util.openNotificationSettings
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@ExperimentalPagerApi
@Composable
fun QueueScreen(
    viewModel: QueueViewModel = hiltViewModel(),
    navController: NavHostController,
    imageLoader: ImageLoader
) {

    val state = viewModel.state.value
    val userId = viewModel.userId
    val showDialog = viewModel.showDialog

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val pagerState = rememberPagerState(pageCount = 2, initialPage = 1)
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)

    var hasNotificationPermission by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else mutableStateOf(true)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->

            hasNotificationPermission = isGranted

            if (!isGranted) {
                scope.launch {
                    val result = snackBarHostState.showSnackbar(
                        "Разрешите показ уведомений",
                        actionLabel = "Settings"
                    )
                    if (result == SnackbarResult.ActionPerformed)
                        context.openNotificationSettings()
                }
            }
        }
    )

    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

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
                message = if (userId in ADMIN_IDS) context.getString(R.string.admin_delete_message)
                else context.getString(R.string.delete_message),
                actionLabel = context.getString(R.string.undo),
                duration = SnackbarDuration.Short
            )

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.addToQueue(
                    isLeftQueue = deletedItem.isLeftQueue,
                    firstName = deletedItem.firstName,
                    timestamp = deletedItem.timestamp
                )
            }
        }
    }

    if (showDialog.value) AdminDialog(
        showDialog = { showDialog.value = it },
        onAddClicked = { isLeftQueue, firstName ->
            viewModel.addToQueue(
                isLeftQueue = isLeftQueue,
                firstName = firstName,
                timestamp = System.currentTimeMillis()
            )
        },
        queue = state.queue
    )

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
            if (!state.isQueueLoading) FloatingActionButton(
                modifier = Modifier.padding(bottom = if (userId in ADMIN_IDS) 0.dp else 76.dp),
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                    if (!viewModel.isAdding.value && hasNotificationPermission) {
                        if (userId in ADMIN_IDS) showDialog.value = true
                        else viewModel.addToQueue(
                            isLeftQueue = pagerState.currentPage == 0,
                            firstName = dataStore.value.firstName,
                            timestamp = System.currentTimeMillis()
                        )
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
                .padding(padding)
        ) {
            TabLayout(pagerState = pagerState)

            if (state.isQueueLoading) {
                repeat(5) {
                    AnimatedShimmer()
                }
            } else {

                // Content
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.padding(bottom = if (userId in ADMIN_IDS) 0.dp else 76.dp)
                ) { page ->
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
}

@Composable
fun QueueLeftContent(
    state: QueueState,
    userId: String?,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val leftQueue = state.queue.filter { it.isLeftQueue }
        if (leftQueue.isEmpty()) EmptyContent(modifier = Modifier.align(Alignment.Center))

        LazyColumn(
            contentPadding = PaddingValues(10.dp),
            reverseLayout = true
        ) {
            items(leftQueue) { item ->
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

@Composable
fun QueueRightContent(
    state: QueueState,
    userId: String?,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val rightQueue = state.queue.filter { !it.isLeftQueue }
        if (rightQueue.isEmpty()) EmptyContent(modifier = Modifier.align(Alignment.Center))

        AnimatedVisibility(
            visible = !state.isQueueLoading,
            enter = fadeIn() + expandVertically()
        ) {
            LazyColumn(
                contentPadding = PaddingValues(10.dp),
                reverseLayout = true
            ) {
                items(rightQueue) { item ->
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

@Composable
fun QueueItem(
    queueItem: Queue,
    userId: String?,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit,
    onSwipeToDelete: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val isAdminQueueItem = queueItem.userId in ADMIN_IDS

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

    Spacer(modifier = Modifier.height(12.dp))

    if (queueItem.userId == userId || userId in ADMIN_IDS) {
        SwipeableActionsBox(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { if (!isAdminQueueItem) onDetailsClicked(queueItem.id) },
            startActions = listOf(swipeToDelete),
            swipeThreshold = 120.dp
        ) {
            if (isAdminQueueItem) UserAddedByAdminItem(firstName = queueItem.firstName)
            else DefaultUserItem(
                queueItem = queueItem,
                imageLoader = imageLoader,
                onDetailsClicked = onDetailsClicked
            )
        }
    } else {
        if (isAdminQueueItem) UserAddedByAdminItem(firstName = queueItem.firstName)
        else DefaultUserItem(
            queueItem = queueItem,
            imageLoader = imageLoader,
            onDetailsClicked = onDetailsClicked
        )
    }
}

@Composable
fun DefaultUserItem(
    queueItem: Queue,
    imageLoader: ImageLoader,
    onDetailsClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailsClicked(queueItem.id) }
            .clip(shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StandardImageView(
            imageLoader = imageLoader,
            model = queueItem.profilePictureUri
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
    }
}

@Composable
fun UserAddedByAdminItem(firstName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = firstName,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
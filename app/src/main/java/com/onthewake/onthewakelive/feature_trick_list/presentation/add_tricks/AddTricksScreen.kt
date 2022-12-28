package com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.components.StandardLoadingView
import com.onthewake.onthewakelive.feature_trick_list.domain.model.TrickList
import com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks.components.DefaultFilterChip
import com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks.components.SelectableItem
import com.onthewake.onthewakelive.feature_trick_list.presentation.components.CategoryTextView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@Composable
fun AddTricksScreen(
    navController: NavHostController,
    viewModel: AddTricksViewModel = hiltViewModel()
) {

    val trickList = viewModel.state.allTrickList
    val userTrickList = viewModel.state.userTrickList

    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val snackBarHostState = remember { SnackbarHostState() }

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surface

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    val scope = rememberCoroutineScope()

    var spinsSelected by remember { mutableStateOf(true) }
    var raileyTricksSelected by remember { mutableStateOf(true) }
    var backRollTricksSelected by remember { mutableStateOf(true) }
    var frontFlipTricksSelected by remember { mutableStateOf(true) }
    var frontRollTricksSelected by remember { mutableStateOf(true) }
    var tantrumTricksSelected by remember { mutableStateOf(true) }
    var whipTricksSelected by remember { mutableStateOf(true) }
    var grabsSelected by remember { mutableStateOf(true) }
    var railsSelected by remember { mutableStateOf(true) }

    val expandedFab by remember {
        derivedStateOf { !listState.isScrollInProgress }
    }

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

    BackHandler {
        if (sheetState.isCollapsed) navController.popBackStack()
        else scope.launch { sheetState.collapse() }
    }

    if (trickList != null && userTrickList != null && !viewModel.state.isLoading) {

        var spins by remember {
            mutableStateOf(trickList.spins.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.spins.contains(it)
                )
            })
        }
        var raileyTricks by remember {
            mutableStateOf(trickList.raileyTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.raileyTricks.contains(it)
                )
            })
        }
        var backRollTricks by remember {
            mutableStateOf(trickList.backRollTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.backRollTricks.contains(it)
                )
            })
        }
        var frontFlipTricks by remember {
            mutableStateOf(trickList.frontFlipTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.frontFlipTricks.contains(it)
                )
            })
        }
        var frontRollTricks by remember {
            mutableStateOf(trickList.frontRollTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.frontRollTricks.contains(it)
                )
            })
        }
        var tantrumTricks by remember {
            mutableStateOf(trickList.tantrumTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.tantrumTricks.contains(it)
                )
            })
        }
        var whipTricks by remember {
            mutableStateOf(trickList.whipTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.whipTricks.contains(it)
                )
            })
        }
        var grabs by remember {
            mutableStateOf(trickList.grabs.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.grabs.contains(it)
                )
            })
        }
        var rails by remember {
            mutableStateOf(trickList.rails.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.rails.contains(it)
                )
            })
        }

        BottomSheetScaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    scrollBehavior = scrollBehavior,
                    title = { Text(text = stringResource(R.string.trick_list)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.arrow_back_icon)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                if (sheetState.isCollapsed) sheetState.expand()
                                else sheetState.collapse()
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = stringResource(R.string.filter_icon)
                            )
                        }
                    }
                )
            },
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            sheetElevation = 1.dp,
            sheetBackgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            sheetContent = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(top = 10.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .size(width = 32.dp, height = 4.dp)
                            .align(Alignment.TopCenter),
                        backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        elevation = 0.dp
                    ) {}
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Filter Tricks",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                        Row(modifier = Modifier.padding(top = 16.dp)) {
                            DefaultFilterChip(
                                label = stringResource(R.string.spins),
                                selected = spinsSelected,
                                onClick = { spinsSelected = !spinsSelected }
                            )
                            DefaultFilterChip(
                                label = stringResource(R.string.railey),
                                selected = raileyTricksSelected,
                                onClick = { raileyTricksSelected = !raileyTricksSelected }
                            )
                            DefaultFilterChip(
                                label = stringResource(R.string.back_roll),
                                selected = backRollTricksSelected,
                                onClick = { backRollTricksSelected = !backRollTricksSelected }
                            )
                        }
                        Row {
                            DefaultFilterChip(
                                label = stringResource(R.string.front_flip),
                                selected = frontFlipTricksSelected,
                                onClick = { frontFlipTricksSelected = !frontFlipTricksSelected }
                            )
                            DefaultFilterChip(
                                label = stringResource(R.string.front_roll),
                                selected = frontRollTricksSelected,
                                onClick = { frontRollTricksSelected = !frontRollTricksSelected }
                            )
                            DefaultFilterChip(
                                label = stringResource(R.string.tantrum),
                                selected = tantrumTricksSelected,
                                onClick = { tantrumTricksSelected = !tantrumTricksSelected }
                            )
                        }
                        Row {
                            DefaultFilterChip(
                                label = stringResource(R.string.whip),
                                selected = whipTricksSelected,
                                onClick = { whipTricksSelected = !whipTricksSelected }
                            )
                            DefaultFilterChip(
                                label = stringResource(R.string.grabs),
                                selected = grabsSelected,
                                onClick = { grabsSelected = !grabsSelected }
                            )
                            DefaultFilterChip(
                                label = stringResource(R.string.rails),
                                selected = railsSelected,
                                onClick = { railsSelected = !railsSelected }
                            )
                        }
                    }
                }
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        val selectedSpins = spins.filter { it.isSelected }
                        val selectedRaileyTricks = raileyTricks.filter { it.isSelected }
                        val selectedBackRollTricks = backRollTricks.filter { it.isSelected }
                        val selectedFrontFlipTricks = frontFlipTricks.filter { it.isSelected }
                        val selectedFrontRollTricks = frontRollTricks.filter { it.isSelected }
                        val selectedTantrumTricks = tantrumTricks.filter { it.isSelected }
                        val selectedWhipTricks = whipTricks.filter { it.isSelected }
                        val selectedGrabs = grabs.filter { it.isSelected }
                        val selectedRails = rails.filter { it.isSelected }

                        val selectedTrickList = TrickList(
                            spins = selectedSpins.map { it.toTrickItem() },
                            raileyTricks = selectedRaileyTricks.map { it.toTrickItem() },
                            backRollTricks = selectedBackRollTricks.map { it.toTrickItem() },
                            frontFlipTricks = selectedFrontFlipTricks.map { it.toTrickItem() },
                            frontRollTricks = selectedFrontRollTricks.map { it.toTrickItem() },
                            tantrumTricks = selectedTantrumTricks.map { it.toTrickItem() },
                            whipTricks = selectedWhipTricks.map { it.toTrickItem() },
                            grabs = selectedGrabs.map { it.toTrickItem() },
                            rails = selectedRails.map { it.toTrickItem() },
                        )

                        viewModel.addTrick(selectedTrickList)
                    },
                    modifier = Modifier.padding(
                        bottom = BottomSheetScaffoldDefaults.SheetPeekHeight + 16.dp
                    ),
                    expanded = expandedFab,
                    icon = { Icon(Icons.Filled.Done, stringResource(R.string.icon_done)) },
                    text = { Text(text = stringResource(R.string.finish_setup)) },
                )
            },
        ) { paddingValues ->

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                if (spinsSelected) {
                    item {
                        CategoryTextView(text = stringResource(id = R.string.spins))
                    }
                    itemsIndexed(spins) { index, spinItem ->
                        SelectableItem(
                            title = spinItem.name,
                            subtitle = spinItem.description,
                            selected = spinItem.isSelected,
                            onClick = { spins = onItemClick(spins, index) }
                        )
                    }
                }
                if (raileyTricksSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.railey_tricks))
                    }
                    itemsIndexed(raileyTricks) { index, raileyTrickItem ->
                        SelectableItem(
                            title = raileyTrickItem.name,
                            subtitle = raileyTrickItem.description,
                            selected = raileyTrickItem.isSelected,
                            onClick = { raileyTricks = onItemClick(raileyTricks, index) }
                        )
                    }
                }
                if (backRollTricksSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.back_roll_tricks))
                    }
                    itemsIndexed(backRollTricks) { index, backRollTrick ->
                        SelectableItem(
                            title = backRollTrick.name,
                            subtitle = backRollTrick.description,
                            selected = backRollTrick.isSelected,
                            onClick = { backRollTricks = onItemClick(backRollTricks, index) }
                        )
                    }
                }
                if (frontFlipTricksSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.front_flip_tricks))
                    }
                    itemsIndexed(frontFlipTricks) { index, frontFlipTrick ->
                        SelectableItem(
                            title = frontFlipTrick.name,
                            subtitle = frontFlipTrick.description,
                            selected = frontFlipTrick.isSelected,
                            onClick = { frontFlipTricks = onItemClick(frontFlipTricks, index) }
                        )
                    }
                }
                if (frontRollTricksSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.front_roll_tricks))
                    }
                    itemsIndexed(frontRollTricks) { index, frontRollTrick ->
                        SelectableItem(
                            title = frontRollTrick.name,
                            subtitle = frontRollTrick.description,
                            selected = frontRollTrick.isSelected,
                            onClick = { frontRollTricks = onItemClick(frontRollTricks, index) }
                        )
                    }
                }
                if (tantrumTricksSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.tantrum_tricks))
                    }
                    itemsIndexed(tantrumTricks) { index, tantrumTrick ->
                        SelectableItem(
                            title = tantrumTrick.name,
                            subtitle = tantrumTrick.description,
                            selected = tantrumTrick.isSelected,
                            onClick = { tantrumTricks = onItemClick(tantrumTricks, index) }
                        )
                    }
                }
                if (whipTricksSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.whip_tricks))
                    }
                    itemsIndexed(whipTricks) { index, whipTrick ->
                        SelectableItem(
                            title = whipTrick.name,
                            subtitle = whipTrick.description,
                            selected = whipTrick.isSelected,
                            onClick = { whipTricks = onItemClick(whipTricks, index) }
                        )
                    }
                }
                if (grabsSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.grabs))
                    }
                    itemsIndexed(grabs) { index, grab ->
                        SelectableItem(
                            title = grab.name,
                            subtitle = grab.description,
                            selected = grab.isSelected,
                            onClick = { grabs = onItemClick(grabs, index) }
                        )
                    }
                }
                if (railsSelected) {
                    item {
                        CategoryTextView(text = stringResource(R.string.rails))
                    }
                    itemsIndexed(rails) { index, railItem ->
                        SelectableItem(
                            title = railItem.name,
                            subtitle = railItem.description,
                            selected = railItem.isSelected,
                            onClick = { rails = onItemClick(rails, index) }
                        )
                    }
                }
            }
        }
    } else StandardLoadingView()
}

fun onItemClick(
    itemList: List<TrickItemState>, index: Int
): List<TrickItemState> = itemList.mapIndexed { j, items ->
    if (index == j) items.copy(isSelected = !items.isSelected) else items
}
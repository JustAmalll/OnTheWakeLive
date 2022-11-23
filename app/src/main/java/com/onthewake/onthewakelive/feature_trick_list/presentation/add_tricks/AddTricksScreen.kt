package com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_trick_list.data.remote.dto.TrickListDto
import com.onthewake.onthewakelive.feature_trick_list.presentation.TrickItemState
import com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks.features.DefaultFilterChip
import com.onthewake.onthewakelive.feature_trick_list.presentation.add_tricks.features.SelectableItem
import com.onthewake.onthewakelive.feature_trick_list.presentation.toTrickItemDto
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

    val listState = rememberLazyListState()

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

    if (trickList != null && userTrickList != null) {

        var spinItems by remember {
            mutableStateOf(trickList.spins.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.spins.contains(it)
                )
            })
        }
        var raileyTrickItems by remember {
            mutableStateOf(trickList.raileyTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.raileyTricks.contains(it)
                )
            })
        }
        var backRollTrickItems by remember {
            mutableStateOf(trickList.backRollTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.backRollTricks.contains(it)
                )
            })
        }
        var frontFlipTrickItems by remember {
            mutableStateOf(trickList.frontFlipTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.frontFlipTricks.contains(it)
                )
            })
        }
        var frontRollTrickItems by remember {
            mutableStateOf(trickList.frontRollTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.frontRollTricks.contains(it)
                )
            })
        }
        var tantrumTrickItems by remember {
            mutableStateOf(trickList.tantrumTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.tantrumTricks.contains(it)
                )
            })
        }
        var whipTrickItems by remember {
            mutableStateOf(trickList.whipTricks.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.whipTricks.contains(it)
                )
            })
        }
        var grabItems by remember {
            mutableStateOf(trickList.grabs.map {
                TrickItemState(
                    name = it.name,
                    description = it.description,
                    isSelected = userTrickList.grabs.contains(it)
                )
            })
        }
        var railItems by remember {
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
                    title = { Text(text = "Trick List") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Arrow Back"
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
                                contentDescription = "Filter Icon"
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
                                label = "Spins",
                                selected = spinsSelected,
                                onClick = { spinsSelected = !spinsSelected }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DefaultFilterChip(
                                label = "Railey",
                                selected = raileyTricksSelected,
                                onClick = { raileyTricksSelected = !raileyTricksSelected }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DefaultFilterChip(
                                label = "Back Roll",
                                selected = backRollTricksSelected,
                                onClick = { backRollTricksSelected = !backRollTricksSelected }
                            )
                        }
                        Row {
                            DefaultFilterChip(
                                label = "Front Flip",
                                selected = frontFlipTricksSelected,
                                onClick = { frontFlipTricksSelected = !frontFlipTricksSelected }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DefaultFilterChip(
                                label = "Front Roll",
                                selected = frontRollTricksSelected,
                                onClick = { frontRollTricksSelected = !frontRollTricksSelected }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DefaultFilterChip(
                                label = "Tantrum",
                                selected = tantrumTricksSelected,
                                onClick = { tantrumTricksSelected = !tantrumTricksSelected }
                            )
                        }
                        Row {
                            DefaultFilterChip(
                                label = "Whip",
                                selected = whipTricksSelected,
                                onClick = { whipTricksSelected = !whipTricksSelected }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DefaultFilterChip(
                                label = "Grabs",
                                selected = grabsSelected,
                                onClick = { grabsSelected = !grabsSelected }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DefaultFilterChip(
                                label = "Rails",
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
                        val selectedSpins = spinItems.filter { it.isSelected }
                        val selectedRaileyTricks = raileyTrickItems.filter { it.isSelected }
                        val selectedBackRollTricks = backRollTrickItems.filter { it.isSelected }
                        val selectedFrontFlipTricks = frontFlipTrickItems.filter { it.isSelected }
                        val selectedFrontRollTricks = frontRollTrickItems.filter { it.isSelected }
                        val selectedTantrumTricks = tantrumTrickItems.filter { it.isSelected }
                        val selectedWhipTricks = whipTrickItems.filter { it.isSelected }
                        val selectedGrabs = grabItems.filter { it.isSelected }
                        val selectedRails = railItems.filter { it.isSelected }

                        val trickListDto = TrickListDto(
                            spins = selectedSpins.map { it.toTrickItemDto() },
                            raileyTricks = selectedRaileyTricks.map { it.toTrickItemDto() },
                            backRollTricks = selectedBackRollTricks.map { it.toTrickItemDto() },
                            frontFlipTricks = selectedFrontFlipTricks.map { it.toTrickItemDto() },
                            frontRollTricks = selectedFrontRollTricks.map { it.toTrickItemDto() },
                            tantrumTricks = selectedTantrumTricks.map { it.toTrickItemDto() },
                            whipTricks = selectedWhipTricks.map { it.toTrickItemDto() },
                            grabs = selectedGrabs.map { it.toTrickItemDto() },
                            rails = selectedRails.map { it.toTrickItemDto() },
                        )

                        viewModel.addTrick(trickListDto)
                    },
                    modifier = Modifier.padding(
                        bottom = BottomSheetScaffoldDefaults.SheetPeekHeight + 16.dp
                    ),
                    expanded = expandedFab,
                    icon = { Icon(Icons.Filled.Done, "Done Icon") },
                    text = { Text(text = "Finish setup") },
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
                        Text(
                            text = "Spins",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(spinItems.size) { index ->
                        SelectableItem(
                            title = spinItems[index].name,
                            subtitle = spinItems[index].description,
                            selected = spinItems[index].isSelected,
                            onClick = {
                                spinItems = spinItems.mapIndexed { j, spinItems ->
                                    if (index == j) spinItems.copy(
                                        isSelected = !spinItems.isSelected
                                    ) else spinItems
                                }
                            }
                        )
                    }
                }
                if (raileyTricksSelected) {
                    item {
                        Text(
                            text = "Railey Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.raileyTricks.size) { index ->
                        SelectableItem(
                            title = trickList.raileyTricks[index].name,
                            subtitle = trickList.raileyTricks[index].description,
                            selected = raileyTrickItems[index].isSelected,
                            onClick = {
                                raileyTrickItems =
                                    raileyTrickItems.mapIndexed { j, raileyTrickItems ->
                                        if (index == j) raileyTrickItems.copy(
                                            isSelected = !raileyTrickItems.isSelected
                                        ) else raileyTrickItems
                                    }
                            }
                        )
                    }
                }
                if (backRollTricksSelected) {
                    item {
                        Text(
                            text = "Back Roll Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.backRollTricks.size) { index ->
                        SelectableItem(
                            title = trickList.backRollTricks[index].name,
                            subtitle = trickList.backRollTricks[index].description,
                            selected = backRollTrickItems[index].isSelected,
                            onClick = {
                                backRollTrickItems =
                                    backRollTrickItems.mapIndexed { j, backRollTrickItems ->
                                        if (index == j) backRollTrickItems.copy(
                                            isSelected = !backRollTrickItems.isSelected
                                        ) else backRollTrickItems
                                    }
                            }
                        )
                    }
                }
                if (frontFlipTricksSelected) {
                    item {
                        Text(
                            text = "Front Flip Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.frontFlipTricks.size) { index ->
                        SelectableItem(
                            title = trickList.frontFlipTricks[index].name,
                            subtitle = trickList.frontFlipTricks[index].description,
                            selected = frontFlipTrickItems[index].isSelected,
                            onClick = {
                                frontFlipTrickItems =
                                    frontFlipTrickItems.mapIndexed { j, frontFlipTrickItems ->
                                        if (index == j) frontFlipTrickItems.copy(
                                            isSelected = !frontFlipTrickItems.isSelected
                                        ) else frontFlipTrickItems
                                    }
                            }
                        )
                    }
                }
                if (frontRollTricksSelected) {
                    item {
                        Text(
                            text = "Front Roll Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.frontRollTricks.size) { index ->
                        SelectableItem(
                            title = trickList.frontRollTricks[index].name,
                            subtitle = trickList.frontRollTricks[index].description,
                            selected = frontRollTrickItems[index].isSelected,
                            onClick = {
                                frontRollTrickItems =
                                    frontRollTrickItems.mapIndexed { j, frontRollTrickItems ->
                                        if (index == j) frontRollTrickItems.copy(
                                            isSelected = !frontRollTrickItems.isSelected
                                        ) else frontRollTrickItems
                                    }
                            }
                        )
                    }
                }
                if (tantrumTricksSelected) {
                    item {
                        Text(
                            text = "Tantrum Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.tantrumTricks.size) { index ->
                        SelectableItem(
                            title = trickList.tantrumTricks[index].name,
                            subtitle = trickList.tantrumTricks[index].description,
                            selected = tantrumTrickItems[index].isSelected,
                            onClick = {
                                tantrumTrickItems =
                                    tantrumTrickItems.mapIndexed { j, tantrumTrickItems ->
                                        if (index == j) tantrumTrickItems.copy(
                                            isSelected = !tantrumTrickItems.isSelected
                                        ) else tantrumTrickItems
                                    }
                            }
                        )
                    }
                }
                if (whipTricksSelected) {
                    item {
                        Text(
                            text = "Whip Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.whipTricks.size) { index ->
                        SelectableItem(
                            title = trickList.whipTricks[index].name,
                            subtitle = trickList.whipTricks[index].description,
                            selected = whipTrickItems[index].isSelected,
                            onClick = {
                                whipTrickItems =
                                    whipTrickItems.mapIndexed { j, whipTrickItems ->
                                        if (index == j) whipTrickItems.copy(
                                            isSelected = !whipTrickItems.isSelected
                                        ) else whipTrickItems
                                    }
                            }
                        )
                    }
                }
                if (grabsSelected) {
                    item {
                        Text(
                            text = "Grabs",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.grabs.size) { index ->
                        SelectableItem(
                            title = trickList.grabs[index].name,
                            subtitle = trickList.grabs[index].description,
                            selected = grabItems[index].isSelected,
                            onClick = {
                                grabItems = grabItems.mapIndexed { j, grabItems ->
                                    if (index == j) grabItems.copy(
                                        isSelected = !grabItems.isSelected
                                    ) else grabItems
                                }
                            }
                        )
                    }
                }
                if (railsSelected) {
                    item {
                        Text(
                            text = "Rails",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickList.rails.size) { index ->
                        SelectableItem(
                            title = trickList.rails[index].name,
                            subtitle = trickList.rails[index].description,
                            selected = railItems[index].isSelected,
                            onClick = {
                                railItems = railItems.mapIndexed { j, railItems ->
                                    if (index == j) railItems.copy(
                                        isSelected = !railItems.isSelected
                                    ) else railItems
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    println("viewModel.state.isLoading ${viewModel.state.isLoading}")

    if (viewModel.state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
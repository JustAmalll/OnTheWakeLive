package com.onthewake.onthewakelive.feature_trick_list.presentation.trick_list

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.core.presentation.StandardLoadingView
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.EmptyContent
import com.onthewake.onthewakelive.feature_trick_list.presentation.components.CategoryTextView
import com.onthewake.onthewakelive.feature_trick_list.presentation.trick_list.components.TrickItem
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterial3Api
@Composable
fun TrickListScreen(
    navController: NavHostController,
    viewModel: TrickListViewModel = hiltViewModel()
) {

    val trickListState = viewModel.state.allTrickList

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackBarHostState = remember { SnackbarHostState() }
    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val surfaceColor = MaterialTheme.colorScheme.surface

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
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
                }
            )
        }
    ) { paddingValues ->

        if (viewModel.state.isLoading) StandardLoadingView()

        if (trickListState == null && !viewModel.state.isLoading) EmptyContent()

        if (trickListState != null)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                if (trickListState.spins.isNotEmpty()) {
                    item { CategoryTextView(text = "Spins") }
                    items(trickListState.spins.size) { index ->
                        TrickItem(
                            title = trickListState.spins[index].name,
                            subtitle = trickListState.spins[index].description
                        )
                    }
                }
                if (trickListState.raileyTricks.isNotEmpty()) {
                    item { CategoryTextView(text = "Railey Tricks") }
                    items(trickListState.raileyTricks.size) { index ->
                        TrickItem(
                            title = trickListState.raileyTricks[index].name,
                            subtitle = trickListState.raileyTricks[index].description
                        )
                    }
                }
                if (trickListState.backRollTricks.isNotEmpty()) {
                    item { CategoryTextView(text = "Back Roll Tricks") }
                    items(trickListState.backRollTricks.size) { index ->
                        TrickItem(
                            title = trickListState.backRollTricks[index].name,
                            subtitle = trickListState.backRollTricks[index].description
                        )
                    }
                }
                if (trickListState.frontFlipTricks.isNotEmpty()) {
                    item { CategoryTextView(text = "Front Flip Tricks") }
                    items(trickListState.frontFlipTricks.size) { index ->
                        TrickItem(
                            title = trickListState.frontFlipTricks[index].name,
                            subtitle = trickListState.frontFlipTricks[index].description
                        )
                    }
                }
                if (trickListState.frontRollTricks.isNotEmpty()) {
                    item { CategoryTextView(text = "Front Roll Tricks") }
                    items(trickListState.frontRollTricks.size) { index ->
                        TrickItem(
                            title = trickListState.frontRollTricks[index].name,
                            subtitle = trickListState.frontRollTricks[index].description
                        )
                    }
                }
                if (trickListState.tantrumTricks.isNotEmpty()) {
                    item { CategoryTextView(text = "Tantrum Tricks") }
                    items(trickListState.tantrumTricks.size) { index ->
                        TrickItem(
                            title = trickListState.tantrumTricks[index].name,
                            subtitle = trickListState.tantrumTricks[index].description
                        )
                    }
                }
                if (trickListState.whipTricks.isNotEmpty()) {
                    item { CategoryTextView(text = "Whip Tricks") }
                    items(trickListState.whipTricks.size) { index ->
                        TrickItem(
                            title = trickListState.whipTricks[index].name,
                            subtitle = trickListState.whipTricks[index].description,
                        )
                    }
                }
                if (trickListState.grabs.isNotEmpty()) {
                    item { CategoryTextView(text = "Grabs") }
                    items(trickListState.grabs.size) { index ->
                        TrickItem(
                            title = trickListState.grabs[index].name,
                            subtitle = trickListState.grabs[index].description
                        )
                    }
                }
                if (trickListState.rails.isNotEmpty()) {
                    item { CategoryTextView(text = "Rails") }
                    items(trickListState.rails.size) { index ->
                        TrickItem(
                            title = trickListState.rails[index].name,
                            subtitle = trickListState.rails[index].description
                        )
                    }
                }
            }
    }
}
package com.onthewake.onthewakelive.feature_trick_list.presentation.trick_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.presentation.components.StandardLoadingView
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.EmptyContent
import com.onthewake.onthewakelive.feature_trick_list.presentation.components.CategoryTextView
import com.onthewake.onthewakelive.feature_trick_list.presentation.trick_list.components.TrickItem
import kotlinx.coroutines.flow.collectLatest

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@Composable
fun TrickListScreen(
    navController: NavHostController,
    viewModel: TrickListViewModel = hiltViewModel()
) {

    val userTrickListState = viewModel.state.userTrickList

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
            snackBarHostState.showSnackbar(message = message)
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
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
                }
            )
        }
    ) { paddingValues ->

        AnimatedContent(targetState = viewModel.state.isLoading) { isLoading ->
            if (isLoading) {
                StandardLoadingView()
            } else if (userTrickListState == null) {
                EmptyContent()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = paddingValues.calculateTopPadding())
                ) {
                    if (userTrickListState.spins.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(id = R.string.spins))
                        }
                        items(userTrickListState.spins) { spin ->
                            TrickItem(title = spin.name, subtitle = spin.description)
                        }
                    }
                    if (userTrickListState.raileyTricks.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.railey_tricks))
                        }
                        items(userTrickListState.raileyTricks) { raileyTrick ->
                            TrickItem(title = raileyTrick.name, subtitle = raileyTrick.description)
                        }
                    }
                    if (userTrickListState.backRollTricks.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.back_roll_tricks))
                        }
                        items(userTrickListState.backRollTricks) { backRollTrick ->
                            TrickItem(title = backRollTrick.name, subtitle = backRollTrick.description)
                        }
                    }
                    if (userTrickListState.frontFlipTricks.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.front_flip_tricks))
                        }
                        items(userTrickListState.frontFlipTricks) { frontFlipTrick ->
                            TrickItem(
                                title = frontFlipTrick.name, subtitle = frontFlipTrick.description
                            )
                        }
                    }
                    if (userTrickListState.frontRollTricks.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.front_roll_tricks))
                        }
                        items(userTrickListState.frontRollTricks) { frontRollTrick ->
                            TrickItem(
                                title = frontRollTrick.name, subtitle = frontRollTrick.description
                            )
                        }
                    }
                    if (userTrickListState.tantrumTricks.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.tantrum_tricks))
                        }
                        items(userTrickListState.tantrumTricks) { tantrumTrick ->
                            TrickItem(title = tantrumTrick.name, subtitle = tantrumTrick.description)
                        }
                    }
                    if (userTrickListState.whipTricks.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.whip_tricks))
                        }
                        items(userTrickListState.whipTricks) { whipTrick ->
                            TrickItem(title = whipTrick.name, subtitle = whipTrick.description)
                        }
                    }
                    if (userTrickListState.grabs.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.grabs))
                        }
                        items(userTrickListState.grabs) { grab ->
                            TrickItem(title = grab.name, subtitle = grab.description)
                        }
                    }
                    if (userTrickListState.rails.isNotEmpty()) {
                        item {
                            CategoryTextView(text = stringResource(R.string.rails))
                        }
                        items(userTrickListState.rails) { railTrick ->
                            TrickItem(title = railTrick.name, subtitle = railTrick.description)
                        }
                    }
                }
            }
        }
    }
}
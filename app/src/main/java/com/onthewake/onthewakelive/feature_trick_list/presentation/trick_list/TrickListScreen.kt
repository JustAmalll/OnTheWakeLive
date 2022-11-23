package com.onthewake.onthewakelive.feature_trick_list.presentation.trick_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.components.EmptyContent
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

        if (trickListState != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                if (trickListState.spins.isNotEmpty()) {
                    item {
                        Text(
                            text = "Spins",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.spins.size) { index ->
                        TrickItem(
                            title = trickListState.spins[index].name,
                            subtitle = trickListState.spins[index].description
                        )
                    }
                }
                if (trickListState.raileyTricks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Railey Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.raileyTricks.size) { index ->
                        TrickItem(
                            title = trickListState.raileyTricks[index].name,
                            subtitle = trickListState.raileyTricks[index].description
                        )
                    }
                }
                if (trickListState.backRollTricks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Back Roll Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.backRollTricks.size) { index ->
                        TrickItem(
                            title = trickListState.backRollTricks[index].name,
                            subtitle = trickListState.backRollTricks[index].description
                        )
                    }
                }
                if (trickListState.frontFlipTricks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Front Flip Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.frontFlipTricks.size) { index ->
                        TrickItem(
                            title = trickListState.frontFlipTricks[index].name,
                            subtitle = trickListState.frontFlipTricks[index].description
                        )
                    }
                }
                if (trickListState.frontRollTricks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Front Roll Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.frontRollTricks.size) { index ->
                        TrickItem(
                            title = trickListState.frontRollTricks[index].name,
                            subtitle = trickListState.frontRollTricks[index].description
                        )
                    }
                }
                if (trickListState.tantrumTricks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Tantrum Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.tantrumTricks.size) { index ->
                        TrickItem(
                            title = trickListState.tantrumTricks[index].name,
                            subtitle = trickListState.tantrumTricks[index].description
                        )
                    }
                }
                if (trickListState.whipTricks.isNotEmpty()) {
                    item {
                        Text(
                            text = "Whip Tricks",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.whipTricks.size) { index ->
                        TrickItem(
                            title = trickListState.whipTricks[index].name,
                            subtitle = trickListState.whipTricks[index].description,
                        )
                    }
                }
                if (trickListState.grabs.isNotEmpty()) {
                    item {
                        Text(
                            text = "Grabs",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.grabs.size) { index ->
                        TrickItem(
                            title = trickListState.grabs[index].name,
                            subtitle = trickListState.grabs[index].description
                        )
                    }
                }
                if (trickListState.rails.isNotEmpty()) {
                    item {
                        Text(
                            text = "Rails",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                    items(trickListState.rails.size) { index ->
                        TrickItem(
                            title = trickListState.rails[index].name,
                            subtitle = trickListState.rails[index].description
                        )
                    }
                }
            }
        } else {
            EmptyContent()
        }
    }
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

@Composable
fun TrickItem(
    title: String,
    subtitle: String
) {

    val onSurface = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .border(
                    width = 1.dp,
                    color = onSurface,
                    shape = RoundedCornerShape(size = 10.dp)
                )
                .clip(RoundedCornerShape(size = 10.dp))
        ) {
            Text(
                modifier = Modifier.padding(start = 12.dp, top = 10.dp),
                text = title.replaceFirstChar { it.uppercaseChar() },
                style = TextStyle(
                    color = onSurface,
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                modifier = Modifier.padding(start = 12.dp, bottom = 10.dp, end = 50.dp),
                text = subtitle.replaceFirstChar { it.uppercaseChar() },
                style = TextStyle(color = onSurface),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
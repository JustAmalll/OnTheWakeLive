package queue.presentation.list.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.utils.clickableWithoutIndication
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import queue.domain.model.Line

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun TabRow(pagerState: PagerState) {
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = Color.White,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(size = 6.dp)),
                height = 2.dp,
                color = Color(0xFF2E7AD3)
            )
        },
        containerColor = Color(0xFF1D1B20),
        divider = {}
    ) {
        Line.entries.forEachIndexed { index, line ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableWithoutIndication {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    }
                    .padding(vertical = 12.dp),
                text = stringResource(resource = line.displayName),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}
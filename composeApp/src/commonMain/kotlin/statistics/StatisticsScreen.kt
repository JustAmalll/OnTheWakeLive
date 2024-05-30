package statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.ic_cat
import onthewakelive.composeapp.generated.resources.ic_statistics
import onthewakelive.composeapp.generated.resources.statistics
import onthewakelive.composeapp.generated.resources.statistics_screen_in_development
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object StatisticsAssembly : Tab {

    @Composable
    override fun Content() {
        StatisticsScreen()
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.statistics)
            val icon = painterResource(resource = Res.drawable.ic_statistics)

            return remember { TabOptions(index = 0u, title = title, icon = icon) }
        }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun StatisticsScreen() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.statistics),
                        fontSize = 20.sp
                    )
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 80.dp)
            ) {
                Text(
                    text = stringResource(Res.string.statistics_screen_in_development),
                    fontWeight = FontWeight.Medium,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_cat),
                contentDescription = null
            )
        }
    }
}
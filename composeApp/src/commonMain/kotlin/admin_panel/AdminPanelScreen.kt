package admin_panel

import activate_subscription.ActivateSubscriptionAssembly
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.activate_subscription
import onthewakelive.composeapp.generated.resources.admin_panel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

object AdminPanelAssembly : Tab {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current?.parent

        AdminPanelScreen(
            onActivateSubscriptionClicked = {
                navigator?.push(ActivateSubscriptionAssembly())
            }
        )
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(resource = Res.string.admin_panel)
            val icon = rememberVectorPainter(image = Icons.Default.AdminPanelSettings)

            return remember { TabOptions(index = 1u, title = title, icon = icon) }
        }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun AdminPanelScreen(onActivateSubscriptionClicked: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.admin_panel)) }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 30.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .clickable(onClick = onActivateSubscriptionClicked)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(Res.string.activate_subscription))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }
        }
    }
}
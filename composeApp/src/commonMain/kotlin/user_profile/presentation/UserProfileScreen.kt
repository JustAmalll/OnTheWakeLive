package user_profile.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.profile
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

object UserProfileAssembly : Tab {

    @Composable
    override fun Content() {
        UserProfileScreen()
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(resource = Res.string.profile)
            val icon = rememberVectorPainter(image = Icons.Default.Person)

            return remember { TabOptions(index = 1u, title = title, icon = icon) }
        }
}

@Composable
private fun UserProfileScreen() {

}
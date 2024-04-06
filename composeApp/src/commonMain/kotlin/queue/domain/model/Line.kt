package queue.domain.model

import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.left
import onthewakelive.composeapp.generated.resources.right
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@OptIn(ExperimentalResourceApi::class)
enum class Line(val displayName: StringResource) {
    LEFT(displayName = Res.string.left),
    RIGHT(displayName = Res.string.right)
}
package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.DensityLarge
import ly.david.musicsearch.ui.common.icons.DensitySmall
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.showLessInfo
import musicsearch.ui.common.generated.resources.showMoreInfo
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverflowMenuScope.MoreInfoToggleMenuItem(
    showMoreInfo: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    ToggleMenuItem(
        toggleOnText = stringResource(Res.string.showMoreInfo),
        toggleOffText = stringResource(Res.string.showLessInfo),
        toggled = showMoreInfo,
        onToggle = onToggle,
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = if (showMoreInfo) CustomIcons.DensityLarge else CustomIcons.DensitySmall,
                contentDescription = null,
            )
        },
    )
}

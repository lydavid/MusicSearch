package ly.david.musicsearch.ui.common.topappbar

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.DensityLarge
import ly.david.musicsearch.ui.common.icons.DensitySmall
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
fun OverflowMenuScope.MoreInfoToggleMenuItem(
    showMoreInfo: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    val strings = LocalStrings.current

    ToggleMenuItem(
        toggleOnText = strings.showMoreInfo,
        toggleOffText = strings.showLessInfo,
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

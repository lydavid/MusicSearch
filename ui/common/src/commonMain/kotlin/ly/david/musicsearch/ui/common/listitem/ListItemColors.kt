package ly.david.musicsearch.ui.common.listitem

import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ly.david.musicsearch.ui.common.theme.DISABLED_ALPHA
import ly.david.musicsearch.ui.common.theme.SELECTED_ALPHA

@Composable
fun listItemColors(
    isSelected: Boolean,
    enabled: Boolean = true,
): ListItemColors {
    return ListItemDefaults.colors(
        containerColor = if (isSelected) {
            MaterialTheme.colorScheme.primary.copy(alpha = SELECTED_ALPHA)
        } else {
            Color.Transparent
        },
        headlineColor = getEnabledColor(enabled),
        leadingIconColor = getEnabledColor(enabled),
        trailingIconColor = getEnabledColor(enabled),
    )
}

@Composable
private fun getEnabledColor(enabled: Boolean) =
    if (enabled) {
        Color.Unspecified
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = DISABLED_ALPHA)
    }

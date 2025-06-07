package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.KeyboardArrowDown
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun CollectionsFilterChipsBar(
    sortOption: CollectionSortOption,
    modifier: Modifier = Modifier,
    showLocal: Boolean = true,
    onShowLocalToggle: (Boolean) -> Unit = {},
    showRemote: Boolean = true,
    onShowRemoteToggle: (Boolean) -> Unit = {},
    onSortClick: () -> Unit = {},
) {
    val strings = LocalStrings.current

    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FilterChip(
            selected = showLocal,
            onClick = { onShowLocalToggle(!showLocal) },
            label = { Text(text = "Local") },
            leadingIcon = {
                if (showLocal) {
                    Icon(
                        imageVector = CustomIcons.Check,
                        contentDescription = null,
                    )
                }
            },
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        FilterChip(
            selected = showRemote,
            onClick = { onShowRemoteToggle(!showRemote) },
            label = { Text(text = "Remote") },
            leadingIcon = {
                if (showRemote) {
                    Icon(
                        imageVector = CustomIcons.Check,
                        contentDescription = null,
                    )
                }
            },
        )

        VerticalDivider(
            modifier = Modifier
                .height(FilterChipDefaults.Height)
                .padding(8.dp),
        )

        FilterChip(
            selected = false,
            onClick = onSortClick,
            label = { Text(text = "Sort: ${sortOption.getLabel(strings)}") },
            trailingIcon = {
                Icon(
                    imageVector = CustomIcons.KeyboardArrowDown,
                    contentDescription = null,
                )
            },
        )
    }
}

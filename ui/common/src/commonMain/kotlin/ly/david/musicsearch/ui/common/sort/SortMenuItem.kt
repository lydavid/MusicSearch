package ly.david.musicsearch.ui.common.sort

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.list.SortableOption
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Sort
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> OverflowMenuScope.SortMenuItem(
    sortOptions: List<T>,
    selectedSortOption: T,
    modifier: Modifier = Modifier,
    onSortOptionClick: (T) -> Unit = {},
) where T : Enum<T>, T : SortableOption {
    val strings = LocalStrings.current

    var showBottomSheet by remember { mutableStateOf(false) }
    if (showBottomSheet) {
        SortOptionsBottomSheet(
            sortOptions = sortOptions,
            selectedSortOption = selectedSortOption,
            onSortOptionClick = {
                onSortOptionClick(it)
                closeMenu()
            },
            onDismiss = {
                showBottomSheet = false
            },
        )
    }

    DropdownMenuItem(
        text = {
            Text(strings.sort)
        },
        leadingIcon = {
            Icon(
                imageVector = CustomIcons.Sort,
                contentDescription = null,
            )
        },
        onClick = {
            showBottomSheet = true
        },
        modifier = modifier,
    )
}

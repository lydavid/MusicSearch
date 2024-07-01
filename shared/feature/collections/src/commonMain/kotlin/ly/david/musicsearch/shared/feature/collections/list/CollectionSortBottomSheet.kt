package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.collection.CollectionSortOption
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.component.ClickableItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionSortBottomSheet(
    sortOption: CollectionSortOption,
    onSortOptionClick: (CollectionSortOption) -> Unit = {},
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        CollectionSortBottomSheetContent(
            sortOption = sortOption,
            onSortOptionClick = onSortOptionClick,
        )
    }
}

@Composable
internal fun CollectionSortBottomSheetContent(
    sortOption: CollectionSortOption = CollectionSortOption.ALPHABETICALLY,
    onSortOptionClick: (CollectionSortOption) -> Unit = {},
) {
    val strings = LocalStrings.current

    Column {
        CollectionSortOption.entries.forEach {
            ClickableItem(
                title = it.getLabel(strings),
                endIcon = if (sortOption == it) Icons.Default.Check else null,
                onClick = {
                    onSortOptionClick(it)
                },
            )
        }
    }
}

internal fun CollectionSortOption.getLabel(strings: AppStrings): String {
    return when (this) {
        CollectionSortOption.ALPHABETICALLY -> strings.alphabetically
        CollectionSortOption.ALPHABETICALLY_REVERSE -> strings.alphabeticallyReverse
        CollectionSortOption.MOST_ENTITY_COUNT -> strings.mostEntities
        CollectionSortOption.LEAST_ENTITY_COUNT -> strings.leastEntities
    }
}

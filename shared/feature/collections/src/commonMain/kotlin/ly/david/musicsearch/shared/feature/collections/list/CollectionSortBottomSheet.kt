package ly.david.musicsearch.shared.feature.collections.list

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.collection.CollectionSortOption
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.alphabetically
import musicsearch.ui.common.generated.resources.alphabeticallyReverse
import musicsearch.ui.common.generated.resources.leastEntities
import musicsearch.ui.common.generated.resources.mostEntities
import org.jetbrains.compose.resources.stringResource

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
    Column {
        CollectionSortOption.entries.forEach {
            ClickableItem(
                title = it.getLabel(),
                endIcon = if (sortOption == it) CustomIcons.Check else null,
                onClick = {
                    onSortOptionClick(it)
                },
            )
        }
    }
}

@Composable
internal fun CollectionSortOption.getLabel(): String {
    return stringResource(
        when (this) {
            CollectionSortOption.ALPHABETICALLY -> Res.string.alphabetically
            CollectionSortOption.ALPHABETICALLY_REVERSE -> Res.string.alphabeticallyReverse
            CollectionSortOption.MOST_ENTITY_COUNT -> Res.string.mostEntities
            CollectionSortOption.LEAST_ENTITY_COUNT -> Res.string.leastEntities
        },
    )
}

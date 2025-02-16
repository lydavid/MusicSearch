package ly.david.musicsearch.ui.common.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleChoiceBottomSheet(
    options: List<String>,
    selectedOptionIndex: Int,
    onSortOptionIndexClick: (Int) -> Unit = {},
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        MultipleChoiceBottomSheetContent(
            options = options,
            selectedOptionIndex = selectedOptionIndex,
            onSortOptionIndexClick = onSortOptionIndexClick,
        )
    }
}

@Composable
internal fun MultipleChoiceBottomSheetContent(
    options: List<String>,
    selectedOptionIndex: Int,
    onSortOptionIndexClick: (Int) -> Unit = {},
) {
    Column {
        options.forEachIndexed { index, option ->
            ClickableItem(
                title = option,
                endIcon = if (selectedOptionIndex == index) Icons.Default.Check else null,
                onClick = {
                    onSortOptionIndexClick(index)
                },
            )
        }
    }
}

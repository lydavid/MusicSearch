package ly.david.musicsearch.ui.common.release

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseStatusesBottomSheet(
    selectedStatuses: Set<ReleaseStatus>,
    onClick: (ReleaseStatus) -> Unit,
    bottomSheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit = {},
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
    ) {
        ReleaseStatusesBottomSheetContent(
            selectedStatuses = selectedStatuses,
            onClick = onClick,
        )
    }
}

// TODO: select all, deselect all: tristate?
@Composable
internal fun ReleaseStatusesBottomSheetContent(
    selectedStatuses: Set<ReleaseStatus>,
    onClick: (ReleaseStatus) -> Unit,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        ReleaseStatus.entries.forEach { releaseStatus ->
            ClickableItem(
                title = releaseStatus.getDisplayString(),
                // TODO: consider showing count of each status
                startIcon = if (selectedStatuses.contains(releaseStatus)) CustomIcons.Check else null,
                onClick = {
                    onClick(releaseStatus)
                },
            )
        }
    }
}

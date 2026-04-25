package ly.david.musicsearch.ui.common.release

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.ui.common.button.TriStateCheckboxWithText
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.Check
import ly.david.musicsearch.ui.common.icons.CustomIcons
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.deselectAll
import musicsearch.ui.common.generated.resources.selectAll
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ReleaseStatusesBottomSheet(
    selectedStatuses: Set<ReleaseStatus>,
    onClick: (ReleaseStatus?) -> Unit,
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

@Composable
internal fun ReleaseStatusesBottomSheetContent(
    selectedStatuses: Set<ReleaseStatus>,
    onClick: (ReleaseStatus?) -> Unit,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        val selectedAll = selectedStatuses.size == ReleaseStatus.entries.size
        TriStateCheckboxWithText(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(
                if (selectedAll) {
                    Res.string.deselectAll
                } else {
                    Res.string.selectAll
                },
            ),
            toggleableState = when {
                selectedAll -> ToggleableState.On
                selectedStatuses.isEmpty() -> ToggleableState.Off
                else -> ToggleableState.Indeterminate
            },
            onClick = {
                onClick(null)
            },
        )

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

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

package ly.david.musicsearch.ui.common.release

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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

@Composable
internal fun ReleaseStatusesBottomSheetContent(
    state: ReleaseStatusesUiState,
) {
    val selectedStatuses = state.selectedStatuses
    val releaseStatusCounts = state.releaseStatusCounts
    val eventSink = state.eventSink

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
                eventSink(ReleaseStatusesUiEvent.UpdateShowReleaseStatus(null))
            },
        )

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))

        ReleaseStatus.entries.forEach { releaseStatus ->
            val count = releaseStatusCounts[releaseStatus] ?: 0
            ClickableItem(
                title = releaseStatus.getDisplayString() + " ($count)",
                startIcon = if (selectedStatuses.contains(releaseStatus)) CustomIcons.Check else null,
                onClick = {
                    eventSink(ReleaseStatusesUiEvent.UpdateShowReleaseStatus(releaseStatus))
                },
            )
        }
    }
}

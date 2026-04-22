package ly.david.musicsearch.ui.common.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewReleaseStatusesBottomSheetContentAll() {
    PreviewTheme {
        Surface {
            ReleaseStatusesBottomSheetContent(
                selectedStatuses = ReleaseStatus.entries.toSet(),
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseStatusesBottomSheetContentWithOneDeselected() {
    PreviewTheme {
        Surface {
            ReleaseStatusesBottomSheetContent(
                selectedStatuses = ReleaseStatus.entries.minus(ReleaseStatus.PROMOTION).toSet(),
                onClick = {},
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseStatusesBottomSheetContentWithOneSelected() {
    PreviewTheme {
        Surface {
            ReleaseStatusesBottomSheetContent(
                selectedStatuses = setOf(ReleaseStatus.BOOTLEG),
                onClick = {},
            )
        }
    }
}

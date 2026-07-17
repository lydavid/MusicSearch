package ly.david.musicsearch.ui.common.release

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toPersistentHashMap
import kotlinx.collections.immutable.toPersistentSet
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.ui.common.preview.PreviewTheme

private val releaseStatusCounts = mapOf(
    ReleaseStatus.OFFICIAL to 235,
    ReleaseStatus.PROMOTION to 1,
    ReleaseStatus.BOOTLEG to 2,
    ReleaseStatus.PSEUDO_RELEASE to 14,
    ReleaseStatus.UNKNOWN to 4,
).toPersistentHashMap()

@PreviewLightDark
@Composable
internal fun PreviewReleaseStatusesBottomSheetContentAll() {
    PreviewTheme {
        Surface {
            ReleaseStatusesBottomSheetContent(
                state = ReleaseStatusesUiState(
                    selectedStatuses = ReleaseStatus.entries.toPersistentSet(),
                    releaseStatusCounts = releaseStatusCounts,
                ),
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
                state = ReleaseStatusesUiState(
                    selectedStatuses = ReleaseStatus.entries.minus(ReleaseStatus.PROMOTION).toPersistentSet(),
                    releaseStatusCounts = releaseStatusCounts,
                ),
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
                state = ReleaseStatusesUiState(
                    selectedStatuses = persistentSetOf(ReleaseStatus.BOOTLEG),
                    releaseStatusCounts = releaseStatusCounts,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseStatusesBottomSheetContentWithNoneSelected() {
    PreviewTheme {
        Surface {
            ReleaseStatusesBottomSheetContent(
                state = ReleaseStatusesUiState(
                    selectedStatuses = persistentSetOf(),
                    releaseStatusCounts = releaseStatusCounts,
                ),
            )
        }
    }
}

package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupPrimaryType
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays

private val testReleaseGroup = ReleaseGroupListItemModel(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    name = "欠けた心象、世のよすが",
    disambiguation = "That one",
    firstReleaseDate = "2021-09-08",
    primaryType = ReleaseGroupPrimaryType.EP,
    formattedArtistCredits = "Some artist feat. some other artist",
)

private val lastListenedNow = Instant.parse("2024-06-01T00:00:00Z")
private val lastListenedThreeDaysAgo = Instant.parse("2024-05-29T00:00:00Z").toEpochMilliseconds()
private val lastListenedFiveHoursAgo = Instant.parse("2024-05-31T19:00:00Z").toEpochMilliseconds()

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupListItem() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupListItem(
            releaseGroup = testReleaseGroup,
            filterText = "",
            showType = false,
            now = Instant.parse("1970-01-01T00:00:00Z"),
            showLastListenedPeriod = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupListItemWithType() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupListItem(
            releaseGroup = testReleaseGroup,
            filterText = "",
            showType = true,
            now = Instant.parse("1970-01-01T00:00:00Z"),
            showLastListenedPeriod = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupListItemVisited() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupListItem(
            releaseGroup = ReleaseGroupListItemModel(
                id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                name = "欠けた心象、世のよすが",
                disambiguation = "That one",
                firstReleaseDate = "2021-09-08",
                primaryType = ReleaseGroupPrimaryType.EP,
                formattedArtistCredits = "Some artist feat. some other artist",
                visited = true,
            ),
            filterText = "artist",
            showType = true,
            now = Instant.parse("1970-01-01T00:00:00Z"),
            showLastListenedPeriod = false,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupListItemWithLastListenedPeriod() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupListItem(
            releaseGroup = testReleaseGroup.copy(
                listenCount = 42L,
                lastListenedAtMs = lastListenedThreeDaysAgo,
            ),
            filterText = "",
            showType = false,
            now = lastListenedNow,
            showLastListenedPeriod = true,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupListItemVisitedWithLastListenedPeriod() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupListItem(
            releaseGroup = testReleaseGroup.copy(
                visited = true,
                listenCount = 42L,
                lastListenedAtMs = lastListenedFiveHoursAgo,
            ),
            filterText = "artist",
            showType = false,
            now = lastListenedNow,
            showLastListenedPeriod = true,
        )
    }
}

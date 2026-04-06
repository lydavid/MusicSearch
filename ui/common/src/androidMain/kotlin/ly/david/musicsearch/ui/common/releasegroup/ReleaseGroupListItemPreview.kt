package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
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

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupListItem() {
    PreviewWithTransitionAndOverlays {
        ReleaseGroupListItem(
            releaseGroup = testReleaseGroup,
            filterText = "",
            showType = false,
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
        )
    }
}

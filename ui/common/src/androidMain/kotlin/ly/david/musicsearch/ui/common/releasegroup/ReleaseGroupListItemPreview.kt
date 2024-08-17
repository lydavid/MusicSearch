package ly.david.musicsearch.ui.common.releasegroup

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

private val testReleaseGroup = ReleaseGroupListItemModel(
    id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
    name = "欠けた心象、世のよすが",
    primaryType = "EP",
    firstReleaseDate = "2021-09-08",
    formattedArtistCredits = "Some artist feat. some other artist",
)

@DefaultPreviews
@Composable
internal fun PreviewReleaseGroupListItem() {
    PreviewTheme {
        Surface {
            ReleaseGroupListItem(
                releaseGroup = testReleaseGroup,
                showType = false,
            )
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewReleaseGroupListItemWithType() {
    PreviewTheme {
        Surface {
            ReleaseGroupListItem(
                releaseGroup = testReleaseGroup,
                showType = true,
            )
        }
    }
}

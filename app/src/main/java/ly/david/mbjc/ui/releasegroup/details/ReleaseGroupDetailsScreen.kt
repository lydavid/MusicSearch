package ly.david.mbjc.ui.releasegroup.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.domain.releasegroup.ReleaseGroupScaffoldModel
import ly.david.data.core.releasegroup.getDisplayTypes
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.ui.common.R
import ly.david.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.ui.common.url.UrlsSection
import ly.david.ui.image.LargeImage

@Composable
internal fun ReleaseGroupDetailsScreen(
    releaseGroup: ReleaseGroupScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    coverArtUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            LargeImage(
                url = coverArtUrl,
                mbid = releaseGroup.id
            )
        }

        item {
            releaseGroup.run {
                InformationListSeparatorHeader(R.string.release_group)
                TextWithHeadingRes(
                    headingRes = R.string.type,
                    text = getDisplayTypes(),
                    filterText = filterText,
                )

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick
                )
            }
        }
    }
}

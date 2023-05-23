package ly.david.mbjc.ui.releasegroup.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.domain.ReleaseGroupScaffoldModel
import ly.david.data.getDisplayTypes
import ly.david.ui.common.text.TextWithHeadingRes
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader
import ly.david.ui.common.R
import ly.david.ui.common.coverart.BigCoverArt

@Composable
internal fun ReleaseGroupDetailsScreen(
    releaseGroup: ReleaseGroupScaffoldModel,
    modifier: Modifier = Modifier,
    coverArtUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        item {
            BigCoverArt(coverArtUrl = coverArtUrl)
        }

        item {
            releaseGroup.run {
                InformationListSeparatorHeader(R.string.release_group)
                TextWithHeadingRes(headingRes = R.string.type, text = getDisplayTypes())
            }
        }
    }
}

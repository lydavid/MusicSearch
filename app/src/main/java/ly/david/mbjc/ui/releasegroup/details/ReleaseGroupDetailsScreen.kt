package ly.david.mbjc.ui.releasegroup.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import ly.david.data.domain.ReleaseGroupScaffoldModel
import ly.david.data.getDisplayTypes
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.TextWithHeadingRes
import ly.david.mbjc.ui.common.coverart.BigCoverArt
import ly.david.mbjc.ui.common.listitem.InformationListSeparatorHeader

@Composable
internal fun ReleaseGroupDetailsScreen(
    releaseGroup: ReleaseGroupScaffoldModel,
    coverArtUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
) {

    LazyColumn(state = lazyListState) {

        item {
            BigCoverArt(
                coverArtUrl = coverArtUrl,
                resourceId = releaseGroup.id
            )
        }

        item {
            releaseGroup.run {
                InformationListSeparatorHeader(R.string.release_group)
                TextWithHeadingRes(headingRes = R.string.type, text = getDisplayTypes())
            }
        }
    }
}

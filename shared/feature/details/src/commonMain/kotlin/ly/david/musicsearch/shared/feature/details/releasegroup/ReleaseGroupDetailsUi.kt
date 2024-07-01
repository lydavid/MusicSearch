package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.releasegroup.getDisplayTypes
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.ui.common.text.TextWithHeading
import ly.david.ui.common.url.UrlsSection
import ly.david.musicsearch.ui.image.LargeImage

@Composable
internal fun ReleaseGroupDetailsUi(
    releaseGroup: ReleaseGroupScaffoldModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    imageUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            LargeImage(
                url = imageUrl,
                mbid = releaseGroup.id,
            )
        }

        item {
            releaseGroup.run {
                ListSeparatorHeader(text = strings.informationHeader(strings.releaseGroup))
                TextWithHeading(
                    heading = strings.type,
                    text = getDisplayTypes(),
                    filterText = filterText,
                )

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                    onItemClick = onItemClick,
                )
            }
        }
    }
}

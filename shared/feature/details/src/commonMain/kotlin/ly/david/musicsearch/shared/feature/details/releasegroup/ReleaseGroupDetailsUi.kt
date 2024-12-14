package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.UrlsSection
import ly.david.musicsearch.ui.image.LargeImage
import ly.david.musicsearch.ui.image.getPlaceholderKey

@Composable
internal fun ReleaseGroupDetailsUi(
    releaseGroup: ReleaseGroupDetailsModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    imageUrl: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            if (filterText.isBlank()) {
                LargeImage(
                    url = imageUrl,
                    placeholderKey = getPlaceholderKey(releaseGroup.id),
                )
            }
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
                )
            }
        }
    }
}

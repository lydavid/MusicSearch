package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNull
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.ui.common.image.LargeImage
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.urlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun ReleaseGroupDetailsUi(
    releaseGroup: ReleaseGroupDetailsModel,
    detailsUiState: ReleaseGroupDetailsUiState,
    modifier: Modifier = Modifier,
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onCollapseExpandExternalLinks: () -> Unit = {},
) {
    val strings = LocalStrings.current

    LazyColumn(
        modifier = modifier,
        state = detailsUiState.lazyListState,
    ) {
        releaseGroup.run {
            item {
                if (filterText.isBlank()) {
                    LargeImage(
                        url = imageMetadata.largeUrl,
                        placeholderKey = imageMetadata.databaseId.toString(),
                        onClick = onImageClick,
                    )
                }

                ListSeparatorHeader(text = strings.informationHeader(strings.releaseGroup))
                detailsUiState.numberOfImages?.ifNotNull {
                    TextWithHeading(
                        heading = strings.numberOfImages,
                        text = "$it",
                        filterText = filterText,
                    )
                }
                TextWithHeading(
                    heading = strings.type,
                    text = getDisplayTypes(),
                    filterText = filterText,
                )
                firstReleaseDate.ifNotNullOrEmpty {
                    TextWithHeading(
                        heading = strings.firstReleaseDate,
                        text = it,
                        filterText = filterText,
                    )
                }
            }

            item {
                WikipediaSection(
                    extract = wikipediaExtract,
                    filterText = filterText,
                )
            }

            urlsSection(
                urls = urls,
                collapsed = detailsUiState.isExternalLinksCollapsed,
                onCollapseExpand = onCollapseExpandExternalLinks,
            )
        }
    }
}

package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.UrlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.image.LargeImage

@Composable
internal fun ArtistDetailsUi(
    artist: ArtistDetailsModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            if (filterText.isBlank()) {
                LargeImage(
                    url = artist.imageUrls.largeUrl,
                    placeholderKey = artist.imageUrls.databaseId.toString(),
                )
            }

            artist.run {
                ArtistInformationSection(
                    filterText = filterText,
                )

                AreaSection(
                    areaListItemModel = areaListItemModel,
                    filterText = filterText,
                    onItemClick = onItemClick,
                )

                // TODO: begin area, end area

                UrlsSection(
                    urls = urls,
                    filterText = filterText,
                )
            }
        }
    }
}

@Composable
private fun ArtistDetailsModel.ArtistInformationSection(
    filterText: String = "",
) {
    val strings = LocalStrings.current

    ListSeparatorHeader(text = strings.informationHeader(strings.artist))
    sortName.ifNotNullOrEmpty {
        TextWithHeading(
            heading = strings.sortName,
            text = it,
            filterText = filterText,
        )
    }
    type?.ifNotNullOrEmpty {
        TextWithHeading(
            heading = strings.type,
            text = it,
            filterText = filterText,
        )
    }
    gender?.ifNotNullOrEmpty {
        TextWithHeading(
            heading = strings.gender,
            text = it,
            filterText = filterText,
        )
    }
    LifeSpanText(
        lifeSpan = lifeSpan,
        heading = strings.date,
        beginHeading = when (type) {
            "Person" -> strings.born
            "Character" -> strings.created
            else -> strings.founded
        },
        endHeading = when (type) {
            "Person" -> strings.died
            // Characters do not "die": https://musicbrainz.org/doc/Artist
            else -> strings.dissolved
        },
        filterText = filterText,
    )

    ipis?.ifNotNullOrEmpty {
        TextWithHeading(
            heading = strings.ipi,
            text = it.joinToString(", "),
            filterText = filterText,
        )
    }

    isnis?.ifNotNullOrEmpty {
        TextWithHeading(
            heading = strings.isni,
            text = it.joinToString(", "),
            filterText = filterText,
        )
    }

    WikipediaSection(
        extract = wikipediaExtract,
        filterText = filterText,
    )

    Spacer(modifier = Modifier.padding(bottom = 16.dp))
}

@Composable
private fun AreaSection(
    areaListItemModel: AreaListItemModel?,
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    areaListItemModel?.run {
        ListSeparatorHeader(text = strings.area)

        if (name.contains(filterText, ignoreCase = true)) {
            AreaListItem(
                area = this,
                showType = false,
                onAreaClick = {
                    onItemClick(
                        MusicBrainzEntity.AREA,
                        id,
                        name,
                    )
                },
            )
        }
    }
}

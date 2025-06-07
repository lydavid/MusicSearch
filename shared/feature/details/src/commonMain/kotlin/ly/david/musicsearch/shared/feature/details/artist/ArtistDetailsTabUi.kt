package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.theme.LocalStrings

@Composable
internal fun ArtistDetailsTabUi(
    artist: ArtistDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onCollapseExpandExternalLinks: () -> Unit = {},
) {
    DetailsTabUi(
        detailsModel = artist,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onCollapseExpandExternalLinks = onCollapseExpandExternalLinks,
        entityInfoSection = {
            ArtistInformationSection(
                filterText = filterText,
            )
        },
        bringYourOwnLabelsSection = {
            artist.run {
                item {
                    AreaSection(
                        areaListItemModel = areaListItemModel,
                        filterText = filterText,
                        onItemClick = onItemClick,
                    )
                }

                // TODO: begin area, end area
            }
        },
    )
}

@Composable
private fun ArtistDetailsModel.ArtistInformationSection(
    filterText: String = "",
) {
    val strings = LocalStrings.current

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
                showIcon = false,
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

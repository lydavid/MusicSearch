package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.foundation.layout.Column
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
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.listitem.CollapsibleListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.relation.RelationListItem
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.url.UrlsSection
import ly.david.musicsearch.ui.common.wikimedia.WikipediaSection
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.image.LargeImage
import ly.david.musicsearch.ui.image.getPlaceholderKey

@Composable
internal fun ArtistDetailsUi(
    artist: ArtistDetailsModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    collapsedSections: Set<ArtistDetailsSection> = setOf(),
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onCollapseSection: (ArtistDetailsSection) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        state = lazyListState,
    ) {
        item {
            if (filterText.isBlank()) {
                LargeImage(
                    url = artist.imageUrl.orEmpty(),
                    placeholderKey = getPlaceholderKey(artist.id),
                )
            }

            artist.run {
                ArtistInformationSection(
                    wikipediaExtract = wikipediaExtract,
                    filterText = filterText,
                )

                AreaSection(
                    areaListItemModel = areaListItemModel,
                    filterText = filterText,
                    onItemClick = onItemClick,
                )

                // TODO: begin area, end area

                BandsAndMembersSection(
                    artists = artists,
                    filterText = filterText,
                    collapsedSections = collapsedSections,
                    onItemClick = onItemClick,
                    onCollapseSection = onCollapseSection,
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

@Composable
private fun ArtistDetailsModel.ArtistInformationSection(
    wikipediaExtract: WikipediaExtract,
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

        if (name.contains(
                filterText,
                ignoreCase = true,
            )
        ) {
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

@Composable
private fun BandsAndMembersSection(
    artists: List<RelationListItemModel>,
    modifier: Modifier = Modifier,
    filterText: String = "",
    collapsedSections: Set<ArtistDetailsSection> = setOf(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onCollapseSection: (ArtistDetailsSection) -> Unit = {},
) {
    Column(modifier = modifier) {
        BandsOrMembersSection(
            section = ArtistDetailsSection.PartOfGroups,
            artists = artists.filter { it.isForwardDirection == true && it.additionalInfo?.contains("to") == false },
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )
        BandsOrMembersSection(
            section = ArtistDetailsSection.PreviouslyPartOfGroups,
            artists = artists.filter { it.isForwardDirection == true && it.additionalInfo?.contains("to") == true },
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )

        BandsOrMembersSection(
            section = ArtistDetailsSection.MembersOfGroup,
            artists = artists.filter { it.isForwardDirection == false && it.additionalInfo?.contains("to") == false },
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )
        BandsOrMembersSection(
            section = ArtistDetailsSection.PreviousMembersOfGroup,
            artists = artists.filter { it.isForwardDirection == false && it.additionalInfo?.contains("to") == true },
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun BandsOrMembersSection(
    section: ArtistDetailsSection,
    artists: List<RelationListItemModel>,
    collapsedSections: Set<ArtistDetailsSection>,
    onCollapseSection: (ArtistDetailsSection) -> Unit,
    filterText: String,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit,
) {
    if (artists.isEmpty()) return

    val collapsed = collapsedSections.contains(section)
    CollapsibleListSeparatorHeader(
        "${section.title} (${artists.size})",
        collapsed = collapsed,
        onClick = { onCollapseSection(section) },
    )

    if (!collapsed) {
        artists
            .filter {
                it.name.contains(filterText, ignoreCase = true) ||
                    it.label.contains(filterText, ignoreCase = true)
            }
            .forEach {
                RelationListItem(
                    relation = it,
                    onItemClick = onItemClick,
                )
            }
    }
}

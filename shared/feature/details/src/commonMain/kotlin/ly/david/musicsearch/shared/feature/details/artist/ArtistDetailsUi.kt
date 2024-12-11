package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.common.ifNotNullOrEmpty
import ly.david.musicsearch.shared.domain.getLifeSpanForDisplay
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
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
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.text.TextWithHeading
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
    artistsAndUrlsRelationsUiState: RelationsUiState,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onCollapseSection: (ArtistDetailsSection) -> Unit = {},
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(
        modifier = modifier,
        state = artistsAndUrlsRelationsUiState.lazyListState,
    ) {
        // Hack to preserve scroll position when returning
        items(1 + artistsAndUrlsRelationsUiState.lazyPagingItems.itemCount) { index ->
            if (index == 0) {
                Column {
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
                    }
                }
            } else {
                artistsAndUrlsRelationsUiState.lazyPagingItems.get(index - 1)?.let {
                    RelationListItem(
                        relation = it,
                        onItemClick = { entity, id, title ->
                            if (entity == MusicBrainzEntity.URL) {
                                uriHandler.openUri(title.orEmpty())
                            } else {
                                onItemClick(
                                    entity,
                                    id,
                                    title,
                                )
                            }
                        },
                    )
                }
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

@Composable
private fun MembersAndGroupsSection(
    artist: ArtistDetailsModel,
    modifier: Modifier = Modifier,
    filterText: String = "",
    collapsedSections: Set<ArtistDetailsSection> = setOf(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onCollapseSection: (ArtistDetailsSection) -> Unit = {},
) {
    Column(modifier = modifier) {
        MembersOrGroupsSection(
            section = ArtistDetailsSection.PartOfGroups,
            artists = artist.membersAndGroups.partOfGroups,
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )
        MembersOrGroupsSection(
            section = ArtistDetailsSection.PreviouslyPartOfGroups,
            artists = artist.membersAndGroups.previouslyPartOfGroups,
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )

        MembersOrGroupsSection(
            section = ArtistDetailsSection.MembersOfGroup,
            artists = artist.membersAndGroups.membersOfGroup,
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )
        MembersOrGroupsSection(
            section = ArtistDetailsSection.PreviousMembersOfGroup,
            artists = artist.membersAndGroups.previousMembersOfGroup,
            collapsedSections = collapsedSections,
            onCollapseSection = onCollapseSection,
            filterText = filterText,
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun MembersOrGroupsSection(
    section: ArtistDetailsSection,
    artists: List<RelationListItemModel>,
    collapsedSections: Set<ArtistDetailsSection>,
    onCollapseSection: (ArtistDetailsSection) -> Unit,
    filterText: String,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit,
) {
    if (artists.isEmpty()) return

    val collapsed = collapsedSections.contains(section)

    // The size may be misleading because a member could appear multiple times
    // if they are credited with multiple roles, or joined and left the group multiple times
    CollapsibleListSeparatorHeader(
        "${section.title} (${artists.size})",
        collapsed = collapsed,
        onClick = { onCollapseSection(section) },
    )

    if (!collapsed) {
        artists
            .filter {
                it.getNameWithDisambiguation().contains(filterText, ignoreCase = true) ||
                    it.attributes?.contains(filterText, ignoreCase = true) == true ||
                    it.additionalInfo?.contains(filterText, ignoreCase = true) == true ||
                    it.lifeSpan.getLifeSpanForDisplay().contains(filterText, ignoreCase = true)
            }
            .forEach {
                RelationListItem(
                    relation = it,
                    showLabel = false,
                    onItemClick = onItemClick,
                )
            }
    }
}

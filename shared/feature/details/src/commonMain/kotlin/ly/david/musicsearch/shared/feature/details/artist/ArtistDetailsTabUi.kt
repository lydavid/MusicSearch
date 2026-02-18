package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.common.getDateTimeFormatted
import ly.david.musicsearch.shared.domain.common.getDateTimePeriod
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listen.ListenWithRecording
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.shared.feature.details.area.AreaSection
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUi
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.component.ClickableItem
import ly.david.musicsearch.ui.common.icons.ChevronRight
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Headphones
import ly.david.musicsearch.ui.common.listitem.LifeSpanText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.formatPeriod
import ly.david.musicsearch.ui.common.relation.UrlListItem
import ly.david.musicsearch.ui.common.text.TextWithHeading
import ly.david.musicsearch.ui.common.text.TextWithIcon
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.born
import musicsearch.ui.common.generated.resources.created
import musicsearch.ui.common.generated.resources.date
import musicsearch.ui.common.generated.resources.died
import musicsearch.ui.common.generated.resources.dissolved
import musicsearch.ui.common.generated.resources.founded
import musicsearch.ui.common.generated.resources.gender
import musicsearch.ui.common.generated.resources.ipi
import musicsearch.ui.common.generated.resources.isni
import musicsearch.ui.common.generated.resources.listens
import musicsearch.ui.common.generated.resources.sortName
import musicsearch.ui.common.generated.resources.type
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
internal fun ArtistDetailsTabUi(
    artist: ArtistDetailsModel,
    modifier: Modifier = Modifier,
    detailsTabUiState: DetailsTabUiState = DetailsTabUiState(),
    filterText: String = "",
    onImageClick: () -> Unit = {},
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
    onCollapseExpandExternalLinks: () -> Unit = {},
    onCollapseExpandAliases: () -> Unit = {},
    onSeeAllListensClick: () -> Unit = {},
) {
    DetailsTabUi(
        detailsModel = artist,
        detailsTabUiState = detailsTabUiState,
        modifier = modifier,
        filterText = filterText,
        onImageClick = onImageClick,
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

                listenSection(
                    artist = this@run,
                    now = detailsTabUiState.now,
                    onSeeAllListensClick = onSeeAllListensClick,
                )
            }
        },
        onCollapseExpandAliases = onCollapseExpandAliases,
    )
}

@Composable
private fun ArtistDetailsModel.ArtistInformationSection(
    filterText: String = "",
) {
    sortName.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.sortName),
            text = it,
            filterText = filterText,
        )
    }
    type.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.type),
            text = it,
            filterText = filterText,
        )
    }
    gender.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.gender),
            text = it,
            filterText = filterText,
        )
    }
    LifeSpanText(
        lifeSpan = lifeSpan,
        heading = stringResource(Res.string.date),
        beginHeading = when (type) {
            "Person" -> stringResource(Res.string.born)
            "Character" -> stringResource(Res.string.created)
            else -> stringResource(Res.string.founded)
        },
        endHeading = when (type) {
            "Person" -> stringResource(Res.string.died)
            // Characters do not "die": https://musicbrainz.org/doc/Artist
            else -> stringResource(Res.string.dissolved)
        },
        filterText = filterText,
    )

    ipis.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.ipi),
            text = it.joinToString(", "),
            filterText = filterText,
        )
    }

    isnis.ifNotEmpty {
        TextWithHeading(
            heading = stringResource(Res.string.isni),
            text = it.joinToString(", "),
            filterText = filterText,
        )
    }
}

private fun LazyListScope.listenSection(
    artist: ArtistDetailsModel,
    now: Instant,
    onSeeAllListensClick: () -> Unit,
) {
    if (artist.listenCount != null) {
        item {
            ListSeparatorHeader(stringResource(Res.string.listens))
        }
        item {
            ListItem(
                headlineContent = {
                    TextWithIcon(
                        imageVector = CustomIcons.Headphones,
                        text = artist.listenCount.toString(),
                    )
                },
            )
        }
        items(artist.latestListens) {
            LastListenedListItem(
                listen = it,
                now = now,
            )
        }
        item {
            ClickableItem(
                title = "See all listens",
                endIcon = CustomIcons.ChevronRight,
                onClick = onSeeAllListensClick,
            )
        }
        item {
            UrlListItem(
                relation = RelationListItemModel(
                    id = "listenbrainz_url",
                    type = "ListenBrainz",
                    linkedEntity = MusicBrainzEntityType.URL,
                    name = artist.listenBrainzUrl,
                    linkedEntityId = "listenbrainz_url",
                ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LastListenedListItem(
    listen: ListenWithRecording,
    now: Instant,
) {
    val instant = Instant.fromEpochMilliseconds(listen.listenedMs)
    val formattedDateTimePeriod = formatPeriod(instant.getDateTimePeriod(now = now))
    val formattedDateTime = instant.getDateTimeFormatted()

    val text = buildString {
        append(formattedDateTimePeriod)
        append(" ($formattedDateTime)")
        append(" - ")
        append(listen.getNameWithDisambiguation())
    }
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
}

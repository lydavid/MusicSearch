package ly.david.musicsearch.ui.common.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.artist.ArtistListItem
import ly.david.musicsearch.ui.common.event.EventListItem
import ly.david.musicsearch.ui.common.genre.GenreListItem
import ly.david.musicsearch.ui.common.instrument.InstrumentListItem
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.place.PlaceListItem
import ly.david.musicsearch.ui.common.recording.RecordingListItem
import ly.david.musicsearch.ui.common.relation.RelationListItem
import ly.david.musicsearch.ui.common.release.ReleaseListItem
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupListItem
import ly.david.musicsearch.ui.common.series.SeriesListItem
import ly.david.musicsearch.ui.common.work.WorkListItem

@Suppress("CyclomaticComplexMethod")
@Composable
fun EntitiesPagingListUi(
    uiState: EntitiesPagingListUiState,
    modifier: Modifier = Modifier,
    selectedIds: ImmutableSet<String> = persistentSetOf(),
    now: Instant = Clock.System.now(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onSelect: (String) -> Unit = {},
    requestForMissingCoverArtUrl: suspend (id: String) -> Unit = { _ -> },
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = uiState.lazyPagingItems,
        modifier = modifier,
        lazyListState = uiState.lazyListState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is AreaListItemModel -> {
                AreaListItem(
                    area = listItemModel,
                    onAreaClick = {
                        onItemClick(
                            MusicBrainzEntity.AREA,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is ArtistListItemModel -> {
                ArtistListItem(
                    artist = listItemModel,
                    onClick = {
                        onItemClick(
                            MusicBrainzEntity.ARTIST,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is EventListItemModel -> {
                EventListItem(
                    event = listItemModel,
                    onEventClick = {
                        onItemClick(
                            MusicBrainzEntity.EVENT,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is GenreListItemModel -> {
                GenreListItem(
                    genre = listItemModel,
                    onGenreClick = {
                        onItemClick(
                            MusicBrainzEntity.GENRE,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is InstrumentListItemModel -> {
                InstrumentListItem(
                    instrument = listItemModel,
                    onInstrumentClick = {
                        onItemClick(
                            MusicBrainzEntity.INSTRUMENT,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is LabelListItemModel -> {
                LabelListItem(
                    label = listItemModel,
                    onLabelClick = {
                        onItemClick(
                            MusicBrainzEntity.LABEL,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is PlaceListItemModel -> {
                PlaceListItem(
                    place = listItemModel,
                    onPlaceClick = {
                        onItemClick(
                            MusicBrainzEntity.PLACE,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is RecordingListItemModel -> {
                RecordingListItem(
                    recording = listItemModel,
                    onRecordingClick = {
                        onItemClick(
                            MusicBrainzEntity.RECORDING,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is ReleaseListItemModel -> {
                ReleaseListItem(
                    release = listItemModel,
                    showMoreInfo = uiState.showMoreInfo,
                    requestForMissingCoverArtUrl = {
                        requestForMissingCoverArtUrl(listItemModel.id)
                    },
                    onClick = {
                        onItemClick(
                            MusicBrainzEntity.RELEASE,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is ReleaseGroupListItemModel -> {
                ReleaseGroupListItem(
                    releaseGroup = listItemModel,
                    showType = false,
                    requestForMissingCoverArtUrl = {
                        requestForMissingCoverArtUrl(listItemModel.id)
                    },
                    onClick = {
                        onItemClick(
                            MusicBrainzEntity.RELEASE_GROUP,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is SeriesListItemModel -> {
                SeriesListItem(
                    series = listItemModel,
                    onSeriesClick = {
                        onItemClick(
                            MusicBrainzEntity.SERIES,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is WorkListItemModel -> {
                WorkListItem(
                    work = listItemModel,
                    onWorkClick = {
                        onItemClick(
                            MusicBrainzEntity.WORK,
                            id,
                            getNameWithDisambiguation(),
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                )
            }

            is RelationListItemModel -> {
                RelationListItem(
                    relation = listItemModel,
                    onItemClick = { entity, id, title ->
                        require(entity != MusicBrainzEntity.URL)
                        onItemClick(
                            entity,
                            id,
                            title,
                        )
                    },
                )
            }

            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is LastUpdatedFooter -> {
                LastUpdatedFooterItem(
                    lastUpdated = listItemModel.lastUpdated,
                    now = now,
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}

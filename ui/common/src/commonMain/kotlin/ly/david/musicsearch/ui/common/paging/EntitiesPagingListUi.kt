package ly.david.musicsearch.ui.common.paging

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
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
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.artist.ArtistListItem
import ly.david.musicsearch.ui.common.event.EventListItem
import ly.david.musicsearch.ui.common.genre.GenreListItem
import ly.david.musicsearch.ui.common.instrument.InstrumentListItem
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.listitem.LastUpdatedFooterItem
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.place.PlaceListItem
import ly.david.musicsearch.ui.common.recording.RecordingListItem
import ly.david.musicsearch.ui.common.relation.RelationListItem
import ly.david.musicsearch.ui.common.release.ReleaseListItem
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupListItem
import ly.david.musicsearch.ui.common.series.SeriesListItem
import ly.david.musicsearch.ui.common.work.WorkListItem
import kotlin.time.Clock
import kotlin.time.Instant

@Suppress("CyclomaticComplexMethod")
@Composable
fun EntitiesPagingListUi(
    uiState: EntitiesPagingListUiState,
    modifier: Modifier = Modifier,
    selectedIds: ImmutableSet<String> = persistentSetOf(),
    now: Instant = Clock.System.now(),
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
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
                            MusicBrainzEntityType.AREA,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is ArtistListItemModel -> {
                ArtistListItem(
                    artist = listItemModel,
                    onClick = {
                        onItemClick(
                            MusicBrainzEntityType.ARTIST,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is EventListItemModel -> {
                EventListItem(
                    event = listItemModel,
                    onEventClick = {
                        onItemClick(
                            MusicBrainzEntityType.EVENT,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is GenreListItemModel -> {
                GenreListItem(
                    genre = listItemModel,
                    onGenreClick = {
                        onItemClick(
                            MusicBrainzEntityType.GENRE,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is InstrumentListItemModel -> {
                InstrumentListItem(
                    instrument = listItemModel,
                    onInstrumentClick = {
                        onItemClick(
                            MusicBrainzEntityType.INSTRUMENT,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is LabelListItemModel -> {
                LabelListItem(
                    label = listItemModel,
                    onLabelClick = {
                        onItemClick(
                            MusicBrainzEntityType.LABEL,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is PlaceListItemModel -> {
                PlaceListItem(
                    place = listItemModel,
                    onPlaceClick = {
                        onItemClick(
                            MusicBrainzEntityType.PLACE,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is RecordingListItemModel -> {
                RecordingListItem(
                    recording = listItemModel,
                    onRecordingClick = {
                        onItemClick(
                            MusicBrainzEntityType.RECORDING,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
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
                            MusicBrainzEntityType.RELEASE,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
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
                            MusicBrainzEntityType.RELEASE_GROUP,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is SeriesListItemModel -> {
                SeriesListItem(
                    series = listItemModel,
                    onSeriesClick = {
                        onItemClick(
                            MusicBrainzEntityType.SERIES,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is WorkListItemModel -> {
                WorkListItem(
                    work = listItemModel,
                    onWorkClick = {
                        onItemClick(
                            MusicBrainzEntityType.WORK,
                            id,
                        )
                    },
                    isSelected = selectedIds.contains(listItemModel.id),
                    onSelect = onSelect,
                    onEditCollectionClick = onEditCollectionClick,
                )
            }

            is RelationListItemModel -> {
                RelationListItem(
                    relation = listItemModel,
                    onItemClick = { entity, id ->
                        require(entity != MusicBrainzEntityType.URL)
                        onItemClick(
                            entity,
                            id,
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

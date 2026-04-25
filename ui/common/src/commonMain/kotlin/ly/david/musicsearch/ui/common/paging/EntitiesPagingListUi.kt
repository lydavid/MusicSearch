package ly.david.musicsearch.ui.common.paging

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListSeparator
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
import ly.david.musicsearch.ui.common.releasegroup.getDisplayString
import ly.david.musicsearch.ui.common.series.SeriesListItem
import ly.david.musicsearch.ui.common.work.WorkListItem
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.showingXOfY
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Suppress("CyclomaticComplexMethod")
@Composable
fun EntitiesPagingListUi(
    uiState: EntitiesPagingListUiState,
    filterText: String,
    now: Instant,
    modifier: Modifier = Modifier,
    onItemClick: MusicBrainzItemClickHandler = { _, _ -> },
    selectedIds: ImmutableList<String> = persistentListOf(),
    onSelect: (String) -> Unit = {},
    onEditCollectionClick: (String) -> Unit = {},
    requestForMissingCoverArtUrl: suspend (id: String) -> Unit = { _ -> },
    onLogin: () -> Unit = {},
) {
    val filteredCount = uiState.filteredCount
    val totalCount = uiState.totalCount
    val showHeader = filteredCount != 0 && filteredCount != totalCount
    val header: @Composable (() -> Unit)? = if (showHeader) {
        {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                text = stringResource(
                    Res.string.showingXOfY,
                    filteredCount,
                    totalCount,
                ),
            )
        }
    } else {
        null
    }

    ScreenWithPagingLoadingAndError(
        lazyPagingItems = uiState.lazyPagingItems,
        modifier = modifier,
        lazyListState = uiState.lazyListState,
        keyed = true,
        onLogin = onLogin,
        header = header,
        itemContent = { listItemModel: ListItemModel? ->
            when (listItemModel) {
                is AreaListItemModel -> {
                    AreaListItem(
                        area = listItemModel,
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
                        showType = uiState.showMoreInfo,
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
                        filterText = filterText,
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
                        filterText = filterText,
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
                        filterText = filterText,
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

                is ReleaseGroupListSeparator -> {
                    ListSeparatorHeader(text = listItemModel.types.getDisplayString())
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
        },
    )
}

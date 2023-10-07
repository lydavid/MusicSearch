package ly.david.musicsearch.feature.search.internal

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import ly.david.data.core.getNameWithDisambiguation
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.domain.listitem.AreaListItemModel
import ly.david.musicsearch.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.domain.listitem.EndOfList
import ly.david.musicsearch.domain.listitem.EventListItemModel
import ly.david.musicsearch.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.domain.listitem.LabelListItemModel
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.musicsearch.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.domain.listitem.WorkListItemModel
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.artist.ArtistListItem
import ly.david.ui.common.event.EventListItem
import ly.david.ui.common.instrument.InstrumentListItem
import ly.david.ui.common.label.LabelListItem
import ly.david.ui.common.paging.PagingLoadingAndErrorHandler
import ly.david.ui.common.place.PlaceListItem
import ly.david.ui.common.recording.RecordingListItem
import ly.david.ui.common.release.ReleaseListItem
import ly.david.ui.common.releasegroup.ReleaseGroupListItem
import ly.david.ui.common.series.SeriesListItem
import ly.david.ui.common.work.WorkListItem

@Composable
internal fun SearchResultsScreen(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState = rememberLazyListState(),
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    PagingLoadingAndErrorHandler(
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
        snackbarHostState = snackbarHostState,
        customNoResultsText = strings.noResultsFoundSearch,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ArtistListItemModel -> {
                ArtistListItem(artist = listItemModel) {
                    onItemClick(MusicBrainzEntity.ARTIST, id, null)
                }
            }

            is ReleaseGroupListItemModel -> {
                // TODO: should see album type rather than year
                ReleaseGroupListItem(releaseGroup = listItemModel) {
                    onItemClick(MusicBrainzEntity.RELEASE_GROUP, id, getNameWithDisambiguation())
                }
            }

            is ReleaseListItemModel -> {
                ReleaseListItem(release = listItemModel) {
                    onItemClick(MusicBrainzEntity.RELEASE, id, getNameWithDisambiguation())
                }
            }

            is RecordingListItemModel -> {
                RecordingListItem(recording = listItemModel) {
                    onItemClick(MusicBrainzEntity.RECORDING, id, getNameWithDisambiguation())
                }
            }

            is WorkListItemModel -> {
                WorkListItem(work = listItemModel) {
                    onItemClick(MusicBrainzEntity.WORK, id, getNameWithDisambiguation())
                }
            }

            is AreaListItemModel -> {
                AreaListItem(area = listItemModel) {
                    onItemClick(MusicBrainzEntity.AREA, id, getNameWithDisambiguation())
                }
            }

            is PlaceListItemModel -> {
                PlaceListItem(place = listItemModel) {
                    onItemClick(MusicBrainzEntity.PLACE, id, getNameWithDisambiguation())
                }
            }

            is InstrumentListItemModel -> {
                InstrumentListItem(instrument = listItemModel) {
                    onItemClick(MusicBrainzEntity.INSTRUMENT, id, getNameWithDisambiguation())
                }
            }

            is LabelListItemModel -> {
                LabelListItem(label = listItemModel) {
                    onItemClick(MusicBrainzEntity.LABEL, id, getNameWithDisambiguation())
                }
            }

            is EventListItemModel -> {
                EventListItem(event = listItemModel) {
                    onItemClick(MusicBrainzEntity.EVENT, id, getNameWithDisambiguation())
                }
            }

            is SeriesListItemModel -> {
                SeriesListItem(series = listItemModel) {
                    onItemClick(MusicBrainzEntity.SERIES, id, getNameWithDisambiguation())
                }
            }

            is EndOfList -> {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    text = "End of search results."
                )
            }

            else -> {
                // Null. Do nothing.
            }
        }
    }
}

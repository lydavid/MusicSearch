package ly.david.musicsearch.shared.feature.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.EndOfList
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.listitem.LabelListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHeader
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.artist.ArtistListItem
import ly.david.musicsearch.ui.common.event.EventListItem
import ly.david.musicsearch.ui.common.instrument.InstrumentListItem
import ly.david.musicsearch.ui.common.label.LabelListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.place.PlaceListItem
import ly.david.musicsearch.ui.common.recording.RecordingListItem
import ly.david.musicsearch.ui.common.release.ReleaseListItem
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupListItem
import ly.david.musicsearch.ui.common.series.SeriesListItem
import ly.david.musicsearch.ui.common.work.WorkListItem
import ly.david.musicsearch.ui.core.LocalStrings

@Composable
internal fun SearchResultsUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState = rememberLazyListState(),
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
) {
    val strings = LocalStrings.current

    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        lazyListState = lazyListState,
        customNoResultsText = strings.noResultsFoundSearch,
        keyed = false,
    ) { listItemModel: ListItemModel? ->
        ListItemUi(
            listItemModel = listItemModel,
            onItemClick = onItemClick,
        )
    }
}

@Composable
private fun ListItemUi(
    listItemModel: ListItemModel?,
    onItemClick: MusicBrainzItemClickHandler,
) {
    when (listItemModel) {
        is SearchHeader -> {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                text = "Found ${listItemModel.remoteCount} results.",
            )
        }

        is ArtistListItemModel -> {
            ArtistListItem(artist = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.ARTIST,
                    id,
                    null,
                )
            }
        }

        is ReleaseGroupListItemModel -> {
            ReleaseGroupListItem(
                releaseGroup = listItemModel,
                showType = true,
            ) {
                onItemClick(
                    MusicBrainzEntity.RELEASE_GROUP,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is ReleaseListItemModel -> {
            ReleaseListItem(release = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.RELEASE,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is RecordingListItemModel -> {
            RecordingListItem(recording = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.RECORDING,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is WorkListItemModel -> {
            WorkListItem(work = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.WORK,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is AreaListItemModel -> {
            AreaListItem(
                area = listItemModel,
                showType = true,
            ) {
                onItemClick(
                    MusicBrainzEntity.AREA,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is PlaceListItemModel -> {
            PlaceListItem(place = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.PLACE,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is InstrumentListItemModel -> {
            InstrumentListItem(instrument = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.INSTRUMENT,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is LabelListItemModel -> {
            LabelListItem(label = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.LABEL,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is EventListItemModel -> {
            EventListItem(event = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.EVENT,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is SeriesListItemModel -> {
            SeriesListItem(series = listItemModel) {
                onItemClick(
                    MusicBrainzEntity.SERIES,
                    id,
                    getNameWithDisambiguation(),
                )
            }
        }

        is EndOfList -> {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                text = "End of search results.",
            )
        }

        else -> {
            // Null. Do nothing.
        }
    }
}

package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.collections.areas.EntitiesByCollection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun CollectionPlatformContent(
    collectionId: String,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    isRemote: Boolean,
    filterText: String,
    entity: MusicBrainzEntity?,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    showMoreInfoInReleaseListItem: Boolean,
    sortReleaseGroupListItems: Boolean,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit,
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit,
) {
    EntitiesByCollection(
        collectionId = collectionId,
        lazyPagingItems = lazyPagingItems,
        isRemote = isRemote,
        filterText = filterText,
        snackbarHostState = snackbarHostState,
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        onDeleteFromCollection = onDeleteFromCollection,
        onItemClick = onItemClick,
    )
//    when (entity) {
//        MusicBrainzEntity.AREA -> {
//
//        }
//
//        MusicBrainzEntity.ARTIST -> {
//            ArtistsByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onArtistClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.EVENT -> {
//            EventsByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onEventClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.INSTRUMENT -> {
//            InstrumentsByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onInstrumentClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.LABEL -> {
//            LabelsByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onLabelClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.PLACE -> {
//            PlacesByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onPlaceClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.RECORDING -> {
//            RecordingsByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onRecordingClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.RELEASE -> {
//            ReleasesByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                showMoreInfo = showMoreInfoInReleaseListItem,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onReleaseClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.RELEASE_GROUP -> {
//            ReleaseGroupsByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                isSorted = sortReleaseGroupListItems,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onReleaseGroupClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.SERIES -> {
//            SeriesByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onSeriesClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.WORK -> {
//            WorksByCollectionScreen(
//                collectionId = collectionId,
//                isRemote = isRemote,
//                filterText = filterText,
//                snackbarHostState = snackbarHostState,
//                modifier = Modifier
//                    .padding(innerPadding)
//                    .fillMaxSize()
//                    .nestedScroll(scrollBehavior.nestedScrollConnection),
//                onDeleteFromCollection = onDeleteFromCollection,
//                onWorkClick = onItemClick,
//            )
//        }
//
//        MusicBrainzEntity.COLLECTION,
//        MusicBrainzEntity.GENRE,
//        MusicBrainzEntity.URL,
//        -> {
//            error("Collections by ${entity.name} not supported.")
//        }
//
//        null -> {
//            FullScreenLoadingIndicator()
//        }
//    }
}

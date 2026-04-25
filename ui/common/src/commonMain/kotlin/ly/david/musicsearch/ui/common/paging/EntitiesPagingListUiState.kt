package ly.david.musicsearch.ui.common.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.list.showTypes
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.ui.common.list.AllEntitiesListUiState
import ly.david.musicsearch.ui.common.topappbar.Tab

data class EntitiesPagingListUiState(
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val lazyListState: LazyListState,
    val totalCount: Int = 0,
    val filteredCount: Int = 0,
    val showMoreInfo: Boolean = true,
)

@Composable
fun AllEntitiesListUiState.toEntitiesPagingListUiState(
    tab: Tab?,
    entitiesLazyPagingItems: EntitiesLazyPagingItems,
): EntitiesPagingListUiState = when (tab) {
    Tab.AREAS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.areasLazyPagingItems,
            lazyListState = this.areasListUiState.lazyListState,
            totalCount = this.areasListUiState.totalCount,
            filteredCount = this.areasListUiState.filteredCount,
        )
    }

    Tab.ARTISTS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.artistsLazyPagingItems,
            lazyListState = this.artistsListUiState.lazyListState,
            totalCount = this.artistsListUiState.totalCount,
            filteredCount = this.artistsListUiState.filteredCount,
        )
    }

    Tab.EVENTS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.eventsLazyPagingItems,
            lazyListState = this.eventsListUiState.lazyListState,
            totalCount = this.eventsListUiState.totalCount,
            filteredCount = this.eventsListUiState.filteredCount,
        )
    }

    Tab.GENRES -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.genresLazyPagingItems,
            lazyListState = this.genresListUiState.lazyListState,
            totalCount = this.genresListUiState.totalCount,
            filteredCount = this.genresListUiState.filteredCount,
        )
    }

    Tab.INSTRUMENTS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.instrumentsLazyPagingItems,
            lazyListState = this.instrumentsListUiState.lazyListState,
            totalCount = this.instrumentsListUiState.totalCount,
            filteredCount = this.instrumentsListUiState.filteredCount,
        )
    }

    Tab.LABELS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.labelsLazyPagingItems,
            lazyListState = this.labelsListUiState.lazyListState,
            totalCount = this.labelsListUiState.totalCount,
            filteredCount = this.labelsListUiState.filteredCount,
        )
    }

    Tab.PLACES -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.placesLazyPagingItems,
            lazyListState = this.placesListUiState.lazyListState,
            totalCount = this.placesListUiState.totalCount,
            filteredCount = this.placesListUiState.filteredCount,
        )
    }

    Tab.RECORDINGS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.recordingsLazyPagingItems,
            lazyListState = this.recordingsListUiState.lazyListState,
            totalCount = this.recordingsListUiState.totalCount,
            filteredCount = this.recordingsListUiState.filteredCount,
        )
    }

    Tab.RELEASES -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.releasesLazyPagingItems,
            lazyListState = this.releasesListUiState.lazyListState,
            showMoreInfo = (this.releasesListUiState.listFilters as ListFilters.Releases).showMoreInfo,
            totalCount = this.releasesListUiState.totalCount,
            filteredCount = this.releasesListUiState.filteredCount,
        )
    }

    Tab.RELEASE_GROUPS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.releaseGroupsLazyPagingItems,
            lazyListState = this.releaseGroupsListUiState.lazyListState,
            showMoreInfo = this.releaseGroupsListUiState.listFilters.showTypes(),
            totalCount = this.releaseGroupsListUiState.totalCount,
            filteredCount = this.releaseGroupsListUiState.filteredCount,
        )
    }

    Tab.SERIES -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.seriesLazyPagingItems,
            lazyListState = this.seriesListUiState.lazyListState,
            totalCount = this.seriesListUiState.totalCount,
            filteredCount = this.seriesListUiState.filteredCount,
        )
    }

    Tab.WORKS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.worksLazyPagingItems,
            lazyListState = this.worksListUiState.lazyListState,
            totalCount = this.worksListUiState.totalCount,
            filteredCount = this.worksListUiState.filteredCount,
        )
    }

    Tab.RELATIONSHIPS -> {
        EntitiesPagingListUiState(
            lazyPagingItems = entitiesLazyPagingItems.relationsLazyPagingItems,
            lazyListState = this.relationsUiState.lazyListState,
            totalCount = this.relationsUiState.totalCount,
            filteredCount = this.relationsUiState.filteredCount,
        )
    }

    Tab.DETAILS,
    Tab.TRACKS,
    null,
    -> {
        error("$tab does not map to ${EntitiesPagingListUiState::class.simpleName}")
    }
}

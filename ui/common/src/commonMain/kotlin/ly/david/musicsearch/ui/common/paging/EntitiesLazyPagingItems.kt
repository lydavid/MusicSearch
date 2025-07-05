package ly.david.musicsearch.ui.common.paging

import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.shared.domain.listitem.EntityListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.ui.common.topappbar.Tab

data class EntitiesLazyPagingItems(
    val areasLazyPagingItems: LazyPagingItems<ListItemModel>,
    val artistsLazyPagingItems: LazyPagingItems<ListItemModel>,
    val eventsLazyPagingItems: LazyPagingItems<ListItemModel>,
    val genresLazyPagingItems: LazyPagingItems<ListItemModel>,
    val instrumentsLazyPagingItems: LazyPagingItems<ListItemModel>,
    val labelsLazyPagingItems: LazyPagingItems<ListItemModel>,
    val placesLazyPagingItems: LazyPagingItems<ListItemModel>,
    val recordingsLazyPagingItems: LazyPagingItems<ListItemModel>,
    val releasesLazyPagingItems: LazyPagingItems<ListItemModel>,
    val releaseGroupsLazyPagingItems: LazyPagingItems<ListItemModel>,
    val seriesLazyPagingItems: LazyPagingItems<ListItemModel>,
    val worksLazyPagingItems: LazyPagingItems<ListItemModel>,
    val relationsLazyPagingItems: LazyPagingItems<ListItemModel>,
    val tracksLazyPagingItems: LazyPagingItems<ListItemModel>,
)

fun EntitiesLazyPagingItems.getLazyPagingItemsForTab(tab: Tab?): LazyPagingItems<ListItemModel>? {
    return when (tab) {
        Tab.AREAS -> areasLazyPagingItems
        Tab.ARTISTS -> artistsLazyPagingItems
        Tab.EVENTS -> eventsLazyPagingItems
        Tab.GENRES -> genresLazyPagingItems
        Tab.INSTRUMENTS -> instrumentsLazyPagingItems
        Tab.LABELS -> labelsLazyPagingItems
        Tab.PLACES -> placesLazyPagingItems
        Tab.RECORDINGS -> recordingsLazyPagingItems
        Tab.RELEASES -> releasesLazyPagingItems
        Tab.RELEASE_GROUPS -> releaseGroupsLazyPagingItems
        Tab.SERIES -> seriesLazyPagingItems
        Tab.WORKS -> worksLazyPagingItems
        Tab.RELATIONSHIPS -> relationsLazyPagingItems
        Tab.TRACKS -> tracksLazyPagingItems
        Tab.DETAILS,
        Tab.STATS,
        null,
        -> null
    }
}

fun EntitiesLazyPagingItems.getLoadedIdsForTab(tab: Tab?): List<String> {
    return getLazyPagingItemsForTab(tab)?.itemSnapshotList?.items
        ?.filterIsInstance<EntityListItemModel>()
        ?.map { item -> item.id }
        .orEmpty()
}

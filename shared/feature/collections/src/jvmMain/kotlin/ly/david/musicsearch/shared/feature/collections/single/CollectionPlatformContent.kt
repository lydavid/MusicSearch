package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

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
    // TODO:
}

package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal expect fun CollectionPlatformContent(
    collectionId: String,
    isRemote: Boolean,
    filterText: String,
    entity: MusicBrainzEntity?,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    showMoreInfoInReleaseListItem: Boolean = true,
    sortReleaseGroupListItems: Boolean = false,
    onItemClick: (entity: MusicBrainzEntity, id: String, title: String?) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (collectableId: String, name: String) -> Unit = { _, _ -> },
)

package ly.david.musicsearch.domain.collection

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

interface CollectionRepository {
    fun observeAllCollections(
        showLocal: Boolean,
        showRemote: Boolean,
        query: String,
        entity: MusicBrainzEntity?,
    ): Flow<PagingData<CollectionListItemModel>>
}

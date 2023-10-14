package ly.david.musicsearch.domain.collection

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.core.listitem.CollectionListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity

interface CollectionRepository {
    fun observeAllCollections(
        showLocal: Boolean,
        showRemote: Boolean,
        query: String,
        entity: MusicBrainzEntity?,
    ): Flow<PagingData<CollectionListItemModel>>
}

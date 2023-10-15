package ly.david.musicsearch.domain.releasegroup

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel

interface ReleaseGroupRepository {
    suspend fun lookupReleaseGroup(releaseGroupId: String): ReleaseGroupScaffoldModel

    fun observeReleaseGroupsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        query: String,
        isRemote: Boolean,
        sorted: Boolean,
    ): Flow<PagingData<ListItemModel>>
}

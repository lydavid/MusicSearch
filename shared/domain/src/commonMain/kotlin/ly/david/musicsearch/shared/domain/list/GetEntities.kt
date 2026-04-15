package ly.david.musicsearch.shared.domain.list

import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.appendLastUpdatedFooter
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

interface GetEntities {
    operator fun invoke(
        entity: MusicBrainzEntityType,
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>>
}

class GetEntitiesImpl(
    private val entitiesListRepository: EntitiesListRepository,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntities {
    override operator fun invoke(
        entity: MusicBrainzEntityType,
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            entitiesListRepository.observeEntities(
                entity = entity,
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .cachedIn(scope = coroutineScope)
                .appendLastUpdatedFooter(
                    browseRemoteMetadataRepository = browseRemoteMetadataRepository,
                    browseMethod = browseMethod,
                    browseEntity = entity,
                )
                .distinctUntilChanged()
        }
    }
}

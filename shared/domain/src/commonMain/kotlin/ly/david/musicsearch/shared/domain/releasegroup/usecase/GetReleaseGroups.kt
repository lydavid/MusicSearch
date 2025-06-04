package ly.david.musicsearch.shared.domain.releasegroup.usecase

import androidx.paging.PagingData
import app.cash.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.base.usecase.GetEntitiesByEntity
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.appendLastUpdatedBanner
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository

interface GetReleaseGroups {
    operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>>
}

class GetReleaseGroupsImpl(
    private val releaseGroupsListRepository: ReleaseGroupsListRepository,
    private val browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    private val coroutineScope: CoroutineScope,
) : GetEntitiesByEntity<ListItemModel>, GetReleaseGroups {
    override operator fun invoke(
        browseMethod: BrowseMethod?,
        listFilters: ListFilters,
    ): Flow<PagingData<ListItemModel>> {
        return if (browseMethod == null) {
            emptyFlow()
        } else {
            releaseGroupsListRepository.observeReleaseGroups(
                browseMethod = browseMethod,
                listFilters = listFilters,
            )
                .cachedIn(scope = coroutineScope)
                .appendLastUpdatedBanner(
                    browseRemoteMetadataRepository = browseRemoteMetadataRepository,
                    browseMethod = browseMethod,
                    browseEntity = MusicBrainzEntity.RELEASE_GROUP,
                )
                .distinctUntilChanged()
        }
    }
}

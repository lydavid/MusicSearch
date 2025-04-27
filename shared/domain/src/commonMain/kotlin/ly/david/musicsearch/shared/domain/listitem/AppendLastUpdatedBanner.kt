package ly.david.musicsearch.shared.domain.listitem

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertFooterItem
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : ListItemModel> Flow<PagingData<T>>.appendLastUpdatedBanner(
    browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    browseMethod: BrowseMethod,
    browseEntity: MusicBrainzEntity,
): Flow<PagingData<ListItemModel>> = flatMapMerge { releaseGroups ->
    val browseByEntity = browseMethod as? BrowseMethod.ByEntity
    if (browseByEntity != null) {
        browseRemoteMetadataRepository.observe(
            entityId = browseByEntity.entityId,
            entity = browseEntity,
        ).map { browseRemoteMetadata ->
            Pair(releaseGroups, browseRemoteMetadata)
        }
    } else {
        flowOf(Pair(releaseGroups, null))
    }
}
    .map { (pagingData, browseRemoteMetadata) ->
        if (browseRemoteMetadata == null) return@map pagingData.map { it as T }

        pagingData
            .map { it as ListItemModel }
            .insertFooterItem(
                terminalSeparatorType = TerminalSeparatorType.SOURCE_COMPLETE,
                item = LastUpdatedFooter(lastUpdated = browseRemoteMetadata.lastUpdated),
            )
    }

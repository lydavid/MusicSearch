package ly.david.musicsearch.shared.domain.listitem

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertFooterItem
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : ListItemModel> Flow<PagingData<T>>.appendLastUpdatedBanner(
    browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    browseMethod: BrowseMethod,
    browseEntity: MusicBrainzEntity,
): Flow<PagingData<ListItemModel>> {
    return transformLatest { listItems ->
        val browseByEntity = browseMethod as? BrowseMethod.ByEntity
        if (browseByEntity != null) {
            browseRemoteMetadataRepository.observe(
                entityId = browseByEntity.entityId,
                entity = browseEntity,
            ).collect { browseRemoteMetadata ->
                if (browseRemoteMetadata == null) {
                    emit(listItems.map { it as ListItemModel })
                } else {
                    emit(
                        listItems
                            .map { it as ListItemModel }
                            .insertFooterItem(
                                terminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
                                item = LastUpdatedFooter(lastUpdated = browseRemoteMetadata.lastUpdated),
                            ),
                    )
                }
            }
        } else {
            emit(listItems.map { it as ListItemModel })
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T : ListItemModel> Flow<PagingData<T>>.appendPlaceholderLastUpdatedBanner(): Flow<PagingData<ListItemModel>> {
    return transformLatest { listItems ->
        emit(listItems.map { it as ListItemModel })
    }
}

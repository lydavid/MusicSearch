package ly.david.musicsearch.shared.domain.listitem

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertFooterItem
import androidx.paging.map
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

// TODO: this can still cause the last updated footer to appear before the rest of the content
//  and when using lazy item key, it will stay on screen, so content won't start at top
@OptIn(ExperimentalCoroutinesApi::class)
fun <T : ListItemModel> Flow<PagingData<T>>.appendLastUpdatedBanner(
    browseRemoteMetadataRepository: BrowseRemoteMetadataRepository,
    browseMethod: BrowseMethod,
    browseEntity: MusicBrainzEntityType,
): Flow<PagingData<ListItemModel>> {
    val browseByEntity = browseMethod as? BrowseMethod.ByEntity

    return if (browseByEntity != null) {
        val metadataFlow = browseRemoteMetadataRepository.observe(
            entityId = browseByEntity.entityId,
            entity = browseEntity,
        )

        combine(this, metadataFlow) { listItems, browseRemoteMetadata ->
            val mappedItems = listItems.map { it as ListItemModel }

            if (browseRemoteMetadata != null && browseEntity != MusicBrainzEntityType.RELEASE_GROUP) {
                mappedItems.insertFooterItem(
                    terminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
                    item = LastUpdatedFooter(lastUpdated = browseRemoteMetadata.lastUpdated),
                )
            } else {
                mappedItems
            }
        }
    } else {
        map { listItems -> listItems.map { it as ListItemModel } }
    }
}

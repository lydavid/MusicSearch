package ly.david.mbjc.ui.collections.events

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.EventListItemModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.event.EventRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.EventsByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class EventsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val relationDao: RelationDao,
    eventDao: EventDao,
    pagedList: PagedList<EventRoomModel, EventListItemModel>,
) : EventsByEntityViewModel(
    relationDao = relationDao,
    eventDao = eventDao,
    pagedList = pagedList
) {

    override suspend fun browseEventsByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApiService.browseEventsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, eventMusicBrainzModels: List<EventMusicBrainzModel>) {
        collectionEntityDao.insertAll(
            eventMusicBrainzModels.map { event ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = event.id
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteCollectionEntityLinks(resourceId)
            relationDao.deleteBrowseResourceCountByResource(resourceId, MusicBrainzResource.EVENT)
        }
    }

    override fun getLinkedResourcesPagingSource(
        resourceId: String,
        query: String
    ): PagingSource<Int, EventRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getEventsByCollection(resourceId)
        }
        else -> {
            collectionEntityDao.getEventsByCollectionFiltered(
                collectionId = resourceId,
                query = "%$query%"
            )
        }
    }
}

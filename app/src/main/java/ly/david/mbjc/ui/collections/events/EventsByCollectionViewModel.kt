package ly.david.mbjc.ui.collections.events

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.EventListItemModel
import ly.david.data.domain.toEventListItemModel
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.collection.CollectionEntityDao
import ly.david.data.persistence.collection.CollectionEntityRoomModel
import ly.david.data.persistence.event.EventDao
import ly.david.data.persistence.event.EventRoomModel
import ly.david.data.persistence.event.toEventRoomModel
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.mbjc.ui.common.paging.PagedList

@HiltViewModel
internal class EventsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val eventDao: EventDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<EventRoomModel, EventListItemModel>,
) : BrowseEntitiesByEntityViewModel
<EventRoomModel, EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
    byEntity = MusicBrainzResource.EVENT,
    relationDao = relationDao,
    pagedList = pagedList
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApiService.browseEventsByCollection(
            collectionId = entityId,
            offset = offset
        )
    }

    override suspend fun insertAllLinkingModels(entityId: String, musicBrainzModels: List<EventMusicBrainzModel>) {
        eventDao.insertAll(musicBrainzModels.map { it.toEventRoomModel() })
        collectionEntityDao.insertAll(
            musicBrainzModels.map { event ->
                CollectionEntityRoomModel(
                    id = entityId,
                    entityId = event.id
                )
            }
        )
    }

    override suspend fun deleteLinkedResourcesByResource(resourceId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(resourceId)
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

    override fun transformRoomToListItemModel(roomModel: EventRoomModel): EventListItemModel {
        return roomModel.toEventListItemModel()
    }
}

package ly.david.mbjc.ui.collections.events

import androidx.paging.PagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.domain.listitem.EventListItemModel
import ly.david.data.domain.listitem.toEventListItemModel
import ly.david.data.musicbrainz.MusicBrainzAuthState
import ly.david.data.musicbrainz.getBearerToken
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.network.api.BrowseEventsResponse
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.event.EventDao
import ly.david.data.room.event.EventRoomModel
import ly.david.data.room.event.toEventRoomModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import ly.david.ui.common.paging.PagedList

@HiltViewModel
internal class EventsByCollectionViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val collectionEntityDao: CollectionEntityDao,
    private val eventDao: EventDao,
    private val relationDao: RelationDao,
    pagedList: PagedList<EventRoomModel, EventListItemModel>,
    private val musicBrainzAuthState: MusicBrainzAuthState,
) : BrowseEntitiesByEntityViewModel<EventRoomModel, EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
    byEntity = MusicBrainzEntity.EVENT,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApiService.browseEventsByCollection(
            bearerToken = musicBrainzAuthState.getBearerToken(),
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

    override suspend fun deleteLinkedEntitiesByEntity(entityId: String) {
        collectionEntityDao.withTransaction {
            collectionEntityDao.deleteAllFromCollection(entityId)
            relationDao.deleteBrowseEntityCountByEntity(entityId, MusicBrainzEntity.EVENT)
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        query: String,
    ): PagingSource<Int, EventRoomModel> = when {
        query.isEmpty() -> {
            collectionEntityDao.getEventsByCollection(entityId)
        }
        else -> {
            collectionEntityDao.getEventsByCollectionFiltered(
                collectionId = entityId,
                query = "%$query%"
            )
        }
    }

    override fun transformRoomToListItemModel(roomModel: EventRoomModel): EventListItemModel {
        return roomModel.toEventListItemModel()
    }
}

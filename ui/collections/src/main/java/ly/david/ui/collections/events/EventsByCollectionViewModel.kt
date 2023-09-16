package ly.david.ui.collections.events

import androidx.paging.PagingSource
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.listitem.EventListItemModel
import ly.david.data.domain.listitem.toEventListItemModel
import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowseEventsResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.data.room.collection.CollectionEntityDao
import ly.david.data.room.collection.CollectionEntityRoomModel
import ly.david.data.room.event.RoomEventDao
import ly.david.data.room.event.EventRoomModel
import ly.david.data.room.event.toEventRoomModel
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.common.event.EventsPagedList
import ly.david.ui.common.paging.BrowseEntitiesByEntityViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class EventsByCollectionViewModel(
    private val musicBrainzApi: MusicBrainzApi,
    private val collectionEntityDao: CollectionEntityDao,
    private val eventDao: RoomEventDao,
    private val relationDao: RoomRelationDao,
    pagedList: EventsPagedList,
) : BrowseEntitiesByEntityViewModel<EventRoomModel, EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
    byEntity = MusicBrainzEntity.EVENT,
    relationDao = relationDao,
    pagedList = pagedList,
) {

    override suspend fun browseEntitiesByEntity(entityId: String, offset: Int): BrowseEventsResponse {
        return musicBrainzApi.browseEventsByCollection(
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

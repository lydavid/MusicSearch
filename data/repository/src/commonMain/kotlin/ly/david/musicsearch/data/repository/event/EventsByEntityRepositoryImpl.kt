package ly.david.musicsearch.data.repository.event

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class EventsByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val eventDao: EventDao,
    private val browseApi: BrowseApi,
) : EventsByEntityRepository,
    BrowseEntitiesByEntity<EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
        browseEntity = MusicBrainzEntity.EVENT,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeEventsByEntity(
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): Flow<PagingData<EventListItemModel>> {
        return observeEntitiesByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = listFilters,
        )
    }

    override fun deleteLinkedEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseEntityCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> {
                    eventDao.deleteEventsByEntity(entityId)
                }
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String?,
        entity: MusicBrainzEntity?,
        listFilters: ListFilters,
    ): PagingSource<Int, EventListItemModel> {
        return eventDao.getEvents(
            entityId = entityId,
            entity = entity,
            query = listFilters.query,
        )
    }

    override suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseEventsResponse {
        return browseApi.browseEventsByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<EventMusicBrainzModel>,
    ): Int {
        eventDao.insertAll(musicBrainzModels)
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { event -> event.id },
                )
            }

            else -> {
                eventDao.insertEventsByEntity(
                    entityId = entityId,
                    eventIds = musicBrainzModels.map { event -> event.id },
                )
            }
        }
    }

    override fun getLocalLinkedEntitiesCountByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ): Int {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            else -> {
                eventDao.getCountOfEventsByEntity(entityId)
            }
        }
    }
}

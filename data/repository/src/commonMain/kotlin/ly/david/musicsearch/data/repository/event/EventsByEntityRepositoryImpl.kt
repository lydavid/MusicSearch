package ly.david.musicsearch.data.repository.event

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.EventPlaceDao
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.domain.event.EventsByEntityRepository

class EventsByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val eventDao: EventDao,
    private val eventPlaceDao: EventPlaceDao,
    private val musicBrainzApi: MusicBrainzApi,
) : EventsByEntityRepository,
    BrowseEntitiesByEntity<EventListItemModel, EventMusicBrainzModel, BrowseEventsResponse>(
        browseEntity = MusicBrainzEntity.EVENT,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observeEventsByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
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

                MusicBrainzEntity.PLACE -> {
                    eventPlaceDao.deleteEventsByPlace(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, EventListItemModel> {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getEventsByCollection(
                    collectionId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.PLACE -> {
                eventPlaceDao.getEventsByPlace(
                    placeId = entityId,
                    query = listFilters.query,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override suspend fun browseEntities(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowseEventsResponse {
        return when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browseEventsByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.PLACE -> {
                musicBrainzApi.browseEventsByPlace(
                    placeId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<EventMusicBrainzModel>,
    ) {
        eventDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { event -> event.id },
                )
            }

            MusicBrainzEntity.PLACE -> {
                eventPlaceDao.insertAll(
                    eventAndPlaceIds = musicBrainzModels.map { event ->
                        event.id to entityId
                    },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity))
            }
        }
    }
}

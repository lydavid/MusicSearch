package ly.david.musicsearch.data.repository.event

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class EventsListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val eventDao: EventDao,
    private val browseApi: BrowseApi,
) : EventsListRepository,
    BrowseEntities<EventListItemModel, EventMusicBrainzNetworkModel, BrowseEventsResponse>(
        browseEntity = MusicBrainzEntity.EVENT,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
    ) {

    override fun observeEvents(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<EventListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, EventListItemModel> {
        return eventDao.getEvents(
            browseMethod = browseMethod,
            query = listFilters.query,
        )
    }

    override fun observeCountOfEvents(browseMethod: BrowseMethod?): Flow<Int> {
        if (browseMethod == null) return flowOf()
        return eventDao.observeCountOfEvents(browseMethod = browseMethod)
    }

    override fun deleteEntityLinksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteEntityLinksFromCollection(entityId)
                }

                else -> {
                    eventDao.deleteEventLinksByEntity(entityId)
                }
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
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
        musicBrainzModels: List<EventMusicBrainzNetworkModel>,
    ) {
        eventDao.insertAll(musicBrainzModels)
        when (entity) {
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

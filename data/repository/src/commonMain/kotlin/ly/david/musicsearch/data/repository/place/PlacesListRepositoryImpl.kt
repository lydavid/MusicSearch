package ly.david.musicsearch.data.repository.place

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.BrowseRemoteCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.PlacesListRepository

class PlacesListRepositoryImpl(
    private val browseEntityCountDao: BrowseRemoteCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val placeDao: PlaceDao,
    private val browseApi: BrowseApi,
) : PlacesListRepository,
    BrowseEntities<PlaceListItemModel, PlaceMusicBrainzModel, BrowsePlacesResponse>(
        browseEntity = MusicBrainzEntity.PLACE,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observePlaces(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): Flow<PagingData<PlaceListItemModel>> {
        return observeEntities(
            browseMethod = browseMethod,
            listFilters = listFilters,
        )
    }

    override fun observeCountOfAllPlaces(): Flow<Long> {
        return placeDao.observeCountOfAllPlaces()
    }

    override fun getLinkedEntitiesPagingSource(
        browseMethod: BrowseMethod,
        listFilters: ListFilters,
    ): PagingSource<Int, PlaceListItemModel> {
        return placeDao.getPlaces(
            browseMethod = browseMethod,
            query = listFilters.query,
        )
    }

    override fun deleteEntityLinksByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
    ) {
        browseEntityCountDao.withTransaction {
            browseEntityCountDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntity.AREA -> {
                    placeDao.deletePlacesByArea(entityId)
                }

                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteEntityLinksFromCollection(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        offset: Int,
    ): BrowsePlacesResponse {
        return browseApi.browsePlacesByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<PlaceMusicBrainzModel>,
    ) {
        placeDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.AREA -> {
                placeDao.insertPlacesByArea(
                    entityId = entityId,
                    placeIds = musicBrainzModels.map { place -> place.id },
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.insertAll(
                    collectionId = entityId,
                    entityIds = musicBrainzModels.map { place -> place.id },
                )
            }

            else -> {
                error(browseEntitiesNotSupported(entity))
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
                placeDao.getCountOfPlacesByArea(entityId)
            }
        }
    }
}

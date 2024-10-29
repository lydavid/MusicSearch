package ly.david.musicsearch.data.repository.place

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.repository.base.BrowseEntitiesByEntity
import ly.david.musicsearch.shared.domain.place.PlacesByEntityRepository

class PlacesByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val placeDao: PlaceDao,
    private val areaPlaceDao: AreaPlaceDao,
    private val browseApi: BrowseApi,
) : PlacesByEntityRepository,
    BrowseEntitiesByEntity<PlaceListItemModel, PlaceMusicBrainzModel, BrowsePlacesResponse>(
        browseEntity = MusicBrainzEntity.PLACE,
        browseEntityCountDao = browseEntityCountDao,
    ) {

    override fun observePlacesByEntity(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): Flow<PagingData<PlaceListItemModel>> {
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
                MusicBrainzEntity.AREA -> {
                    areaPlaceDao.deletePlacesByArea(entityId)
                }

                MusicBrainzEntity.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override fun getLinkedEntitiesPagingSource(
        entityId: String,
        entity: MusicBrainzEntity,
        listFilters: ListFilters,
    ): PagingSource<Int, PlaceListItemModel> {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                areaPlaceDao.getPlacesByArea(
                    areaId = entityId,
                    query = listFilters.query,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                collectionEntityDao.getPlacesByCollection(
                    collectionId = entityId,
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
    ): BrowsePlacesResponse {
        return when (entity) {
            MusicBrainzEntity.AREA -> {
                browseApi.browsePlacesByArea(
                    areaId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                browseApi.browsePlacesByCollection(
                    collectionId = entityId,
                    offset = offset,
                )
            }

            else -> error(browseEntitiesNotSupported(entity))
        }
    }

    override fun insertAllLinkingModels(
        entityId: String,
        entity: MusicBrainzEntity,
        musicBrainzModels: List<PlaceMusicBrainzModel>,
    ) {
        placeDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntity.AREA -> {
                areaPlaceDao.linkAreaWithPlaces(
                    areaId = entityId,
                    musicBrainzModels.map { place -> place.id },
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
}

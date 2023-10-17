package ly.david.musicsearch.data.repository.place

import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.data.musicbrainz.PlaceMusicBrainzModel
import ly.david.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.repository.BrowseEntitiesByEntity
import ly.david.musicsearch.domain.place.PlacesByEntityRepository

class PlacesByEntityRepositoryImpl(
    private val browseEntityCountDao: BrowseEntityCountDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val placeDao: PlaceDao,
    private val areaPlaceDao: AreaPlaceDao,
    private val musicBrainzApi: MusicBrainzApi,
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
                musicBrainzApi.browsePlacesByArea(
                    areaId = entityId,
                    offset = offset,
                )
            }

            MusicBrainzEntity.COLLECTION -> {
                musicBrainzApi.browsePlacesByCollection(
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

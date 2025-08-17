package ly.david.musicsearch.data.repository.place

import app.cash.paging.PagingData
import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseApi
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.base.BrowseEntities
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.place.PlacesListRepository

class PlacesListRepositoryImpl(
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao,
    private val collectionEntityDao: CollectionEntityDao,
    private val placeDao: PlaceDao,
    private val browseApi: BrowseApi,
    aliasDao: AliasDao,
) : PlacesListRepository,
    BrowseEntities<PlaceListItemModel, PlaceMusicBrainzNetworkModel, BrowsePlacesResponse>(
        browseEntity = MusicBrainzEntityType.PLACE,
        browseRemoteMetadataDao = browseRemoteMetadataDao,
        aliasDao = aliasDao,
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
        entity: MusicBrainzEntityType,
    ) {
        browseRemoteMetadataDao.withTransaction {
            browseRemoteMetadataDao.deleteBrowseRemoteCountByEntity(
                entityId = entityId,
                browseEntity = browseEntity,
            )

            when (entity) {
                MusicBrainzEntityType.AREA -> {
                    placeDao.deletePlacesByArea(entityId)
                }

                MusicBrainzEntityType.COLLECTION -> {
                    collectionEntityDao.deleteAllFromCollection(entityId)
                }

                else -> error(browseEntitiesNotSupported(entity))
            }
        }
    }

    override suspend fun browseEntitiesByEntity(
        entityId: String,
        entity: MusicBrainzEntityType,
        offset: Int,
    ): BrowsePlacesResponse {
        return browseApi.browsePlacesByEntity(
            entityId = entityId,
            entity = entity,
            offset = offset,
        )
    }

    override fun insertAll(
        entityId: String,
        entity: MusicBrainzEntityType,
        musicBrainzModels: List<PlaceMusicBrainzNetworkModel>,
    ) {
        placeDao.insertAll(musicBrainzModels)
        when (entity) {
            MusicBrainzEntityType.AREA -> {
                placeDao.insertPlacesByArea(
                    entityId = entityId,
                    placeIds = musicBrainzModels.map { place -> place.id },
                )
            }

            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.addAllToCollection(
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
        entity: MusicBrainzEntityType,
    ): Int {
        return when (entity) {
            MusicBrainzEntityType.COLLECTION -> {
                collectionEntityDao.getCountOfEntitiesByCollection(entityId)
            }

            else -> {
                placeDao.getCountOfPlacesByArea(entityId)
            }
        }
    }
}

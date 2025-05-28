package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.place.PlaceRepositoryImpl
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.place.PlaceRepository

interface TestPlaceRepository {
    val entityHasRelationsDao: EntityHasRelationsDao
    val visitedDao: DetailsMetadataDao
    val relationDao: RelationDao
    val placeDao: PlaceDao
    val areaDao: AreaDao
    val browseRemoteMetadataDao: BrowseRemoteMetadataDao
    val collectionEntityDao: CollectionEntityDao

    fun createPlaceRepository(
        musicBrainzModel: PlaceMusicBrainzModel,
    ): PlaceRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupPlace(
                    placeId: String,
                    include: String?,
                ): PlaceMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            detailsMetadataDao = visitedDao,
            relationDao = relationDao,
        )
        return PlaceRepositoryImpl(
            placeDao = this.placeDao,
            areaDao = areaDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupPlace(
                    placeId: String,
                    include: String?,
                ): PlaceMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }
}

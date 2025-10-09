package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.place.PlaceRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.place.PlaceRepository

interface TestPlaceRepository {
    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val placeDao: PlaceDao
    val areaDao: AreaDao
    val browseRemoteMetadataDao: BrowseRemoteMetadataDao
    val collectionEntityDao: CollectionEntityDao
    val aliasDao: AliasDao
    val coroutineDispatchers: CoroutineDispatchers

    fun createPlaceRepository(
        musicBrainzModel: PlaceMusicBrainzNetworkModel,
    ): PlaceRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupPlace(
                    placeId: String,
                    include: String?,
                ): PlaceMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return PlaceRepositoryImpl(
            placeDao = this.placeDao,
            areaDao = areaDao,
            relationRepository = relationRepository,
            aliasDao = aliasDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupPlace(
                    placeId: String,
                    include: String?,
                ): PlaceMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            coroutineDispatchers = coroutineDispatchers,
        )
    }
}

package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.area.AreaRepositoryImpl
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao

interface TestAreaRepository {
    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val areaDao: AreaDao
    val aliasDao: AliasDao
    val coroutineDispatchers: CoroutineDispatchers

    /**
     * This builds [AreaRepositoryImpl] cast as [AreaRepository].
     * Database interactions are real. Only network and time are faked.
     */
    fun createAreaRepository(
        musicBrainzModel: AreaMusicBrainzNetworkModel,
    ): AreaRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArea(
                    areaId: String,
                    include: String?,
                ): AreaMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return AreaRepositoryImpl(
            areaDao = areaDao,
            relationRepository = relationRepository,
            aliasDao = aliasDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArea(
                    areaId: String,
                    include: String?,
                ): AreaMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            coroutineDispatchers = coroutineDispatchers,
        )
    }
}

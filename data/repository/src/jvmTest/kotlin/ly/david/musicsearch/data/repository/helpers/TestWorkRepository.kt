package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.work.WorkRepositoryImpl
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.work.WorkRepository

interface TestWorkRepository {
    val entityHasRelationsDao: EntityHasRelationsDao
    val visitedDao: DetailsMetadataDao
    val relationDao: RelationDao
    val workDao: WorkDao
    val workAttributeDao: WorkAttributeDao

    fun createWorkRepository(
        musicBrainzModel: WorkMusicBrainzModel,
    ): WorkRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupWork(
                    workId: String,
                    include: String?,
                ): WorkMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            detailsMetadataDao = visitedDao,
            relationDao = relationDao,
        )
        return WorkRepositoryImpl(
            workDao = workDao,
            workAttributeDao = workAttributeDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupWork(
                    workId: String,
                    include: String?,
                ): WorkMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }
}

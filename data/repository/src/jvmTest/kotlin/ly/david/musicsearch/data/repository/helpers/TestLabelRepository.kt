package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.label.LabelRepositoryImpl
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.label.LabelRepository

interface TestLabelRepository {

    val entityHasRelationsDao: EntityHasRelationsDao
    val visitedDao: VisitedDao
    val relationDao: RelationDao
    val labelDao: LabelDao

    fun createLabelRepository(
        musicBrainzModel: LabelMusicBrainzModel,
    ): LabelRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupLabel(
                    labelId: String,
                    include: String,
                ): LabelMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return LabelRepositoryImpl(
            labelDao = labelDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupLabel(
                    labelId: String,
                    include: String,
                ): LabelMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }
}

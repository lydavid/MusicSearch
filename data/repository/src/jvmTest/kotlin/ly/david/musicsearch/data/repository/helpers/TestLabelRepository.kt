package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.label.LabelRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.label.LabelRepository

interface TestLabelRepository {

    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val labelDao: LabelDao
    val aliasDao: AliasDao
    val coroutineDispatchers: CoroutineDispatchers

    fun createLabelRepository(
        musicBrainzModel: LabelMusicBrainzNetworkModel,
    ): LabelRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupLabel(
                    labelId: String,
                    include: String,
                ): LabelMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return LabelRepositoryImpl(
            labelDao = labelDao,
            relationRepository = relationRepository,
            aliasDao = aliasDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupLabel(
                    labelId: String,
                    include: String,
                ): LabelMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            coroutineDispatchers = coroutineDispatchers,
        )
    }
}

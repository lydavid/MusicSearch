package ly.david.musicsearch.data.repository.helpers

import ly.david.data.test.api.FakeLookupApi
import ly.david.data.test.preferences.NoOpMusicBrainzAuthStore
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.relation.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.series.SeriesRepositoryImpl
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.series.SeriesRepository

interface TestSeriesRepository {
    val relationsMetadataDao: RelationsMetadataDao
    val detailsMetadataDao: DetailsMetadataDao
    val relationDao: RelationDao
    val seriesDao: SeriesDao
    val aliasDao: AliasDao
    val tagDao: TagDao
    val coroutineDispatchers: CoroutineDispatchers

    private fun createRelationRepository(
        musicBrainzModel: SeriesMusicBrainzNetworkModel,
    ): RelationRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupSeries(
                    seriesId: String,
                    include: String?,
                ): SeriesMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return relationRepository
    }

    fun createSeriesRepository(
        musicBrainzModel: SeriesMusicBrainzNetworkModel,
    ): SeriesRepository {
        val relationRepository = createRelationRepository(musicBrainzModel)
        return SeriesRepositoryImpl(
            seriesDao = seriesDao,
            relationRepository = relationRepository,
            aliasDao = aliasDao,
            tagDao = tagDao,
            detailsMetadataDao = detailsMetadataDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupSeries(
                    seriesId: String,
                    include: String?,
                ): SeriesMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            coroutineDispatchers = coroutineDispatchers,
            musicBrainzAuthStore = NoOpMusicBrainzAuthStore(),
        )
    }
}

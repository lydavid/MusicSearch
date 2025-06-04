package ly.david.musicsearch.data.repository.series

import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class SeriesRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val visitedDao: DetailsMetadataDao by inject()
    private val relationDao: RelationDao by inject()
    private val seriesDao: SeriesDao by inject()

    private fun createRelationRepository(
        musicBrainzModel: SeriesMusicBrainzNetworkModel
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
            entityHasRelationsDao = entityHasRelationsDao,
            detailsMetadataDao = visitedDao,
            relationDao = relationDao,
        )
        return relationRepository
    }

    private fun createSeriesRepository(
        musicBrainzModel: SeriesMusicBrainzNetworkModel,
    ): SeriesRepository {
        val relationRepository = createRelationRepository(musicBrainzModel)
        return SeriesRepositoryImpl(
            seriesDao = seriesDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupSeries(
                    seriesId: String,
                    include: String?,
                ): SeriesMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createSeriesRepository(
            musicBrainzModel = SeriesMusicBrainzNetworkModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupSeries(
            seriesId = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
            forceRefresh = false,
        )
        assertEquals(
            SeriesDetailsModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createSeriesRepository(
            musicBrainzModel = SeriesMusicBrainzNetworkModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
                type = "Release group series",
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "official homepage",
                        typeId = "b79eb9a5-46df-492d-b107-1f1fea71b0eb",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "e4a5db48-cae3-404f-921d-0f1c3947f874",
                            resource = "https://www.rollingstone.com/music/music-lists/best-albums-of-all-time-1062063/",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikidata",
                        typeId = "a1eecd98-f2f2-420b-ba8e-e5bc61697869",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "61036cd9-8819-4f56-8739-d7f9bd16d675",
                            resource = "https://www.wikidata.org/wiki/Q240550",
                        ),
                    ),
                ),
            ),
        )
        var allDataDetailsModel = allDataRepository.lookupSeries(
            seriesId = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
            forceRefresh = false,
        )
        assertEquals(
            SeriesDetailsModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
            ),
            allDataDetailsModel,
        )
        allDataDetailsModel = allDataRepository.lookupSeries(
            seriesId = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
            forceRefresh = true,
        )
        assertEquals(
            SeriesDetailsModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
                type = "Release group series",
                urls = listOf(
                    RelationListItemModel(
                        id = "61036cd9-8819-4f56-8739-d7f9bd16d675_3",
                        linkedEntityId = "61036cd9-8819-4f56-8739-d7f9bd16d675",
                        label = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q240550",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "e4a5db48-cae3-404f-921d-0f1c3947f874_2",
                        linkedEntityId = "e4a5db48-cae3-404f-921d-0f1c3947f874",
                        label = "official homepages",
                        name = "https://www.rollingstone.com/music/music-lists/best-albums-of-all-time-1062063/",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataDetailsModel,
        )
    }
}

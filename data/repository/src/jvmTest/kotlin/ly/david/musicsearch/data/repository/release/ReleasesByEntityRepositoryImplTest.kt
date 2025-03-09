package ly.david.musicsearch.data.repository.release

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.japanAreaMusicBrainzModel
import ly.david.data.test.mercuryRecordsLabelMusicBrainzModel
import ly.david.data.test.redReleaseListItemModel
import ly.david.data.test.redReleaseMusicBrainzModel
import ly.david.data.test.releaseWith3CatalogNumbersWithSameLabel
import ly.david.data.test.releaseWithSameCatalogNumberWithDifferentLabels
import ly.david.data.test.utaNoUtaReleaseListItemModel
import ly.david.data.test.utaNoUtaReleaseMusicBrainzModel
import ly.david.data.test.virginMusicLabelMusicBrainzModel
import ly.david.data.test.weirdAlGreatestHitsReleaseListItemModel
import ly.david.data.test.weirdAlGreatestHitsReleaseMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.RecordingReleaseDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestAreaRepository
import ly.david.musicsearch.data.repository.helpers.TestLabelRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleasesByEntityRepositoryImplTest :
    KoinTest,
    TestAreaRepository,
    TestLabelRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val database: Database by inject()
    private val artistReleaseDao: ArtistReleaseDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val recordingReleaseDao: RecordingReleaseDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    override val labelDao: LabelDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    override val areaDao: AreaDao by inject()

    private fun createReleasesByEntityRepository(
        releases: List<ReleaseMusicBrainzModel>,
    ): ReleasesByEntityRepository {
        return ReleasesByEntityRepositoryImpl(
            artistReleaseDao = artistReleaseDao,
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseReleasesByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                    include: String,
                ) = BrowseReleasesResponse(
                    count = 1,
                    offset = 0,
                    musicBrainzModels = releases,
                )
            },
            recordingReleaseDao = recordingReleaseDao,
            releaseDao = releaseDao,
            releaseReleaseGroupDao = releaseReleaseGroupDao,
        )
    }

    @Test
    fun `releases by label - release with multiple catalog numbers with the same label, multiple cover arts`() = runTest {
        val labelRepository = createLabelRepository(
            musicBrainzModel = virginMusicLabelMusicBrainzModel,
        )
        val labelId = virginMusicLabelMusicBrainzModel.id
        labelRepository.lookupLabel(
            labelId = labelId,
            forceRefresh = false,
        )

        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = listOf(
                releaseWith3CatalogNumbersWithSameLabel,
            ),
        )

        database.mbid_imageQueries.insert(
            mbid = releaseWith3CatalogNumbersWithSameLabel.id,
            thumbnailUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
            largeUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-1200.jpg",
            types = persistentListOf(),
            comment = null,
        )
        database.mbid_imageQueries.insert(
            mbid = releaseWith3CatalogNumbersWithSameLabel.id,
            thumbnailUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/37564563886-250.jpg",
            largeUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/37564563886-1200.jpg",
            types = persistentListOf(),
            comment = null,
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = releasesByEntityRepository.observeReleasesByEntity(
            entityId = labelId,
            entity = MusicBrainzEntity.LABEL,
            listFilters = ListFilters(
                query = "ウタ",
            ),
        )
        val releases: List<ReleaseListItemModel> = flow.asSnapshot()
        assertEquals(
            listOf(
                utaNoUtaReleaseListItemModel.copy(
                    catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                    textRepresentation = TextRepresentationUiModel(script = "Jpan", language = "jpn"),
                    imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                    imageId = 1,
                ),
            ),
            releases,
        )
    }

    @Test
    fun `releases by label - release with multiple labels and catalog numbers`() = runTest {
        val labelRepository = createLabelRepository(
            musicBrainzModel = mercuryRecordsLabelMusicBrainzModel,
        )
        val labelId = mercuryRecordsLabelMusicBrainzModel.id
        labelRepository.lookupLabel(
            labelId = labelId,
            forceRefresh = false,
        )

        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = listOf(
                releaseWithSameCatalogNumberWithDifferentLabels,
            ),
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = releasesByEntityRepository.observeReleasesByEntity(
            entityId = labelId,
            entity = MusicBrainzEntity.LABEL,
            listFilters = ListFilters(),
        )
        val releases: List<ReleaseListItemModel> = flow.asSnapshot()
        assertEquals(
            listOf(
                redReleaseListItemModel.copy(
                    catalogNumbers = "3717453",
                ),
            ),
            releases,
        )
    }

    @Test
    fun `load 100, then 200 releases`() = runTest {
        val labelRepository = createLabelRepository(
            musicBrainzModel = virginMusicLabelMusicBrainzModel,
        )
        val labelId = virginMusicLabelMusicBrainzModel.id
        labelRepository.lookupLabel(
            labelId = labelId,
            forceRefresh = false,
        )

        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = (1..200).map {
                releaseWith3CatalogNumbersWithSameLabel.copy(
                    id = it.toString(),
                )
            },
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = releasesByEntityRepository.observeReleasesByEntity(
            entityId = labelId,
            entity = MusicBrainzEntity.LABEL,
            listFilters = ListFilters(),
        )
        flow.asSnapshot().let { releases ->
            assertEquals(
                100,
                releases.size,
            )
        }

        flow.asSnapshot {
            scrollTo(99)
        }.let { releases ->
            assertEquals(
                200,
                releases.size,
            )
        }
    }

    @Test
    fun setupReleasesByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val releases = listOf(
            redReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
        )
        val eventsByEntityRepository = createReleasesByEntityRepository(
            releases = releases,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "releases",
                entity = MusicBrainzEntity.RELEASE,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = releases.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                eventsByEntityRepository.observeReleasesByEntity(
                    entityId = collectionId,
                    entity = MusicBrainzEntity.COLLECTION,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        redReleaseListItemModel,
                        utaNoUtaReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "red",
                    expectedResult = listOf(
                        redReleaseListItemModel,
                        utaNoUtaReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "限",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "-22",
                    expectedResult = listOf(
                        redReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by country code",
                    query = "J",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "swift",
                    expectedResult = listOf(
                        redReleaseListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `releases by area`() = runTest {
        val entityId = japanAreaMusicBrainzModel.id
        val areaRepository = createAreaRepository(
            musicBrainzModel = japanAreaMusicBrainzModel,
        )
        areaRepository.lookupArea(
            areaId = entityId,
            forceRefresh = false,
        )

        val entity = MusicBrainzEntity.AREA
        val releases = listOf(
            weirdAlGreatestHitsReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesByEntityRepository.observeReleasesByEntity(
                    entityId = entityId,
                    entity = entity,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            // this only goes up when we have visited the country
                            releaseCountryCount = 1,
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "hits",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "回",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "2022",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by country code",
                    query = "AF",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "ado",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing releases that belong to multiple entities does not delete the release`() = runTest {
        `releases by label - release with multiple catalog numbers with the same label, multiple cover arts`()

        val areaId = japanAreaMusicBrainzModel.id
        val areaRepository = createAreaRepository(
            musicBrainzModel = japanAreaMusicBrainzModel,
        )
        areaRepository.lookupArea(
            areaId = areaId,
            forceRefresh = false,
        )

        val entity = MusicBrainzEntity.AREA
        val releases = listOf(
            weirdAlGreatestHitsReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesByEntityRepository.observeReleasesByEntity(
                    entityId = areaId,
                    entity = entity,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                            imageId = 1,
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        ),
                    ),
                ),
            ),
        )

        // refresh
        val modifiedReleases = listOf(
            utaNoUtaReleaseMusicBrainzModel.copy(
                name = "some change",
            ),
        )
        val modifiedReleasesByEntityRepository = createReleasesByEntityRepository(
            releases = modifiedReleases,
        )
        modifiedReleasesByEntityRepository.observeReleasesByEntity(
            entityId = areaId,
            entity = entity,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        name = "some change",
                        releaseCountryCount = 1,
                        imageId = 1,
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                    ),
                ),
                this,
            )
        }

        // other entities remain unchanged
//        modifiedReleasesByEntityRepository.observeReleasesByEntity(
//            entityId = virginMusicLabelMusicBrainzModel.id,
//            entity = MusicBrainzEntity.LABEL,
//            listFilters = ListFilters(),
//        ).asSnapshot {
//            refresh()
//        }.run {
//            assertEquals(
//                listOf(
//                    utaNoUtaReleaseListItemModel.copy(
//                        imageId = 1,
//                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
//                    ),
//                ),
//                this,
//            )
//        }
    }
}

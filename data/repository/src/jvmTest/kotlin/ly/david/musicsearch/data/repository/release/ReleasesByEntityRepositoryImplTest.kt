package ly.david.musicsearch.data.repository.release

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.davidBowieArtistMusicBrainzModel
import ly.david.data.test.japanAreaMusicBrainzModel
import ly.david.data.test.mercuryRecordsLabelMusicBrainzModel
import ly.david.data.test.redReleaseListItemModel
import ly.david.data.test.redReleaseMusicBrainzModel
import ly.david.data.test.releaseWith3CatalogNumbersWithSameLabel
import ly.david.data.test.releaseWithSameCatalogNumberWithDifferentLabels
import ly.david.data.test.underPressureJapanReleaseListItemModel
import ly.david.data.test.underPressureJapanReleaseMusicBrainzModel
import ly.david.data.test.underPressureRecordingMusicBrainzModel
import ly.david.data.test.underPressureReleaseGroupMusicBrainzModel
import ly.david.data.test.underPressureReleaseListItemModel
import ly.david.data.test.underPressureReleaseMusicBrainzModel
import ly.david.data.test.underPressureRemasteredReleaseListItemModel
import ly.david.data.test.underPressureRemasteredReleaseMusicBrainzModel
import ly.david.data.test.utaNoUtaReleaseListItemModel
import ly.david.data.test.utaNoUtaReleaseMusicBrainzModel
import ly.david.data.test.virginMusicLabelMusicBrainzModel
import ly.david.data.test.weirdAlGreatestHitsReleaseListItemModel
import ly.david.data.test.weirdAlGreatestHitsReleaseMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleasesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestAreaRepository
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.TestLabelRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseGroupRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.CoverArtArchiveUiModel
import ly.david.musicsearch.shared.domain.release.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleasesByEntityRepositoryImplTest :
    KoinTest,
    TestAreaRepository,
    TestArtistRepository,
    TestLabelRepository,
    TestRecordingRepository,
    TestReleaseRepository,
    TestReleaseGroupRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val database: Database by inject()
    override val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    override val releaseDao: ReleaseDao by inject()
    override val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    override val labelDao: LabelDao by inject()
    override val mediumDao: MediumDao by inject()
    override val trackDao: TrackDao by inject()
    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val artistDao: ArtistDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val areaDao: AreaDao by inject()

    private fun createReleasesByEntityRepository(
        releases: List<ReleaseMusicBrainzModel>,
    ): ReleasesByEntityRepository {
        return ReleasesByEntityRepositoryImpl(
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
            releaseDao = releaseDao,
        )
    }

    @Test
    fun `releases by label - release with multiple catalog numbers with the same label, multiple cover arts`() =
        runTest {
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
                browseMethod = BrowseMethod.ByEntity(
                    entityId = labelId,
                    entity = MusicBrainzEntity.LABEL,
                ),
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
            browseMethod = BrowseMethod.ByEntity(
                entityId = labelId,
                entity = MusicBrainzEntity.LABEL,
            ),
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
    fun `load 100, then 200 (prefetch), then 300 after scrolling`() = runTest {
        val labelRepository = createLabelRepository(
            musicBrainzModel = virginMusicLabelMusicBrainzModel,
        )
        val labelId = virginMusicLabelMusicBrainzModel.id
        labelRepository.lookupLabel(
            labelId = labelId,
            forceRefresh = false,
        )

        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = (1..300).map {
                releaseWith3CatalogNumbersWithSameLabel.copy(
                    id = it.toString(),
                )
            },
        )

        val flow: Flow<PagingData<ReleaseListItemModel>> = releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = labelId,
                entity = MusicBrainzEntity.LABEL,
            ),
            listFilters = ListFilters(),
        )
        flow.asSnapshot().let { releases ->
            assertEquals(
                200,
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

        flow.asSnapshot {
            scrollTo(199)
        }.let { releases ->
            assertEquals(
                300,
                releases.size,
            )
        }

        flow.asSnapshot {
            scrollTo(300)
        }.let { releases ->
            assertEquals(
                300,
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
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                    ),
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
    fun setupReleasesByJapan() = runTest {
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
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesByEntityRepository.observeReleasesByEntity(
                    browseMethod = browseMethod,
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
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            // this only goes up when we have visited the country
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
    fun `refreshing releases by area that belong to multiple entities does not delete the release`() = runTest {
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

        val browseMethod = BrowseMethod.ByEntity(
            entityId = areaId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesByEntityRepository.observeReleasesByEntity(
                    browseMethod = browseMethod,
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
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                            imageId = 1,
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        ),
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
            ),
        )

        // refresh
        val modifiedReleases = listOf(
            weirdAlGreatestHitsReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel.copy(
                name = "some change",
            ),
        )
        val modifiedReleasesByEntityRepository = createReleasesByEntityRepository(
            releases = modifiedReleases,
        )
        modifiedReleasesByEntityRepository.observeReleasesByEntity(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                        imageId = 1,
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                    ),
                    weirdAlGreatestHitsReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                ),
                this,
            )
        }

        // other entities remain unchanged
        val oldReleasesByEntityRepository = createReleasesByEntityRepository(
            releases = listOf(
                releaseWith3CatalogNumbersWithSameLabel,
            ),
        )
        oldReleasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entity = MusicBrainzEntity.LABEL,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                        imageId = 1,
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `refreshing releases by label that belong to multiple entities does not delete the release`() = runTest {
        `releases by label - release with multiple catalog numbers with the same label, multiple cover arts`()

        val areaId = japanAreaMusicBrainzModel.id
        val areaRepository = createAreaRepository(
            musicBrainzModel = japanAreaMusicBrainzModel,
        )
        areaRepository.lookupArea(
            areaId = areaId,
            forceRefresh = false,
        )

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
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = areaId,
                        entity = MusicBrainzEntity.AREA,
                    ),
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
                        utaNoUtaReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                            imageId = 1,
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        ),
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            releaseCountryCount = 1,
                        ),
                    ),
                ),
            ),
        )

        // refresh
        val modifiedReleases = listOf(
            releaseWith3CatalogNumbersWithSameLabel.copy(
                name = "some change",
            ),
        )
        val modifiedReleasesByEntityRepository = createReleasesByEntityRepository(
            releases = modifiedReleases,
        )
        modifiedReleasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entity = MusicBrainzEntity.LABEL,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                        imageId = 1,
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                    ),
                ),
                this,
            )
        }

        // other entities remain unchanged
        modifiedReleasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = areaId,
                entity = MusicBrainzEntity.AREA,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                        imageId = 1,
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                    ),
                ),
                this,
            )
        }

        modifiedReleasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entity = MusicBrainzEntity.LABEL,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                        imageId = 1,
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun setupReleasesByDavidBowie() = runTest {
        val entityId = davidBowieArtistMusicBrainzModel.id
        val entity = MusicBrainzEntity.ARTIST
        createArtistRepository(
            davidBowieArtistMusicBrainzModel,
        ).lookupArtistDetails(
            artistId = entityId,
            forceRefresh = false,
        )
        val releases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel,
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesByEntityRepository.observeReleasesByEntity(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entity = entity,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupReleasesByUnderPressureRecording() = runTest {
        val entityId = underPressureRecordingMusicBrainzModel.id
        val entity = MusicBrainzEntity.RECORDING
        createRecordingRepository(
            underPressureRecordingMusicBrainzModel,
        ).lookupRecording(
            recordingId = entityId,
            forceRefresh = false,
        )
        val releases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel,
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesByEntityRepository.observeReleasesByEntity(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entity = entity,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupReleasesByUnderPressureReleaseGroup() = runTest {
        val entityId = underPressureReleaseGroupMusicBrainzModel.id
        val entity = MusicBrainzEntity.RELEASE_GROUP
        createReleaseGroupRepository(
            underPressureReleaseGroupMusicBrainzModel,
        ).lookupReleaseGroup(
            releaseGroupId = entityId,
            forceRefresh = false,
        )
        val releases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel,
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesByEntityRepository.observeReleasesByEntity(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entity = entity,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing releases by artist that belong to multiple entities does not delete the release`() = runTest {
        setupReleasesByDavidBowie()
        setupReleasesByJapan()

        val modifiedReleases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-release-group",
            ),
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if release group is linked to multiple entities",
            ),
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = modifiedReleases,
        )

        // refresh
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = davidBowieArtistMusicBrainzModel.id,
                entity = MusicBrainzEntity.ARTIST,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = japanAreaMusicBrainzModel.id,
                entity = MusicBrainzEntity.AREA,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                    weirdAlGreatestHitsReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                ),
                this,
            )
        }
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    weirdAlGreatestHitsReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                ),
                this,
            )
        }

        // now visit the release and refresh it
        val releaseRepository = createReleaseRepository(
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if release is linked to multiple entities",
                releaseGroup = underPressureReleaseGroupMusicBrainzModel,
            ),
        )
        val expectedReleaseDetails = ReleaseDetailsModel(
            id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
            name = "Under Pressure",
            date = "1991",
            artistCredits = listOf(
                ArtistCreditUiModel(
                    artistId = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
                    name = "Queen",
                    joinPhrase = " & ",
                ),
                ArtistCreditUiModel(
                    artistId = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                    name = "David Bowie",
                    joinPhrase = "",
                ),
            ),
            countryCode = "JP",
            coverArtArchive = CoverArtArchiveUiModel(
                count = 0,
            ),
            quality = "normal",
            status = "Official",
            statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
            textRepresentation = TextRepresentationUiModel(
                script = "Latn",
                language = "eng",
            ),
            releaseGroup = ReleaseGroupForRelease(
                id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
                name = "Under Pressure",
                firstReleaseDate = "1981-10",
                primaryType = "Single",
            ),
            areas = listOf(
                AreaListItemModel(
                    id = "2db42837-c832-3c27-b4a3-08198f75693c",
                    name = "Japan",
                    visited = true,
                    countryCodes = listOf("JP"),
                    date = "1991",
                ),
            ),
        )
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = false,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails,
                releaseDetailsModel,
            )
        }
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = true,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails.copy(
                    disambiguation = "changes will be ignored if release is linked to multiple entities",
                ),
                releaseDetailsModel,
            )
        }
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    weirdAlGreatestHitsReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                        disambiguation = "changes will be ignored if release is linked to multiple entities",
                        visited = true,
                    ),
                    utaNoUtaReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `refreshing releases by recording that belong to multiple entities does not delete the release`() = runTest {
        setupReleasesByDavidBowie()
        setupReleasesByUnderPressureRecording()

        val modifiedReleases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-release-group",
            ),
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if release group is linked to multiple entities",
            ),
        )
        val releasesByEntityRepository = createReleasesByEntityRepository(
            releases = modifiedReleases,
        )

        // refresh
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = underPressureRecordingMusicBrainzModel.id,
                entity = MusicBrainzEntity.RECORDING,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel,
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.ByEntity(
                entityId = davidBowieArtistMusicBrainzModel.id,
                entity = MusicBrainzEntity.ARTIST,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureJapanReleaseListItemModel,
                ),
                this,
            )
        }
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel,
                ),
                this,
            )
        }

        // now visit the release and refresh it
        val releaseRepository = createReleaseRepository(
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if release is linked to multiple entities",
                releaseGroup = underPressureReleaseGroupMusicBrainzModel,
            ),
        )
        val expectedReleaseDetails = ReleaseDetailsModel(
            id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
            name = "Under Pressure",
            date = "1991",
            artistCredits = listOf(
                ArtistCreditUiModel(
                    artistId = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
                    name = "Queen",
                    joinPhrase = " & ",
                ),
                ArtistCreditUiModel(
                    artistId = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                    name = "David Bowie",
                    joinPhrase = "",
                ),
            ),
            countryCode = "JP",
            coverArtArchive = CoverArtArchiveUiModel(
                count = 0,
            ),
            quality = "normal",
            status = "Official",
            statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
            textRepresentation = TextRepresentationUiModel(
                script = "Latn",
                language = "eng",
            ),
            releaseGroup = ReleaseGroupForRelease(
                id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
                name = "Under Pressure",
                firstReleaseDate = "1981-10",
                primaryType = "Single",
            ),
            areas = listOf(
                AreaListItemModel(
                    id = "2db42837-c832-3c27-b4a3-08198f75693c",
                    name = "Japan",
                    visited = false,
                    countryCodes = listOf("JP"),
                    date = "1991",
                ),
            ),
        )
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = false,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails,
                releaseDetailsModel,
            )
        }
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = true,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails.copy(
                    disambiguation = "changes will be ignored if release is linked to multiple entities",
                ),
                releaseDetailsModel,
            )
        }
        releasesByEntityRepository.observeReleasesByEntity(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        disambiguation = "changes will be ignored if release is linked to multiple entities",
                        releaseCountryCount = 1,
                        visited = true,
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `refreshing releases by release group that belong to multiple entities does not delete the release`() =
        runTest {
            setupReleasesByUnderPressureRecording()
            setupReleasesByUnderPressureReleaseGroup()

            val modifiedReleases = listOf(
                underPressureReleaseMusicBrainzModel,
                underPressureRemasteredReleaseMusicBrainzModel.copy(
                    id = "new-id-is-considered-a-different-release-group",
                ),
                underPressureJapanReleaseMusicBrainzModel.copy(
                    disambiguation = "changes will be ignored if release group is linked to multiple entities",
                ),
            )
            val releasesByEntityRepository = createReleasesByEntityRepository(
                releases = modifiedReleases,
            )

            // refresh
            releasesByEntityRepository.observeReleasesByEntity(
                browseMethod = BrowseMethod.ByEntity(
                    entityId = underPressureReleaseGroupMusicBrainzModel.id,
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                ),
                listFilters = ListFilters(),
            ).asSnapshot {
                refresh()
            }.run {
                assertEquals(
                    listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel.copy(
                            id = "new-id-is-considered-a-different-release-group",
                        ),
                        underPressureJapanReleaseListItemModel,
                    ),
                    this,
                )
            }

            // other entities remain unchanged
            releasesByEntityRepository.observeReleasesByEntity(
                browseMethod = BrowseMethod.ByEntity(
                    entityId = underPressureRecordingMusicBrainzModel.id,
                    entity = MusicBrainzEntity.RECORDING,
                ),
                listFilters = ListFilters(),
            ).asSnapshot().run {
                assertEquals(
                    listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureJapanReleaseListItemModel,
                    ),
                    this,
                )
            }
            releasesByEntityRepository.observeReleasesByEntity(
                browseMethod = BrowseMethod.All,
                listFilters = ListFilters(),
            ).asSnapshot().run {
                assertEquals(
                    listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel.copy(
                            id = "new-id-is-considered-a-different-release-group",
                        ),
                        underPressureJapanReleaseListItemModel,
                    ),
                    this,
                )
            }

            // now visit the release and refresh it
            val releaseRepository = createReleaseRepository(
                underPressureJapanReleaseMusicBrainzModel.copy(
                    disambiguation = "changes will be ignored if release is linked to multiple entities",
                    releaseGroup = underPressureReleaseGroupMusicBrainzModel,
                ),
            )
            val expectedReleaseDetails = ReleaseDetailsModel(
                id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
                name = "Under Pressure",
                date = "1991",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "0383dadf-2a4e-4d10-a46a-e9e041da8eb3",
                        name = "Queen",
                        joinPhrase = " & ",
                    ),
                    ArtistCreditUiModel(
                        artistId = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                        name = "David Bowie",
                        joinPhrase = "",
                    ),
                ),
                countryCode = "JP",
                coverArtArchive = CoverArtArchiveUiModel(
                    count = 0,
                ),
                quality = "normal",
                status = "Official",
                statusId = "4e304316-386d-3409-af2e-78857eec5cfe",
                textRepresentation = TextRepresentationUiModel(
                    script = "Latn",
                    language = "eng",
                ),
                releaseGroup = ReleaseGroupForRelease(
                    id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
                    name = "Under Pressure",
                    firstReleaseDate = "1981-10",
                    primaryType = "Single",
                ),
                areas = listOf(
                    AreaListItemModel(
                        id = "2db42837-c832-3c27-b4a3-08198f75693c",
                        name = "Japan",
                        visited = false,
                        countryCodes = listOf("JP"),
                        date = "1991",
                    ),
                ),
            )
            releaseRepository.lookupRelease(
                releaseId = underPressureJapanReleaseMusicBrainzModel.id,
                forceRefresh = false,
            ).let { releaseDetailsModel ->
                assertEquals(
                    expectedReleaseDetails,
                    releaseDetailsModel,
                )
            }
            releaseRepository.lookupRelease(
                releaseId = underPressureJapanReleaseMusicBrainzModel.id,
                forceRefresh = true,
            ).let { releaseDetailsModel ->
                assertEquals(
                    expectedReleaseDetails.copy(
                        disambiguation = "changes will be ignored if release is linked to multiple entities",
                    ),
                    releaseDetailsModel,
                )
            }
            releasesByEntityRepository.observeReleasesByEntity(
                browseMethod = BrowseMethod.All,
                listFilters = ListFilters(),
            ).asSnapshot().run {
                assertEquals(
                    listOf(
                        underPressureReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel,
                        underPressureRemasteredReleaseListItemModel.copy(
                            id = "new-id-is-considered-a-different-release-group",
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            disambiguation = "changes will be ignored if release is linked to multiple entities",
                            releaseCountryCount = 1,
                            visited = true,
                        ),
                    ),
                    this,
                )
            }
        }
}

package ly.david.musicsearch.data.repository.release

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
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
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestAreaRepository
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.TestLabelRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseGroupRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseRepository
import ly.david.musicsearch.data.repository.helpers.TestReleasesListRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.LastUpdatedFooter
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.release.ReleaseStatus
import ly.david.musicsearch.shared.domain.release.TextRepresentationUiModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupForRelease
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

@Suppress("LargeClass")
class ReleasesListRepositoryImplTest :
    KoinTest,
    TestAreaRepository,
    TestArtistRepository,
    TestLabelRepository,
    TestRecordingRepository,
    TestReleaseRepository,
    TestReleasesListRepository,
    TestReleaseGroupRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val database: Database by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionDao: CollectionDao by inject()
    override val collectionEntityDao: CollectionEntityDao by inject()
    override val releaseDao: ReleaseDao by inject()
    override val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    override val labelDao: LabelDao by inject()
    override val mediumDao: MediumDao by inject()
    override val trackDao: TrackDao by inject()
    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val artistDao: ArtistDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val areaDao: AreaDao by inject()
    override val aliasDao: AliasDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()

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
                lastUpdated = testDateTimeInThePast,
            )

            val releasesListRepository = createReleasesListRepository(
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

            releasesListRepository.observeReleases(
                browseMethod = BrowseMethod.ByEntity(
                    entityId = labelId,
                    entity = MusicBrainzEntityType.LABEL,
                ),
                listFilters = ListFilters(
                    query = "ウタ",
                ),
                now = testDateTimeInThePast,
            ).asSnapshot().run {
                assertEquals(
                    listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                            textRepresentation = TextRepresentationUiModel(script = "Jpan", language = "jpn"),
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                            imageId = ImageId(1L),
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                    this,
                )
            }
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
            lastUpdated = testDateTimeInThePast,
        )

        val releasesListRepository = createReleasesListRepository(
            releases = listOf(
                releaseWithSameCatalogNumberWithDifferentLabels,
            ),
        )

        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = labelId,
                entity = MusicBrainzEntityType.LABEL,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    redReleaseListItemModel.copy(
                        catalogNumbers = "3717453",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
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
            lastUpdated = testDateTimeInThePast,
        )

        val releasesListRepository = createReleasesListRepository(
            releases = (1..300).map {
                releaseWith3CatalogNumbersWithSameLabel.copy(
                    id = it.toString(),
                )
            },
        )

        val flow: Flow<PagingData<ListItemModel>> = releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = labelId,
                entity = MusicBrainzEntityType.LABEL,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
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
                301,
                releases.size,
            )
        }

        flow.asSnapshot {
            scrollTo(300)
        }.let { releases ->
            assertEquals(
                301,
                releases.size,
            )
        }
    }

    @Test
    fun releasesByLocalCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val releases = listOf(
            redReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases = emptyList(),
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "releases",
                entity = MusicBrainzEntityType.RELEASE,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = releases.map { it.id },
        )
        releaseDao.insertAll(releases)

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntityType.COLLECTION,
                    ),
                    listFilters = ListFilters(
                        query = query,
                        isRemote = false,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "red",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "限",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "-22",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by country code",
                    query = "J",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "swift",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupReleasesByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val releases = listOf(
            redReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases = releases,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = true,
                name = "releases",
                entity = MusicBrainzEntityType.RELEASE,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = releases.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntityType.COLLECTION,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "red",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "限",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "-22",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by country code",
                    query = "J",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "swift",
                    expectedResult = listOf(
                        redReleaseListItemModel.copy(
                            collected = true,
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
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
            lastUpdated = testDateTimeInThePast,
        )

        val entity = MusicBrainzEntityType.AREA
        val releases = listOf(
            weirdAlGreatestHitsReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "hits",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "回",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "2022",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by country code",
                    query = "AF",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "ado",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing releases does not delete the release`() = runTest {
        `releases by label - release with multiple catalog numbers with the same label, multiple cover arts`()

        val areaId = japanAreaMusicBrainzModel.id
        val areaRepository = createAreaRepository(
            musicBrainzModel = japanAreaMusicBrainzModel,
        )
        areaRepository.lookupArea(
            areaId = areaId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )

        val entity = MusicBrainzEntityType.AREA
        val releases = listOf(
            weirdAlGreatestHitsReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases,
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = areaId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            imageId = ImageId(1L),
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
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
        val modifiedreleasesListRepository = createReleasesListRepository(
            releases = modifiedReleases,
        )
        modifiedreleasesListRepository.observeReleases(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    weirdAlGreatestHitsReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    utaNoUtaReleaseListItemModel.copy(
                        imageId = ImageId(1L),
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }

        // other entities remain unchanged
        val oldReleasesListRepository = createReleasesListRepository(
            releases = listOf(
                releaseWith3CatalogNumbersWithSameLabel,
            ),
        )
        oldReleasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entity = MusicBrainzEntityType.LABEL,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        imageId = ImageId(1L),
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
    }

    @Test
    fun `refreshing releases by label does not delete the release`() = runTest {
        `releases by label - release with multiple catalog numbers with the same label, multiple cover arts`()

        val areaId = japanAreaMusicBrainzModel.id
        val areaRepository = createAreaRepository(
            musicBrainzModel = japanAreaMusicBrainzModel,
        )
        areaRepository.lookupArea(
            areaId = areaId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )

        val releases = listOf(
            weirdAlGreatestHitsReleaseMusicBrainzModel,
            utaNoUtaReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = areaId,
                        entity = MusicBrainzEntityType.AREA,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        utaNoUtaReleaseListItemModel.copy(
                            imageId = ImageId(1L),
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
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
        val modifiedreleasesListRepository = createReleasesListRepository(
            releases = modifiedReleases,
        )
        modifiedreleasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entity = MusicBrainzEntityType.LABEL,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        imageId = ImageId(1L),
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }

        // other entities remain unchanged
        modifiedreleasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = areaId,
                entity = MusicBrainzEntityType.AREA,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        imageId = ImageId(1L),
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }

        modifiedreleasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entity = MusicBrainzEntityType.LABEL,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel.copy(
                        imageId = ImageId(1L),
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                        catalogNumbers = "TYBX-10260, TYCT-69245, TYCX-60187",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
    }

    @Test
    fun setupReleasesByDavidBowie() = runTest {
        val entityId = davidBowieArtistMusicBrainzModel.id
        val entity = MusicBrainzEntityType.ARTIST
        createArtistRepository(
            davidBowieArtistMusicBrainzModel,
        ).lookupArtist(
            artistId = entityId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        val releases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel,
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases = releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entity = entity,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupReleasesByUnderPressureRecording() = runTest {
        val entityId = underPressureRecordingMusicBrainzModel.id
        val entity = MusicBrainzEntityType.RECORDING
        createRecordingRepository(
            underPressureRecordingMusicBrainzModel,
        ).lookupRecording(
            recordingId = entityId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        val releases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel,
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases = releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entity = entity,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupReleasesByUnderPressureReleaseGroup() = runTest {
        val entityId = underPressureReleaseGroupMusicBrainzModel.id
        val entity = MusicBrainzEntityType.RELEASE_GROUP
        createReleaseGroupRepository(
            underPressureReleaseGroupMusicBrainzModel,
        ).lookupReleaseGroup(
            releaseGroupId = entityId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        val releases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel,
            underPressureJapanReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases = releases,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releasesListRepository.observeReleases(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entity = entity,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                    now = testDateTimeInThePast,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureRemasteredReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        underPressureJapanReleaseListItemModel.copy(
                            lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                        ),
                        LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing releases by artist does not delete the release`() = runTest {
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
        val releasesListRepository = createReleasesListRepository(
            releases = modifiedReleases,
        )

        // refresh
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = davidBowieArtistMusicBrainzModel.id,
                entity = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = japanAreaMusicBrainzModel.id,
                entity = MusicBrainzEntityType.AREA,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    weirdAlGreatestHitsReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    utaNoUtaReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(
                sorted = true,
            ),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    weirdAlGreatestHitsReleaseListItemModel,
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel,
                    utaNoUtaReleaseListItemModel,
                ),
                this,
            )
        }
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(
                sorted = false,
            ),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel,
                    underPressureJapanReleaseListItemModel,
                    underPressureReleaseListItemModel,
                    weirdAlGreatestHitsReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                ),
                this,
            )
        }

        // now visit the release and refresh it
        val releaseRepository = createReleaseRepository(
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will show up when visiting",
                releaseGroup = underPressureReleaseGroupMusicBrainzModel,
            ),
        )
        val expectedReleaseDetails = ReleaseDetailsModel(
            id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
            name = "Under Pressure",
            date = "1991",
            disambiguation = "changes will show up when visiting",
            artistCredits = persistentListOf(
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
            quality = "normal",
            status = ReleaseStatus.OFFICIAL,
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
                    countryCodes = persistentListOf("JP"),
                    date = "1991",
                ),
            ),
            lastUpdated = testDateTimeInThePast,
        )
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails,
                releaseDetailsModel,
            )
        }
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails.copy(
                    disambiguation = "changes will show up when visiting",
                ),
                releaseDetailsModel,
            )
        }
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    utaNoUtaReleaseListItemModel,
                    underPressureJapanReleaseListItemModel.copy(
                        releaseCountryCount = 1,
                        disambiguation = "changes will show up when visiting",
                        visited = true,
                    ),
                    underPressureReleaseListItemModel,
                    weirdAlGreatestHitsReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `refreshing releases by recording does not delete the release`() = runTest {
        setupReleasesByDavidBowie()
        setupReleasesByUnderPressureRecording()

        val modifiedReleases = listOf(
            underPressureReleaseMusicBrainzModel,
            underPressureRemasteredReleaseMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-release-group",
            ),
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if release is linked to multiple entities",
            ),
        )
        val releasesListRepository = createReleasesListRepository(
            releases = modifiedReleases,
        )

        // refresh
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = underPressureRecordingMusicBrainzModel.id,
                entity = MusicBrainzEntityType.RECORDING,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = davidBowieArtistMusicBrainzModel.id,
                entity = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureRemasteredReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureJapanReleaseListItemModel,
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                ),
                this,
            )
        }

        // now visit the release and refresh it
        val releaseRepository = createReleaseRepository(
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will show up when visiting",
                releaseGroup = underPressureReleaseGroupMusicBrainzModel,
            ),
        )
        val expectedReleaseDetails = ReleaseDetailsModel(
            id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
            name = "Under Pressure",
            date = "1991",
            disambiguation = "changes will show up when visiting",
            artistCredits = persistentListOf(
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
            quality = "normal",
            status = ReleaseStatus.OFFICIAL,
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
                    countryCodes = persistentListOf("JP"),
                    date = "1991",
                ),
            ),
            lastUpdated = testDateTimeInThePast,
        )
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails,
                releaseDetailsModel,
            )
        }
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails.copy(
                    disambiguation = "changes will show up when visiting",
                ),
                releaseDetailsModel,
            )
        }
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureJapanReleaseListItemModel.copy(
                        disambiguation = "changes will show up when visiting",
                        releaseCountryCount = 1,
                        visited = true,
                    ),
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `refreshing releases by release group does not delete the release`() = runTest {
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
        val releasesListRepository = createReleasesListRepository(
            releases = modifiedReleases,
        )

        // refresh
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = underPressureReleaseGroupMusicBrainzModel.id,
                entity = MusicBrainzEntityType.RELEASE_GROUP,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = underPressureRecordingMusicBrainzModel.id,
                entity = MusicBrainzEntityType.RECORDING,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureRemasteredReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    underPressureJapanReleaseListItemModel.copy(
                        lastUpdated = testDateTimeInThePast.toEpochMilliseconds(),
                    ),
                    LastUpdatedFooter(lastUpdated = testDateTimeInThePast),
                ),
                this,
            )
        }
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureJapanReleaseListItemModel,
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                ),
                this,
            )
        }

        // now visit the release and refresh it
        val releaseRepository = createReleaseRepository(
            underPressureJapanReleaseMusicBrainzModel.copy(
                disambiguation = "changes will show up if we have not visited it yet",
                releaseGroup = underPressureReleaseGroupMusicBrainzModel,
            ),
        )
        val expectedReleaseDetails = ReleaseDetailsModel(
            id = "3e8fe20d-8d8b-454d-9350-2078007d4788",
            name = "Under Pressure",
            date = "1991",
            disambiguation = "changes will show up if we have not visited it yet",
            artistCredits = persistentListOf(
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
            quality = "normal",
            status = ReleaseStatus.OFFICIAL,
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
                    countryCodes = persistentListOf("JP"),
                    date = "1991",
                ),
            ),
            lastUpdated = testDateTimeInThePast,
        )
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails,
                releaseDetailsModel,
            )
        }
        releaseRepository.lookupRelease(
            releaseId = underPressureJapanReleaseMusicBrainzModel.id,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        ).let { releaseDetailsModel ->
            assertEquals(
                expectedReleaseDetails.copy(
                    disambiguation = "changes will show up if we have not visited it yet",
                ),
                releaseDetailsModel,
            )
        }
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureJapanReleaseListItemModel.copy(
                        disambiguation = "changes will show up if we have not visited it yet",
                        releaseCountryCount = 1,
                        visited = true,
                    ),
                    underPressureReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel,
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                ),
                this,
            )
        }
    }
}

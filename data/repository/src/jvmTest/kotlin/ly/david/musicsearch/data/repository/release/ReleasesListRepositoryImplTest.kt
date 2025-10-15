package ly.david.musicsearch.data.repository.release

import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.davidBowieArtistMusicBrainzModel
import ly.david.data.test.japanAreaMusicBrainzModel
import ly.david.data.test.mercuryRecordsLabelMusicBrainzModel
import ly.david.data.test.persona3ReloadOriginalSoundtrackReleaseListItemModel
import ly.david.data.test.persona3ReloadOriginalSoundtrackReleaseMusicBrainzModel
import ly.david.data.test.persona3ReloadSoundtrackAigisReleaseListItemModel
import ly.david.data.test.persona3ReloadSoundtrackAigisReleaseMusicBrainzModel
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
import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.AliasMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TEST_USERNAME
import ly.david.musicsearch.data.repository.helpers.TestAreaRepository
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.TestLabelRepository
import ly.david.musicsearch.data.repository.helpers.TestListensListRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseGroupRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseRepository
import ly.david.musicsearch.data.repository.helpers.TestReleasesListRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.data.repository.helpers.testListens
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
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
    TestReleaseGroupRepository,
    TestListensListRepository {

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
    override val listenDao: ListenDao by inject()

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
                    entityType = MusicBrainzEntityType.LABEL,
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
                        ),
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
                entityType = MusicBrainzEntityType.LABEL,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    redReleaseListItemModel.copy(
                        catalogNumbers = "3717453",
                    ),
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

        val flow = releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = labelId,
                entityType = MusicBrainzEntityType.LABEL,
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
                        entityType = MusicBrainzEntityType.COLLECTION,
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
                        entityType = MusicBrainzEntityType.COLLECTION,
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
            entityType = entity,
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
                        weirdAlGreatestHitsReleaseListItemModel.copy(),
                        utaNoUtaReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "hits",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "回",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "2022",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by country code",
                    query = "AF",
                    expectedResult = listOf(
                        weirdAlGreatestHitsReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "ado",
                    expectedResult = listOf(
                        utaNoUtaReleaseListItemModel.copy(),
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
            entityType = entity,
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
                        weirdAlGreatestHitsReleaseListItemModel.copy(),
                        utaNoUtaReleaseListItemModel.copy(
                            imageId = ImageId(1L),
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
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
                    weirdAlGreatestHitsReleaseListItemModel.copy(),
                    utaNoUtaReleaseListItemModel.copy(
                        imageId = ImageId(1L),
                        imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
                    ),
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
                entityType = MusicBrainzEntityType.LABEL,
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
                    ),
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
                        entityType = MusicBrainzEntityType.AREA,
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
                        weirdAlGreatestHitsReleaseListItemModel.copy(),
                        utaNoUtaReleaseListItemModel.copy(
                            imageId = ImageId(1L),
                            imageUrl = "http://coverartarchive.org/release/38650e8c-3c6b-431e-b10b-2cfb6db847d5/33345773281-250.jpg",
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
        val modifiedreleasesListRepository = createReleasesListRepository(
            releases = modifiedReleases,
        )
        modifiedreleasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.LABEL,
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
                    ),
                ),
                this,
            )
        }

        // other entities remain unchanged
        modifiedreleasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = areaId,
                entityType = MusicBrainzEntityType.AREA,
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
                    ),
                ),
                this,
            )
        }

        modifiedreleasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = virginMusicLabelMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.LABEL,
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
                    ),
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
                        entityType = entity,
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
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
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
                        entityType = entity,
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
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
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
                        entityType = entity,
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
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "under",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "1981",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by date",
                    query = "GB",
                    expectedResult = listOf(
                        underPressureRemasteredReleaseListItemModel.copy(),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "Queen",
                    expectedResult = listOf(
                        underPressureReleaseListItemModel.copy(),
                        underPressureRemasteredReleaseListItemModel.copy(),
                        underPressureJapanReleaseListItemModel.copy(),
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
                entityType = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(),
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel.copy(),
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = japanAreaMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.AREA,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    weirdAlGreatestHitsReleaseListItemModel.copy(),
                    utaNoUtaReleaseListItemModel.copy(),
                    underPressureJapanReleaseListItemModel.copy(),
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
            listenBrainzUrl = "/album/bdaeec2d-94f1-46b5-91f3-340ec6939c66",
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
                entityType = MusicBrainzEntityType.RECORDING,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(),
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel.copy(),
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = davidBowieArtistMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(),
                    underPressureRemasteredReleaseListItemModel.copy(),
                    underPressureJapanReleaseListItemModel.copy(),
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
            listenBrainzUrl = "/album/bdaeec2d-94f1-46b5-91f3-340ec6939c66",
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
                entityType = MusicBrainzEntityType.RELEASE_GROUP,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(),
                    underPressureRemasteredReleaseListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    underPressureJapanReleaseListItemModel.copy(),
                ),
                this,
            )
        }

        // other entities remain unchanged
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = underPressureRecordingMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.RECORDING,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureReleaseListItemModel.copy(),
                    underPressureRemasteredReleaseListItemModel.copy(),
                    underPressureJapanReleaseListItemModel.copy(),
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
            listenBrainzUrl = "/album/bdaeec2d-94f1-46b5-91f3-340ec6939c66",
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

    @Test
    fun `releases with listen count`() = runTest {
        // Add listens
        createListensListRepository(testListens).observeListens(
            username = TEST_USERNAME,
            query = "",
            entityFacet = null,
            stopPrepending = false,
            stopAppending = false,
            onReachedLatest = {},
            onReachedOldest = {},
        ).asSnapshot()

        // Load releases
        val entityId = "37e85ee8-366a-4f17-a011-de94b6632408"
        val entityType = MusicBrainzEntityType.ARTIST
        createArtistRepository(
            ArtistMusicBrainzNetworkModel(
                id = entityId,
                name = "アトラスサウンドチーム",
                disambiguation = "",
                aliases = listOf(
                    AliasMusicBrainzNetworkModel(
                        name = "ATLUS Sound Team",
                        isPrimary = true,
                        locale = "en",
                    ),
                ),
            ),
        ).lookupArtist(
            artistId = entityId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        val releases = listOf(
            persona3ReloadSoundtrackAigisReleaseMusicBrainzModel,
            persona3ReloadOriginalSoundtrackReleaseMusicBrainzModel,
        )
        val releasesListRepository = createReleasesListRepository(
            releases = releases,
            fakeBrowseUsername = TEST_USERNAME,
        )
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = entityId,
                entityType = entityType,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    persona3ReloadSoundtrackAigisReleaseListItemModel.copy(
                        name = "Persona 3 Reload Original Soundtrack", // stubbed values from listens
                        disambiguation = "", // won't be overwritten
                        date = "", // until we visit this release
                        imageId = ImageId(2),
                        imageUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                        listenState = ReleaseListItemModel.ListenState.Unknown,
                    ),
                    persona3ReloadOriginalSoundtrackReleaseListItemModel.copy(
                        listenState = ReleaseListItemModel.ListenState.Unknown,
                    ),
                ),
                this,
            )
        }

        // Visit a release
        createReleaseRepository(
            persona3ReloadSoundtrackAigisReleaseMusicBrainzModel.copy(
                releaseGroup = ReleaseGroupMusicBrainzNetworkModel(
                    id = "ff493297-2f1e-4dc0-9307-cfb8d15602e1",
                    name = "ペルソナ3 リロード オリジナル・サウンドトラック",
                    firstReleaseDate = "2024-02-02",
                ),
                media = listOf(
                    MediumMusicBrainzModel(
                        trackCount = 32,
                        position = 1,
                        format = "CD",
                        tracks = listOf(
                            TrackMusicBrainzModel(
                                position = 1,
                                number = "1",
                                id = "22c930ce-9381-434a-b2e4-5d1afb28b4d8",
                                name = "Full Moon Full Life",
                                length = 293592,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        artist = ArtistMusicBrainzNetworkModel(
                                            id = "37e85ee8-366a-4f17-a011-de94b6632408",
                                        ),
                                        name = "アトラスサウンドチーム",
                                        joinPhrase = "",
                                    ),
                                ),
                                recording = RecordingMusicBrainzNetworkModel(
                                    id = "c4090c59-be0c-4a79-b76d-5e2669e0cd4c",
                                    name = "Full Moon Full Life",
                                    artistCredits = listOf(
                                        ArtistCreditMusicBrainzModel(
                                            artist = ArtistMusicBrainzNetworkModel(
                                                id = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                            ),
                                            name = "高橋あず美",
                                            joinPhrase = " & ",
                                        ),
                                        ArtistCreditMusicBrainzModel(
                                            artist = ArtistMusicBrainzNetworkModel(
                                                id = "c731e592-2620-4f4c-859d-39e294b06b35",
                                            ),
                                            name = "Lotus Juice",
                                            joinPhrase = "",
                                        ),
                                    ),
                                ),
                            ),
                            TrackMusicBrainzModel(
                                position = 16,
                                number = "16",
                                id = "54c8d7b0-8b04-4b60-b5bf-a77060035e29",
                                name = "Color Your Night",
                                length = 227339,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        artist = ArtistMusicBrainzNetworkModel(
                                            id = "37e85ee8-366a-4f17-a011-de94b6632408",
                                        ),
                                        name = "アトラスサウンドチーム",
                                        joinPhrase = "",
                                    ),
                                ),
                                recording = RecordingMusicBrainzNetworkModel(
                                    id = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                                    name = "Color Your Night",
                                    artistCredits = listOf(
                                        ArtistCreditMusicBrainzModel(
                                            artist = ArtistMusicBrainzNetworkModel(
                                                id = "c731e592-2620-4f4c-859d-39e294b06b35",
                                            ),
                                            name = "Lotus Juice",
                                            joinPhrase = " & ",
                                        ),
                                        ArtistCreditMusicBrainzModel(
                                            artist = ArtistMusicBrainzNetworkModel(
                                                id = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                            ),
                                            name = "高橋あず美",
                                            joinPhrase = "",
                                        ),
                                    ),
                                ),
                            ),
                            TrackMusicBrainzModel(
                                position = 30,
                                number = "30",
                                id = "f867b77a-c017-4865-9942-66a46051c06f",
                                name = "It's Going Down Now",
                                length = 186166,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        artist = ArtistMusicBrainzNetworkModel(
                                            id = "37e85ee8-366a-4f17-a011-de94b6632408",
                                        ),
                                        name = "アトラスサウンドチーム",
                                        joinPhrase = "",
                                    ),
                                ),
                                recording = RecordingMusicBrainzNetworkModel(
                                    id = "ca8578d9-7db9-477e-82f2-74cbbf91ef27",
                                    name = "It's Going Down Now",
                                    artistCredits = listOf(
                                        ArtistCreditMusicBrainzModel(
                                            artist = ArtistMusicBrainzNetworkModel(
                                                id = "c731e592-2620-4f4c-859d-39e294b06b35",
                                            ),
                                            name = "Lotus Juice",
                                            joinPhrase = " & ",
                                        ),
                                        ArtistCreditMusicBrainzModel(
                                            artist = ArtistMusicBrainzNetworkModel(
                                                id = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                            ),
                                            name = "高橋あず美",
                                            joinPhrase = "",
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                    MediumMusicBrainzModel(
                        trackCount = 28,
                        position = 2,
                        format = "CD",
                        tracks = listOf(
                            TrackMusicBrainzModel(
                                position = 1,
                                number = "1",
                                id = "67cece38-d01d-4031-9f9e-442944080e3f",
                                name = "Changing Seasons -Reload-",
                                length = 194680,
                                artistCredits = listOf(
                                    ArtistCreditMusicBrainzModel(
                                        artist = ArtistMusicBrainzNetworkModel(
                                            id = "37e85ee8-366a-4f17-a011-de94b6632408",
                                        ),
                                        name = "アトラスサウンドチーム",
                                        joinPhrase = "",
                                    ),
                                ),
                                recording = RecordingMusicBrainzNetworkModel(
                                    id = "82a814df-e795-4e6e-8626-8eb49c2949f4",
                                    name = "Changing Seasons -Reload-",
                                    artistCredits = listOf(
                                        ArtistCreditMusicBrainzModel(
                                            artist = ArtistMusicBrainzNetworkModel(
                                                id = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                            ),
                                            name = "高橋あず美",
                                            joinPhrase = "",
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        ).lookupRelease(
            releaseId = persona3ReloadSoundtrackAigisReleaseMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).let { releaseDetailsModel ->
            assertEquals(
                ReleaseDetailsModel(
                    id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                    name = "Persona 3 Reload Soundtrack",
                    date = "2024-02-02",
                    disambiguation = "Aigis edition",
                    artistCredits = persistentListOf(
                        ArtistCreditUiModel(
                            artistId = "37e85ee8-366a-4f17-a011-de94b6632408",
                            name = "アトラスサウンドチーム",
                            joinPhrase = "",
                        ),
                    ),
                    releaseGroup = ReleaseGroupForRelease(
                        id = "ff493297-2f1e-4dc0-9307-cfb8d15602e1",
                        name = "ペルソナ3 リロード オリジナル・サウンドトラック",
                        firstReleaseDate = "2024-02-02",
                    ),
                    releaseLength = 901777,
                    hasNullLength = false,
                    formattedTracks = "3 + 1",
                    formattedFormats = "2×CD",
                    listenBrainzUrl = "/album/ff493297-2f1e-4dc0-9307-cfb8d15602e1",
                    lastUpdated = testDateTimeInThePast,
                ),
                releaseDetailsModel,
            )
        }

        // browse releases again, and now there is a listen count for the one we visited
        // and its data has been corrected
        releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.ByEntity(
                entityId = entityId,
                entityType = entityType,
            ),
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    persona3ReloadSoundtrackAigisReleaseListItemModel.copy(
                        imageId = ImageId(2),
                        imageUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                        listenState = ReleaseListItemModel.ListenState.Known(
                            listenCount = 2,
                            completeListenCount = 0,
                        ),
                        visited = true,
                    ),
                    persona3ReloadOriginalSoundtrackReleaseListItemModel.copy(
                        listenState = ReleaseListItemModel.ListenState.Unknown,
                    ),
                ),
                this,
            )
        }
    }
}

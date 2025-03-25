package ly.david.musicsearch.data.repository.releasegroup

import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.alsoSprachZarathustraReleaseGroupListItemModel
import ly.david.data.test.alsoSprachZarathustraReleaseGroupMusicBrainzModel
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.berlinerPhilharmonikerArtistMusicBrainzModel
import ly.david.data.test.nutcrackerReleaseGroupListItemModel
import ly.david.data.test.nutcrackerReleaseGroupMusicBrainzModel
import ly.david.data.test.tchaikovskyArtistMusicBrainzModel
import ly.david.data.test.tchaikovskyOverturesReleaseGroupListItemModel
import ly.david.data.test.tchaikovskyOverturesReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.ArtistReleaseGroupDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseGroupRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsByEntityRepository
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ReleaseGroupsByEntityRepositoryImplTest :
    KoinTest,
    TestArtistRepository,
    TestReleaseGroupRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    override val browseEntityCountDao: BrowseEntityCountDao by inject()
    override val artistDao: ArtistDao by inject()
    override val areaDao: AreaDao by inject()
    private val artistReleaseGroupDao: ArtistReleaseGroupDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"

    private fun createReleaseGroupsByEntityRepository(
        releaseGroups: List<ReleaseGroupMusicBrainzModel>,
    ): ReleaseGroupsByEntityRepository {
        return ReleaseGroupsByEntityRepositoryImpl(
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            releaseGroupDao = releaseGroupDao,
            artistReleaseGroupDao = artistReleaseGroupDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseReleaseGroupsByArtist(
                    artistId: String,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseReleaseGroupsResponse {
                    return BrowseReleaseGroupsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = releaseGroups,
                    )
                }

                override suspend fun browseReleaseGroupsByCollection(
                    collectionId: String,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseReleaseGroupsResponse {
                    return BrowseReleaseGroupsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = releaseGroups,
                    )
                }
            },
        )
    }

    @Test
    fun setupReleaseGroupsByCollection() = runTest {
        val releaseGroups = listOf(
            alsoSprachZarathustraReleaseGroupMusicBrainzModel,
            tchaikovskyOverturesReleaseGroupMusicBrainzModel,
        )
        val releaseGroupsByEntityRepository = createReleaseGroupsByEntityRepository(
            releaseGroups = releaseGroups,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "release groups",
                entity = MusicBrainzEntity.RELEASE_GROUP,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = releaseGroups.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
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
                        alsoSprachZarathustraReleaseGroupListItemModel,
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "1812",
                    expectedResult = listOf(
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "album",
                    expectedResult = listOf(
                        alsoSprachZarathustraReleaseGroupListItemModel,
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "tchai",
                    expectedResult = listOf(
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupReleaseGroupsByTchaikovsky() = runTest {
        val entityId = tchaikovskyArtistMusicBrainzModel.id
        val entity = MusicBrainzEntity.ARTIST
        createArtistRepository(
            tchaikovskyArtistMusicBrainzModel,
        ).lookupArtistDetails(
            artistId = entityId,
            forceRefresh = false,
        )
        val releaseGroups = listOf(
            nutcrackerReleaseGroupMusicBrainzModel,
            tchaikovskyOverturesReleaseGroupMusicBrainzModel,
        )
        val releaseGroupsByEntityRepository = createReleaseGroupsByEntityRepository(
            releaseGroups = releaseGroups,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
                    entityId = entityId,
                    entity = entity,
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
                        nutcrackerReleaseGroupListItemModel,
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "1812",
                    expectedResult = listOf(
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "album",
                    expectedResult = listOf(
                        nutcrackerReleaseGroupListItemModel,
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "rattle",
                    expectedResult = listOf(
                        nutcrackerReleaseGroupListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupReleaseGroupsByBerlinerPhilharmoniker() = runTest {
        val entityId = berlinerPhilharmonikerArtistMusicBrainzModel.id
        val entity = MusicBrainzEntity.ARTIST
        createArtistRepository(
            berlinerPhilharmonikerArtistMusicBrainzModel,
        ).lookupArtistDetails(
            artistId = entityId,
            forceRefresh = false,
        )
        val releaseGroups = listOf(
            alsoSprachZarathustraReleaseGroupMusicBrainzModel,
            nutcrackerReleaseGroupMusicBrainzModel,
            tchaikovskyOverturesReleaseGroupMusicBrainzModel,
        )
        val releaseGroupsByEntityRepository = createReleaseGroupsByEntityRepository(
            releaseGroups = releaseGroups,
        )

        testFilter(
            pagingFlowProducer = { query ->
                releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
                    entityId = entityId,
                    entity = entity,
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
                        alsoSprachZarathustraReleaseGroupListItemModel,
                        nutcrackerReleaseGroupListItemModel,
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "1812",
                    expectedResult = listOf(
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "album",
                    expectedResult = listOf(
                        alsoSprachZarathustraReleaseGroupListItemModel,
                        nutcrackerReleaseGroupListItemModel,
                        tchaikovskyOverturesReleaseGroupListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit name",
                    query = "rattle",
                    expectedResult = listOf(
                        nutcrackerReleaseGroupListItemModel,
                    ),
                ),
            ),
        )
    }

//    @Test
//    fun `all releaseGroups`() = runTest {
//        setupReleaseGroupsByTchaikovsky()
//        setupReleaseGroupsByCollection()
//        setupReleaseGroupsByBerlinerPhilharmoniker()
//
//        val releaseGroupsByEntityRepository = createReleaseGroupsByEntityRepository(
//            releaseGroups = listOf(),
//        )
//        testFilter(
//            pagingFlowProducer = { query ->
//                releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
//                    entityId = null,
//                    entity = null,
//                    listFilters = ListFilters(
//                        query = query,
//                    ),
//                )
//            },
//            testCases = listOf(
//                FilterTestCase(
//                    description = "No filter",
//                    query = "",
//                    expectedResult = listOf(
//                        starmanReleaseGroupListItemModel,
//                        underPressureReleaseGroupListItemModel,
//                        hackingToTheGateReleaseGroupListItemModel,
//                        skycladObserverReleaseGroupListItemModel,
//                        dontStopMeNowReleaseGroupListItemModel,
//                    ),
//                ),
//                FilterTestCase(
//                    description = "filter by language",
//                    query = "jpn",
//                    expectedResult = listOf(
//                        hackingToTheGateReleaseGroupListItemModel,
//                        skycladObserverReleaseGroupListItemModel,
//                    ),
//                ),
//            ),
//        )
//    }

    // TODO: assert releaseGroups by collection
//    @Test
//    fun `refreshing releaseGroups that belong to multiple entities does not delete the releaseGroup`() = runTest {
//        setupReleaseGroupsByDavidBowie()
//        setupReleaseGroupsByQueen()
//
//        val modifiedReleaseGroups = listOf(
//            starmanReleaseGroupMusicBrainzModel.copy(
//                id = "new-id-is-considered-a-different-releaseGroup",
//            ),
//            underPressureReleaseGroupMusicBrainzModel.copy(
//                disambiguation = "changes will be ignored if releaseGroup is linked to multiple entities",
//            ),
//        )
//        val releaseGroupsByEntityRepository = createReleaseGroupsByEntityRepository(
//            releaseGroups = modifiedReleaseGroups,
//        )
//
//        // refresh
//        releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
//            entityId = davidBowieArtistMusicBrainzModel.id,
//            entity = MusicBrainzEntity.ARTIST,
//            listFilters = ListFilters(),
//        ).asSnapshot {
//            refresh()
//        }.run {
//            assertEquals(
//                listOf(
//                    underPressureReleaseGroupListItemModel,
//                    starmanReleaseGroupListItemModel.copy(
//                        id = "new-id-is-considered-a-different-releaseGroup",
//                    ),
//                ),
//                this,
//            )
//        }
//
//        // other entities remain unchanged
//        releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
//            entityId = queenArtistMusicBrainzModel.id,
//            entity = MusicBrainzEntity.ARTIST,
//            listFilters = ListFilters(),
//        ).asSnapshot().run {
//            assertEquals(
//                listOf(
//                    underPressureReleaseGroupListItemModel,
//                    dontStopMeNowReleaseGroupListItemModel,
//                ),
//                this,
//            )
//        }
//
//        releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
//            entityId = null,
//            entity = null,
//            listFilters = ListFilters(),
//        ).asSnapshot().run {
//            assertEquals(
//                listOf(
//                    underPressureReleaseGroupListItemModel,
//                    dontStopMeNowReleaseGroupListItemModel,
//                    starmanReleaseGroupListItemModel.copy(
//                        id = "new-id-is-considered-a-different-releaseGroup",
//                    ),
//                ),
//                this,
//            )
//        }
//
//        // now visit the releaseGroup and refresh it
//        val releaseGroupRepository = createReleaseGroupRepository(
//            underPressureReleaseGroupMusicBrainzModel.copy(
//                disambiguation = "changes will be ignored if releaseGroup is linked to multiple entities",
//            ),
//        )
//        releaseGroupRepository.lookupReleaseGroup(
//            releaseGroupId = underPressureReleaseGroupMusicBrainzModel.id,
//            forceRefresh = false,
//        ).let { releaseGroupDetailsModel ->
//            assertEquals(
//                ReleaseGroupDetailsModel(
//                    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
//                    name = "Under Pressure",
//                    type = "Song",
//                    language = "eng",
//                    iswcs = listOf(
//                        "T-010.475.727-8",
//                        "T-011.226.466-0",
//                    ),
//                    attributes = listOf(
//                        ReleaseGroupAttributeUiModel(
//                            value = "2182263",
//                            type = "ACAM ID",
//                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
//                        ),
//                        ReleaseGroupAttributeUiModel(
//                            value = "2406479",
//                            type = "ACAM ID",
//                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
//                        ),
//                    ),
//                ),
//                releaseGroupDetailsModel,
//            )
//        }
//        releaseGroupRepository.lookupReleaseGroup(
//            releaseGroupId = underPressureReleaseGroupMusicBrainzModel.id,
//            forceRefresh = true,
//        ).let { releaseGroupDetailsModel ->
//            assertEquals(
//                ReleaseGroupDetailsModel(
//                    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
//                    disambiguation = "changes will be ignored if releaseGroup is linked to multiple entities",
//                    name = "Under Pressure",
//                    type = "Song",
//                    language = "eng",
//                    iswcs = listOf(
//                        "T-010.475.727-8",
//                        "T-011.226.466-0",
//                    ),
//                    attributes = listOf(
//                        ReleaseGroupAttributeUiModel(
//                            value = "2182263",
//                            type = "ACAM ID",
//                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
//                        ),
//                        ReleaseGroupAttributeUiModel(
//                            value = "2406479",
//                            type = "ACAM ID",
//                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
//                        ),
//                    ),
//                ),
//                releaseGroupDetailsModel,
//            )
//        }
//    }
}

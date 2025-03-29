package ly.david.musicsearch.data.repository.releasegroup

import androidx.paging.testing.asSnapshot
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
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupDetailsModel
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsByEntityRepository
import org.junit.Assert.assertEquals
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

    @Test
    fun `all release groups`() = runTest {
        setupReleaseGroupsByTchaikovsky()
        setupReleaseGroupsByCollection()
        setupReleaseGroupsByBerlinerPhilharmoniker()

        val releaseGroupsByEntityRepository = createReleaseGroupsByEntityRepository(
            releaseGroups = listOf(),
        )
        testFilter(
            pagingFlowProducer = { query ->
                releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
                    entityId = null,
                    entity = null,
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
                        nutcrackerReleaseGroupListItemModel,
                        tchaikovskyOverturesReleaseGroupListItemModel,
                        alsoSprachZarathustraReleaseGroupListItemModel,
                    ),
                ),
            ),
        )
    }

    // TODO: assert releaseGroups by collection
    @Test
    fun `refreshing releaseGroups that belong to multiple entities does not delete the releaseGroup`() = runTest {
        setupReleaseGroupsByBerlinerPhilharmoniker()
        setupReleaseGroupsByTchaikovsky()

        val modifiedReleaseGroups = listOf(
            alsoSprachZarathustraReleaseGroupMusicBrainzModel,
            nutcrackerReleaseGroupMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-release-group",
            ),
            tchaikovskyOverturesReleaseGroupMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if release group is linked to multiple entities",
            ),
        )
        val releaseGroupsByEntityRepository = createReleaseGroupsByEntityRepository(
            releaseGroups = modifiedReleaseGroups,
        )

        // refresh
        releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
            entityId = berlinerPhilharmonikerArtistMusicBrainzModel.id,
            entity = MusicBrainzEntity.ARTIST,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    alsoSprachZarathustraReleaseGroupListItemModel,
                    nutcrackerReleaseGroupListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    tchaikovskyOverturesReleaseGroupListItemModel,
                ),
                this,
            )
        }

        // other entities remain unchanged
        releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
            entityId = tchaikovskyArtistMusicBrainzModel.id,
            entity = MusicBrainzEntity.ARTIST,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    nutcrackerReleaseGroupListItemModel,
                    tchaikovskyOverturesReleaseGroupListItemModel,
                ),
                this,
            )
        }

        releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    nutcrackerReleaseGroupListItemModel,
                    tchaikovskyOverturesReleaseGroupListItemModel,
                    alsoSprachZarathustraReleaseGroupListItemModel,
                    nutcrackerReleaseGroupListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                ),
                this,
            )
        }

        // now visit the releaseGroup and refresh it
        val releaseGroupRepository = createReleaseGroupRepository(
            tchaikovskyOverturesReleaseGroupMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if release group is linked to multiple entities",
            ),
        )
        releaseGroupRepository.lookupReleaseGroup(
            releaseGroupId = tchaikovskyOverturesReleaseGroupMusicBrainzModel.id,
            forceRefresh = false,
        ).let { releaseGroupDetailsModel ->
            assertEquals(
                ReleaseGroupDetailsModel(
                    id = "3e76b16f-c8ef-342a-b909-ca50d92766d2",
                    name = "“1812” Overture / Romeo and Juliet / Marche slave / The Tempest",
                    artistCredits = listOf(
                        ArtistCreditUiModel(
                            artistId = "9ddd7abc-9e1b-471d-8031-583bc6bc8be9",
                            name = "Tchaikovsky",
                            joinPhrase = "; ",
                        ),
                        ArtistCreditUiModel(
                            artistId = "dea28aa9-1086-4ffa-8739-0ccc759de1ce",
                            name = "Berliner Philharmoniker",
                            joinPhrase = ", ",
                        ),
                        ArtistCreditUiModel(
                            artistId = "39e84597-3e0f-4ccc-89d2-6ee1dd6fb050",
                            name = "Claudio Abbado",
                            joinPhrase = "",
                        ),
                    ),
                    primaryType = "Album",
                    firstReleaseDate = "2000-02-01",
                    disambiguation = "",
                ),
                releaseGroupDetailsModel,
            )
        }
        releaseGroupRepository.lookupReleaseGroup(
            releaseGroupId = tchaikovskyOverturesReleaseGroupMusicBrainzModel.id,
            forceRefresh = true,
        ).let { releaseGroupDetailsModel ->
            assertEquals(
                ReleaseGroupDetailsModel(
                    id = "3e76b16f-c8ef-342a-b909-ca50d92766d2",
                    name = "“1812” Overture / Romeo and Juliet / Marche slave / The Tempest",
                    artistCredits = listOf(
                        ArtistCreditUiModel(
                            artistId = "9ddd7abc-9e1b-471d-8031-583bc6bc8be9",
                            name = "Tchaikovsky",
                            joinPhrase = "; ",
                        ),
                        ArtistCreditUiModel(
                            artistId = "dea28aa9-1086-4ffa-8739-0ccc759de1ce",
                            name = "Berliner Philharmoniker",
                            joinPhrase = ", ",
                        ),
                        ArtistCreditUiModel(
                            artistId = "39e84597-3e0f-4ccc-89d2-6ee1dd6fb050",
                            name = "Claudio Abbado",
                            joinPhrase = "",
                        ),
                    ),
                    primaryType = "Album",
                    firstReleaseDate = "2000-02-01",
                    disambiguation = "changes will be ignored if release group is linked to multiple entities",
                ),
                releaseGroupDetailsModel,
            )
        }

        releaseGroupsByEntityRepository.observeReleaseGroupsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    nutcrackerReleaseGroupListItemModel,
                    alsoSprachZarathustraReleaseGroupListItemModel,
                    nutcrackerReleaseGroupListItemModel.copy(
                        id = "new-id-is-considered-a-different-release-group",
                    ),
                    tchaikovskyOverturesReleaseGroupListItemModel.copy(
                        disambiguation = "changes will be ignored if release group is linked to multiple entities",
                        visited = true,
                    ),
                ),
                this,
            )
        }
    }
}

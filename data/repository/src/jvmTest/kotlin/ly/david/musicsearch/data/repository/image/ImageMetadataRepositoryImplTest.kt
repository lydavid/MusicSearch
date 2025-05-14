package ly.david.musicsearch.data.repository.image

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.NoOpCoverArtArchiveApi
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtUrls
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.data.coverart.api.ThumbnailsUrls
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.TestEventRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseGroupRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseRepository
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ImageMetadataRepositoryImplTest :
    KoinTest,
    TestEventRepository,
    TestReleaseRepository,
    TestReleaseGroupRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val imageUrlDao: ImageUrlDao by inject()
    private val coroutineDispatchers: CoroutineDispatchers by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    override val eventDao: EventDao by inject()
    override val releaseDao: ReleaseDao by inject()
    override val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    override val releaseGroupDao: ReleaseGroupDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val areaDao: AreaDao by inject()
    override val labelDao: LabelDao by inject()
    override val mediumDao: MediumDao by inject()
    override val trackDao: TrackDao by inject()

    private fun createRepository(
        coverArtUrlsProducer: (id: String, entity: MusicBrainzEntity) -> List<CoverArtUrls>,
    ): ImageMetadataRepository {
        return ImageMetadataRepositoryImpl(
            coverArtArchiveApi = object : NoOpCoverArtArchiveApi() {
                override suspend fun getCoverArts(
                    mbid: String,
                    entity: MusicBrainzEntity,
                ): CoverArtsResponse {
                    return CoverArtsResponse(
                        coverArtUrls = coverArtUrlsProducer(mbid, entity),
                    )
                }
            },
            imageUrlDao = imageUrlDao,
            logger = object : Logger {
                override fun d(text: String) {
                    println(text)
                }

                override fun e(exception: Exception) {
                    error(exception)
                }
            },
            coroutineScope = TestScope(coroutineDispatchers.io),
        )
    }

    @Test
    fun empty() = runTest {
        val repository = createRepository(coverArtUrlsProducer = { _, _ -> listOf() })
        val imageMetadata = repository.getAndSaveImageMetadata(
            mbid = "",
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
            ),
            imageMetadata,
        )
        Assert.assertEquals(
            0,
            repository.getNumberOfImageMetadataById(""),
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        Assert.assertEquals(
            true,
            imageMetadataList.isEmpty(),
        )
    }

    @Test
    fun `get event cover art`() = runTest {
        val eventId = "a"
        val eventName = "aa"
        val eventDisambiguation = "d"
        createEventRepository(
            EventMusicBrainzModel(
                id = eventId,
                name = eventName,
                disambiguation = eventDisambiguation,
            ),
        ).lookupEvent(
            eventId = eventId,
            forceRefresh = false,
        )
        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                )
            },
        )
        val imageMetadata = repository.getAndSaveImageMetadata(
            mbid = eventId,
            entity = MusicBrainzEntity.EVENT,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )
        Assert.assertEquals(
            1,
            repository.getNumberOfImageMetadataById(eventId),
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        Assert.assertEquals(
            1,
            imageMetadataList.size,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                mbid = eventId,
                name = eventName,
                disambiguation = eventDisambiguation,
                entity = MusicBrainzEntity.EVENT,
            ),
            imageMetadataList[0],
        )
    }

    @Test
    fun `get release cover art`() = runTest {
        val releaseId = "release_id"
        val releaseName = "release name"
        val releaseDisambiguation = "release disambiguation"
        createReleaseRepository(
            ReleaseMusicBrainzModel(
                id = releaseId,
                name = releaseName,
                disambiguation = releaseDisambiguation,
                releaseGroup = ReleaseGroupMusicBrainzModel(
                    id = "release_group_id",
                    name = "release group name",
                    disambiguation = "release group disambiguation",
                ),
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "artist name",
                        artist = ArtistMusicBrainzModel(
                            id = "artist_id",
                            name = "artist_name",
                        ),
                    ),
                ),
            ),
        ).lookupRelease(
            releaseId = releaseId,
            forceRefresh = false,
        )

        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                )
            },
        )
        val imageMetadata = repository.getAndSaveImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )
        Assert.assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseId),
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        Assert.assertEquals(
            1,
            imageMetadataList.size,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                mbid = releaseId,
                name = releaseName,
                disambiguation = releaseDisambiguation,
                entity = MusicBrainzEntity.RELEASE,
            ),
            imageMetadataList[0],
        )
    }

    @Test
    fun `deleting release will still show cover art`() = runTest {
        val releaseId = "a"
        val releaseName = "aa"
        val releaseDisambiguation = "d"
        releaseDao.insert(
            ReleaseMusicBrainzModel(
                id = releaseId,
                name = releaseName,
                disambiguation = releaseDisambiguation,
            ),
        )
        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                )
            },
        )
        val imageMetadata = repository.getAndSaveImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )

        releaseDao.delete(releaseId)
        Assert.assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseId),
        )
        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        Assert.assertEquals(
            1,
            imageMetadataList.size,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                mbid = releaseId,
                name = null,
                disambiguation = null,
                entity = null,
            ),
            imageMetadataList[0],
        )
    }

    @Test
    fun `get release group cover art`() = runTest {
        val releaseGroupId = "a"
        val releaseGroupName = "aa"
        val releaseGroupDisambiguation = "d"
        createReleaseGroupRepository(
            ReleaseGroupMusicBrainzModel(
                id = releaseGroupId,
                name = releaseGroupName,
                disambiguation = releaseGroupDisambiguation,
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "artist name",
                        artist = ArtistMusicBrainzModel(
                            id = "artist_id",
                            name = "artist_name",
                        ),
                    ),
                ),
            ),
        ).lookupReleaseGroup(
            releaseGroupId = releaseGroupId,
            forceRefresh = false,
        )

        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                )
            },
        )
        val imageMetadata = repository.getAndSaveImageMetadata(
            mbid = releaseGroupId,
            entity = MusicBrainzEntity.RELEASE_GROUP,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )
        Assert.assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseGroupId),
        )

        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot().run {
            Assert.assertEquals(
                1,
                size,
            )
            Assert.assertEquals(
                ImageMetadata(
                    databaseId = 1L,
                    thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    mbid = releaseGroupId,
                    name = releaseGroupName,
                    disambiguation = releaseGroupDisambiguation,
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                ),
                this[0],
            )
        }
    }

    @Test
    fun `get event, release, release group cover arts, then sort`() = runTest {
        val eventId = "event-id"
        val eventName = "event name"
        val eventDisambiguation = "d"
        val repository = createRepository(
            coverArtUrlsProducer = { mbid, entity ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "not used right now",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://someartarchive.org/${entity.resourceUri}/$mbid/1.png",
                        ),
                    ),
                )
            },
        )

        eventDao.insert(
            EventMusicBrainzModel(
                id = eventId,
                name = eventName,
                disambiguation = eventDisambiguation,
            ),
        )
        val eventImageMetadata = repository.getAndSaveImageMetadata(
            mbid = eventId,
            entity = MusicBrainzEntity.EVENT,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                largeUrl = "http://someartarchive.org/event/$eventId/1.png",
            ),
            eventImageMetadata,
        )
        Assert.assertEquals(
            1,
            repository.getNumberOfImageMetadataById(eventId),
        )

        val releaseId = "release-id"
        val releaseName = "release name"
        val releaseDisambiguation = "d"
        releaseDao.insert(
            ReleaseMusicBrainzModel(
                id = releaseId,
                name = releaseName,
                disambiguation = releaseDisambiguation,
            ),
        )
        val releaseImageMetadata = repository.getAndSaveImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 2L,
                thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
            ),
            releaseImageMetadata,
        )
        Assert.assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseId),
        )

        val releaseGroupId = "release-group-id"
        val releaseGroupName = "release group name"
        val releaseGroupDisambiguation = "d"
        releaseGroupDao.insertReleaseGroup(
            ReleaseGroupMusicBrainzModel(
                id = releaseGroupId,
                name = releaseGroupName,
                disambiguation = releaseGroupDisambiguation,
            ),
        )
        val releaseGroupImageMetadata = repository.getAndSaveImageMetadata(
            mbid = releaseGroupId,
            entity = MusicBrainzEntity.RELEASE_GROUP,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadata(
                databaseId = 3L,
                thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
            ),
            releaseGroupImageMetadata,
        )
        Assert.assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseGroupId),
        )

        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot().let { imageMetadataList ->
            Assert.assertEquals(
                3,
                imageMetadataList.size,
            )
            Assert.assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 3L,
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        mbid = releaseGroupId,
                        name = releaseGroupName,
                        disambiguation = releaseGroupDisambiguation,
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                    ImageMetadata(
                        databaseId = 2L,
                        thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        mbid = releaseId,
                        name = releaseName,
                        disambiguation = releaseDisambiguation,
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                    ImageMetadata(
                        databaseId = 1L,
                        thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                        largeUrl = "http://someartarchive.org/event/$eventId/1.png",
                        mbid = eventId,
                        name = eventName,
                        disambiguation = eventDisambiguation,
                        entity = MusicBrainzEntity.EVENT,
                    ),
                ),
                imageMetadataList,
            )
        }

        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.LEAST_RECENTLY_ADDED,
        ).asSnapshot().let { imageMetadataList ->
            Assert.assertEquals(
                3,
                imageMetadataList.size,
            )
            Assert.assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 1L,
                        thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                        largeUrl = "http://someartarchive.org/event/$eventId/1.png",
                        mbid = eventId,
                        name = eventName,
                        disambiguation = eventDisambiguation,
                        entity = MusicBrainzEntity.EVENT,
                    ),
                    ImageMetadata(
                        databaseId = 2L,
                        thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        mbid = releaseId,
                        name = releaseName,
                        disambiguation = releaseDisambiguation,
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                    ImageMetadata(
                        databaseId = 3L,
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        mbid = releaseGroupId,
                        name = releaseGroupName,
                        disambiguation = releaseGroupDisambiguation,
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                ),
                imageMetadataList,
            )
        }

        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.ALPHABETICALLY,
        ).asSnapshot().let { imageMetadataList ->
            Assert.assertEquals(
                3,
                imageMetadataList.size,
            )
            Assert.assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 1L,
                        thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                        largeUrl = "http://someartarchive.org/event/$eventId/1.png",
                        mbid = eventId,
                        name = eventName,
                        disambiguation = eventDisambiguation,
                        entity = MusicBrainzEntity.EVENT,
                    ),
                    ImageMetadata(
                        databaseId = 3L,
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        mbid = releaseGroupId,
                        name = releaseGroupName,
                        disambiguation = releaseGroupDisambiguation,
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                    ImageMetadata(
                        databaseId = 2L,
                        thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        mbid = releaseId,
                        name = releaseName,
                        disambiguation = releaseDisambiguation,
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                ),
                imageMetadataList,
            )
        }

        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.ALPHABETICALLY_REVERSE,
        ).asSnapshot().let { imageMetadataList ->
            Assert.assertEquals(
                3,
                imageMetadataList.size,
            )
            Assert.assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 2L,
                        thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        mbid = releaseId,
                        name = releaseName,
                        disambiguation = releaseDisambiguation,
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                    ImageMetadata(
                        databaseId = 3L,
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        mbid = releaseGroupId,
                        name = releaseGroupName,
                        disambiguation = releaseGroupDisambiguation,
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                    ImageMetadata(
                        databaseId = 1L,
                        thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                        largeUrl = "http://someartarchive.org/event/$eventId/1.png",
                        mbid = eventId,
                        name = eventName,
                        disambiguation = eventDisambiguation,
                        entity = MusicBrainzEntity.EVENT,
                    ),
                ),
                imageMetadataList,
            )
        }
    }

    @Test
    fun `queue 99, then 1 more lookup for image metadata`() = runTest {
        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                )
            },
        )

        (1..99).forEach {
            repository.saveImageMetadata(
                mbid = "$it",
                entity = MusicBrainzEntity.RELEASE,
                itemsCount = 99,
            )
        }
        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot().run {
            Assert.assertEquals(
                99,
                size,
            )
        }
        repository.saveImageMetadata(
            mbid = "100",
            entity = MusicBrainzEntity.RELEASE,
            itemsCount = 100,
        )
        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot().run {
            Assert.assertEquals(
                100,
                size,
            )
        }
    }

    @Test
    fun `queue 100 lookups that returns more than 1 image metadata each`() = runTest {
        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510392.png",
                        front = false,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510392.png",
                        ),
                    ),
                )
            },
        )

        (1..100).forEach {
            repository.saveImageMetadata(
                mbid = "$it",
                entity = MusicBrainzEntity.RELEASE,
                itemsCount = 100,
            )
        }
        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot {
            scrollTo(100)
        }.run {
            Assert.assertEquals(
                200,
                size,
            )
        }
    }

    @Test
    fun `queue 99 lookups for image metadata, waiting for timeout to write`() = runTest {
        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                )
            },
        )

        (1..99).forEach {
            repository.saveImageMetadata(
                mbid = "$it",
                entity = MusicBrainzEntity.RELEASE,
                itemsCount = 99,
            )
        }
        testScheduler.advanceTimeBy(3.seconds)
        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot().run {
            Assert.assertEquals(
                99,
                size,
            )
        }
    }

    @Test
    fun `when less than 200 items, save immediately, otherwise batch writes`() = runTest {
        val repository = createRepository(
            coverArtUrlsProducer = { _, _ ->
                listOf(
                    CoverArtUrls(
                        imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                        ),
                    ),
                )
            },
        )

        (1..100).forEach {
            repository.saveImageMetadata(
                mbid = "$it",
                entity = MusicBrainzEntity.RELEASE,
                itemsCount = 100,
            )
            repository.observeAllImageMetadata(
                mbid = null,
                query = "",
                sortOption = ImagesSortOption.RECENTLY_ADDED,
            ).asSnapshot().run {
                Assert.assertEquals(
                    it,
                    size,
                )
            }
        }
        (101..200).forEach {
            repository.saveImageMetadata(
                mbid = "$it",
                entity = MusicBrainzEntity.RELEASE,
                itemsCount = 200, // This value will be updated as more data is loaded into our local database
            )
            repository.observeAllImageMetadata(
                mbid = null,
                query = "",
                sortOption = ImagesSortOption.RECENTLY_ADDED,
            ).asSnapshot {
                scrollTo(100)
            }.run {
                Assert.assertEquals(
                    it,
                    size,
                )
            }
        }

        (201..299).forEach {
            repository.saveImageMetadata(
                mbid = "$it",
                entity = MusicBrainzEntity.RELEASE,
                itemsCount = 300,
            )
            repository.observeAllImageMetadata(
                mbid = null,
                query = "",
                sortOption = ImagesSortOption.RECENTLY_ADDED,
            ).asSnapshot {
                scrollTo(200)
            }.run {
                Assert.assertEquals(
                    200,
                    size,
                )
            }
        }
        repository.saveImageMetadata(
            mbid = "300",
            entity = MusicBrainzEntity.RELEASE,
            itemsCount = 300,
        )
        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot {
            scrollTo(200)
        }.run {
            Assert.assertEquals(
                300,
                size,
            )
        }
    }

    @Before
    fun before() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }
}

package ly.david.musicsearch.data.repository.image

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.coverart.api.CoverArtUrls
import ly.david.musicsearch.data.coverart.api.ThumbnailsUrls
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.MediumDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.database.dao.TrackDao
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.he.TestMusicBrainzImageMetadataRepository
import ly.david.musicsearch.data.repository.helpers.TestEventRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseGroupRepository
import ly.david.musicsearch.data.repository.helpers.TestReleaseRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
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
    TestMusicBrainzImageMetadataRepository,
    TestEventRepository,
    TestReleaseRepository,
    TestReleaseGroupRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val imageUrlDao: ImageUrlDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
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

    @Test
    fun empty() = runTest {
        val repository = createMusicBrainzImageMetadataRepository(coverArtUrlsProducer = { _, _ -> listOf() })
        val imageMetadataWithCount = repository.getAndSaveImageMetadata(
            mbid = "",
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadataWithCount(
                ImageMetadata(
                    imageId = ImageId(1L),
                ),
                count = 0,
            ),
            imageMetadataWithCount,
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
            EventMusicBrainzNetworkModel(
                id = eventId,
                name = eventName,
                disambiguation = eventDisambiguation,
            ),
        ).lookupEvent(
            eventId = eventId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        val repository = createMusicBrainzImageMetadataRepository(
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
        val imageMetadataWithCount = repository.getAndSaveImageMetadata(
            mbid = eventId,
            entity = MusicBrainzEntity.EVENT,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(1L),
                    thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                ),
                count = 1,
            ),
            imageMetadataWithCount,
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
                imageId = ImageId(1L),
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
            ReleaseMusicBrainzNetworkModel(
                id = releaseId,
                name = releaseName,
                disambiguation = releaseDisambiguation,
                releaseGroup = ReleaseGroupMusicBrainzNetworkModel(
                    id = "release_group_id",
                    name = "release group name",
                    disambiguation = "release group disambiguation",
                ),
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "artist name",
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "artist_id",
                            name = "artist_name",
                        ),
                    ),
                ),
            ),
        ).lookupRelease(
            releaseId = releaseId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )

        val repository = createMusicBrainzImageMetadataRepository(
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
                        front = true,
                        thumbnailsUrls = ThumbnailsUrls(
                            resolution250Url = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510392.png",
                        ),
                    ),
                )
            },
        )
        val imageMetadataWithCount = repository.getAndSaveImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(1L),
                    thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                ),
                count = 2,
            ),
            imageMetadataWithCount,
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        Assert.assertEquals(
            2,
            imageMetadataList.size,
        )
        Assert.assertEquals(
            listOf(
                ImageMetadata(
                    imageId = ImageId(2L),
                    thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510392.png",
                    largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510392.png",
                    mbid = releaseId,
                    name = releaseName,
                    disambiguation = releaseDisambiguation,
                    entity = MusicBrainzEntity.RELEASE,
                ),
                ImageMetadata(
                    imageId = ImageId(1L),
                    thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    mbid = releaseId,
                    name = releaseName,
                    disambiguation = releaseDisambiguation,
                    entity = MusicBrainzEntity.RELEASE,
                ),
            ),
            imageMetadataList,
        )
    }

    // When deleting with repository, it will delete the cover art, whereas this is invoking the DAO
    @Test
    fun `deleting release will still show cover art`() = runTest {
        val releaseId = "a"
        val releaseName = "aa"
        val releaseDisambiguation = "d"
        releaseDao.insert(
            ReleaseMusicBrainzNetworkModel(
                id = releaseId,
                name = releaseName,
                disambiguation = releaseDisambiguation,
            ),
        )
        val repository = createMusicBrainzImageMetadataRepository(
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
        val imageMetadataWithCount = repository.getAndSaveImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(1L),
                    thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                ),
                count = 1,
            ),
            imageMetadataWithCount,
        )

        releaseDao.delete(releaseId)
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
                imageId = ImageId(1L),
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
            ReleaseGroupMusicBrainzNetworkModel(
                id = releaseGroupId,
                name = releaseGroupName,
                disambiguation = releaseGroupDisambiguation,
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        name = "artist name",
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "artist_id",
                            name = "artist_name",
                        ),
                    ),
                ),
            ),
        ).lookupReleaseGroup(
            releaseGroupId = releaseGroupId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )

        val repository = createMusicBrainzImageMetadataRepository(
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
        val imageMetadataWithCount = repository.getAndSaveImageMetadata(
            mbid = releaseGroupId,
            entity = MusicBrainzEntity.RELEASE_GROUP,
            forceRefresh = false,
        )
        Assert.assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(1L),
                    thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                ),
                count = 1,
            ),
            imageMetadataWithCount,
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
                    imageId = ImageId(1L),
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
        val repository = createMusicBrainzImageMetadataRepository(
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
            EventMusicBrainzNetworkModel(
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
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(1L),
                    thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                    largeUrl = "http://someartarchive.org/event/$eventId/1.png",
                ),
                count = 1,
            ),
            eventImageMetadata,
        )

        val releaseId = "release-id"
        val releaseName = "release name"
        val releaseDisambiguation = "d"
        releaseDao.insert(
            ReleaseMusicBrainzNetworkModel(
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
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(2L),
                    thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                    largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                ),
                count = 1,
            ),
            releaseImageMetadata,
        )

        val releaseGroupId = "release-group-id"
        val releaseGroupName = "release group name"
        val releaseGroupDisambiguation = "d"
        releaseGroupDao.insertReleaseGroup(
            ReleaseGroupMusicBrainzNetworkModel(
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
            ImageMetadataWithCount(
                imageMetadata =
                    ImageMetadata(
                        imageId = ImageId(3L),
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                    ),
                count = 1,
            ),
            releaseGroupImageMetadata,
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
                        imageId = ImageId(3L),
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        mbid = releaseGroupId,
                        name = releaseGroupName,
                        disambiguation = releaseGroupDisambiguation,
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                    ImageMetadata(
                        imageId = ImageId(2L),
                        thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        mbid = releaseId,
                        name = releaseName,
                        disambiguation = releaseDisambiguation,
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                    ImageMetadata(
                        imageId = ImageId(1L),
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
                        imageId = ImageId(1L),
                        thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                        largeUrl = "http://someartarchive.org/event/$eventId/1.png",
                        mbid = eventId,
                        name = eventName,
                        disambiguation = eventDisambiguation,
                        entity = MusicBrainzEntity.EVENT,
                    ),
                    ImageMetadata(
                        imageId = ImageId(2L),
                        thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        mbid = releaseId,
                        name = releaseName,
                        disambiguation = releaseDisambiguation,
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                    ImageMetadata(
                        imageId = ImageId(3L),
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
                        imageId = ImageId(1L),
                        thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                        largeUrl = "http://someartarchive.org/event/$eventId/1.png",
                        mbid = eventId,
                        name = eventName,
                        disambiguation = eventDisambiguation,
                        entity = MusicBrainzEntity.EVENT,
                    ),
                    ImageMetadata(
                        imageId = ImageId(3L),
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        mbid = releaseGroupId,
                        name = releaseGroupName,
                        disambiguation = releaseGroupDisambiguation,
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                    ImageMetadata(
                        imageId = ImageId(2L),
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
                        imageId = ImageId(2L),
                        thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
                        mbid = releaseId,
                        name = releaseName,
                        disambiguation = releaseDisambiguation,
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                    ImageMetadata(
                        imageId = ImageId(3L),
                        thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                        mbid = releaseGroupId,
                        name = releaseGroupName,
                        disambiguation = releaseGroupDisambiguation,
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                    ImageMetadata(
                        imageId = ImageId(1L),
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
        val repository = createMusicBrainzImageMetadataRepository(
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
        val repository = createMusicBrainzImageMetadataRepository(
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
        val repository = createMusicBrainzImageMetadataRepository(
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
        val repository = createMusicBrainzImageMetadataRepository(
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

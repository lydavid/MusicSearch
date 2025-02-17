package ly.david.musicsearch.data.coverart

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.NoOpCoverArtArchiveApi
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtUrls
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.data.coverart.api.ThumbnailsUrls
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzModel
import ly.david.musicsearch.shared.domain.coverarts.CoverArtsSortOption
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.resourceUri
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ImageMetadataRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val imageUrlDao: ImageUrlDao by inject()
    private val coroutineDispatchers: CoroutineDispatchers by inject()
    private val eventDao: EventDao by inject()
    private val releaseDao: ReleaseDao by inject()
    private val releaseGroupDao: ReleaseGroupDao by inject()

    private fun createRepository(
        coverArtUrlsProducer: (id: String, entity: MusicBrainzEntity) -> List<CoverArtUrls>,
    ): ImageMetadataRepositoryImpl {
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
                    error(text)
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
        val imageMetadata = repository.getImageMetadata(
            mbid = "",
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 1L,
            ),
            imageMetadata,
        )
        assertEquals(
            0,
            repository.getNumberOfImageMetadataById(""),
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = CoverArtsSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        assertEquals(
            true,
            imageMetadataList.isEmpty(),
        )
    }

    @Test
    fun `get event cover art`() = runTest {
        val eventId = "a"
        val eventName = "aa"
        val eventDisambiguation = "d"
        eventDao.insert(
            EventMusicBrainzModel(
                id = eventId,
                name = eventName,
                disambiguation = eventDisambiguation,
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
        val imageMetadata = repository.getImageMetadata(
            mbid = eventId,
            entity = MusicBrainzEntity.EVENT,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )
        assertEquals(
            1,
            repository.getNumberOfImageMetadataById(eventId),
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = CoverArtsSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        assertEquals(
            1,
            imageMetadataList.size,
        )
        assertEquals(
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
        val imageMetadata = repository.getImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )
        assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseId),
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = CoverArtsSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        assertEquals(
            1,
            imageMetadataList.size,
        )
        assertEquals(
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
        val imageMetadata = repository.getImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )

        releaseDao.delete(releaseId)
        assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseId),
        )
        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = CoverArtsSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        assertEquals(
            1,
            imageMetadataList.size,
        )
        assertEquals(
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
        releaseGroupDao.insert(
            ReleaseGroupMusicBrainzModel(
                id = releaseGroupId,
                name = releaseGroupName,
                disambiguation = releaseGroupDisambiguation,
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
        val imageMetadata = repository.getImageMetadata(
            mbid = releaseGroupId,
            entity = MusicBrainzEntity.RELEASE_GROUP,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
            ),
            imageMetadata,
        )
        assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseGroupId),
        )

        val flow = repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = CoverArtsSortOption.RECENTLY_ADDED,
        )
        val imageMetadataList = flow.asSnapshot()
        assertEquals(
            1,
            imageMetadataList.size,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                largeUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                mbid = releaseGroupId,
                name = releaseGroupName,
                disambiguation = releaseGroupDisambiguation,
                entity = MusicBrainzEntity.RELEASE_GROUP,
            ),
            imageMetadataList[0],
        )
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
        val eventImageMetadata = repository.getImageMetadata(
            mbid = eventId,
            entity = MusicBrainzEntity.EVENT,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 1L,
                thumbnailUrl = "http://someartarchive.org/event/$eventId/1.png",
                largeUrl = "http://someartarchive.org/event/$eventId/1.png",
            ),
            eventImageMetadata,
        )
        assertEquals(
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
        val releaseImageMetadata = repository.getImageMetadata(
            mbid = releaseId,
            entity = MusicBrainzEntity.RELEASE,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 2L,
                thumbnailUrl = "http://someartarchive.org/release/$releaseId/1.png",
                largeUrl = "http://someartarchive.org/release/$releaseId/1.png",
            ),
            releaseImageMetadata,
        )
        assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseId),
        )

        val releaseGroupId = "release-group-id"
        val releaseGroupName = "release group name"
        val releaseGroupDisambiguation = "d"
        releaseGroupDao.insert(
            ReleaseGroupMusicBrainzModel(
                id = releaseGroupId,
                name = releaseGroupName,
                disambiguation = releaseGroupDisambiguation,
            ),
        )
        val releaseGroupImageMetadata = repository.getImageMetadata(
            mbid = releaseGroupId,
            entity = MusicBrainzEntity.RELEASE_GROUP,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadata(
                databaseId = 3L,
                thumbnailUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
                largeUrl = "http://someartarchive.org/release-group/$releaseGroupId/1.png",
            ),
            releaseGroupImageMetadata,
        )
        assertEquals(
            1,
            repository.getNumberOfImageMetadataById(releaseGroupId),
        )

        repository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = CoverArtsSortOption.RECENTLY_ADDED,
        ).asSnapshot().let { imageMetadataList ->
            assertEquals(
                3,
                imageMetadataList.size,
            )
            assertEquals(
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
            sortOption = CoverArtsSortOption.LEAST_RECENTLY_ADDED,
        ).asSnapshot().let { imageMetadataList ->
            assertEquals(
                3,
                imageMetadataList.size,
            )
            assertEquals(
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
            sortOption = CoverArtsSortOption.ALPHABETICALLY,
        ).asSnapshot().let { imageMetadataList ->
            assertEquals(
                3,
                imageMetadataList.size,
            )
            assertEquals(
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
            sortOption = CoverArtsSortOption.ALPHABETICALLY_REVERSE,
        ).asSnapshot().let { imageMetadataList ->
            assertEquals(
                3,
                imageMetadataList.size,
            )
            assertEquals(
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
}

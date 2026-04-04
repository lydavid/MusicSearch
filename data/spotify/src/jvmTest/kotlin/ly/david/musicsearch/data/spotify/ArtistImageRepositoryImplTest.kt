package ly.david.musicsearch.data.spotify

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeSpotifyApi
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.spotify.api.SpotifyArtist
import ly.david.musicsearch.data.spotify.auth.SpotifyOAuthInfo
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImageSource
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.RawImageMetadata
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

private const val WIKIMEDIA_LARGE_URL =
    "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/Radwinps2016.jpg"
private const val WIKIMEDIA_THUMBNAIL_URL =
    "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/Radwinps2016.jpg&width=64"

class ArtistImageRepositoryImplTest : KoinTest {
    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val imageUrlDao: ImageUrlDao by inject()

    private val artistDetailsModel = ArtistDetailsModel(
        id = "a",
        name = "artist",
        urls = persistentListOf(
            RelationListItemModel(
                id = "a_u_1",
                linkedEntityId = "u",
                type = "free streaming",
                name = "https://open.spotify.com/artist/1EowJ1WwkMzkCkRomFhui7",
                linkedEntity = MusicBrainzEntityType.URL,
            ),
        ),
    )

    fun createArtistImageRepository(
        spotifyOAuthInfo: SpotifyOAuthInfo,
        wikidataImage: RawImageMetadata,
        spotifyApi: FakeSpotifyApi,
    ) = ArtistImageRepositoryImpl(
        spotifyOAuthInfo = spotifyOAuthInfo,
        spotifyApi = spotifyApi,
        wikimediaRepository = object : WikimediaRepository {
            override suspend fun getWikipediaExtract(
                mbid: String,
                urls: List<RelationListItemModel>,
                languageTag: String,
                forceRefresh: Boolean,
            ): Result<WikipediaExtract> {
                error("Never called")
            }

            override suspend fun getWikimediaImage(urls: List<RelationListItemModel>): RawImageMetadata {
                return wikidataImage
            }
        },
        imageUrlDao = imageUrlDao,
        logger = object : Logger {
            override fun d(text: String) {
                error("Never called")
            }

            override fun e(exception: Exception) {
                error("Never called")
            }
        },
    )

    @Test
    fun `with Spotify secrets and link`() = runTest {
        val artistImageRepository = createArtistImageRepository(
            spotifyOAuthInfo = SpotifyOAuthInfo(
                clientId = "id",
                clientSecret = "secret",
            ),
            wikidataImage = RawImageMetadata(
                largeUrl = WIKIMEDIA_LARGE_URL,
                thumbnailUrl = WIKIMEDIA_THUMBNAIL_URL,
                source = ImageSource.WIKIMEDIA,
            ),
            spotifyApi = FakeSpotifyApi(),
        )

        val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
            detailsModel = artistDetailsModel,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata.Spotify(
                    imageId = ImageId(
                        value = 1,
                    ),
                    rawLargeUrl = "i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
                    rawThumbnailUrl = "i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
                ),
                count = 1,
            ),
            artistImageMetadata,
        )
    }

    @Test
    fun `with Spotify secrets and link but no images returned by API`() = runTest {
        val artistImageRepository = createArtistImageRepository(
            spotifyOAuthInfo = SpotifyOAuthInfo(
                clientId = "id",
                clientSecret = "secret",
            ),
            wikidataImage = RawImageMetadata(
                largeUrl = WIKIMEDIA_LARGE_URL,
                thumbnailUrl = WIKIMEDIA_THUMBNAIL_URL,
                source = ImageSource.WIKIMEDIA,
            ),
            spotifyApi = object : FakeSpotifyApi() {
                override suspend fun getArtist(spotifyArtistId: String): SpotifyArtist {
                    return SpotifyArtist()
                }
            },
        )

        val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
            detailsModel = artistDetailsModel,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata.Spotify(
                    imageId = ImageId(
                        value = 1,
                    ),
                    rawLargeUrl = "",
                    rawThumbnailUrl = "",
                ),
                count = 0,
            ),
            artistImageMetadata,
        )
    }

    @Test
    fun `with Spotify secrets but no Spotify link`() = runTest {
        val artistImageRepository = createArtistImageRepository(
            spotifyOAuthInfo = SpotifyOAuthInfo(
                clientId = "id",
                clientSecret = "secret",
            ),
            wikidataImage = RawImageMetadata(
                largeUrl = WIKIMEDIA_LARGE_URL,
                thumbnailUrl = WIKIMEDIA_THUMBNAIL_URL,
                source = ImageSource.WIKIMEDIA,
            ),
            spotifyApi = FakeSpotifyApi(),
        )

        val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
            detailsModel = artistDetailsModel.copy(
                urls = persistentListOf(),
            ),
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata.Wikimedia(
                    imageId = ImageId(
                        value = 1,
                    ),
                    rawLargeUrl = WIKIMEDIA_LARGE_URL,
                    rawThumbnailUrl = WIKIMEDIA_THUMBNAIL_URL,
                ),
                count = 1,
            ),
            artistImageMetadata,
        )
    }

    @Test
    fun `no Spotify secrets`() {
        runTest {
            val artistImageRepository = createArtistImageRepository(
                spotifyOAuthInfo = SpotifyOAuthInfo(
                    clientId = "",
                    clientSecret = "",
                ),
                wikidataImage = RawImageMetadata(
                    largeUrl = WIKIMEDIA_LARGE_URL,
                    thumbnailUrl = WIKIMEDIA_THUMBNAIL_URL,
                    source = ImageSource.WIKIMEDIA,
                ),
                spotifyApi = FakeSpotifyApi(),
            )

            val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
                detailsModel = artistDetailsModel,
                forceRefresh = false,
            )
            assertEquals(
                ImageMetadataWithCount(
                    imageMetadata = ImageMetadata.Wikimedia(
                        imageId = ImageId(
                            value = 1,
                        ),
                        rawLargeUrl = WIKIMEDIA_LARGE_URL,
                        rawThumbnailUrl = WIKIMEDIA_THUMBNAIL_URL,
                    ),
                    count = 1,
                ),
                artistImageMetadata,
            )
        }
    }

    @Test
    fun `no Spotify secrets and no wikidata images`() = runTest {
        val artistImageRepository = createArtistImageRepository(
            spotifyOAuthInfo = SpotifyOAuthInfo(
                clientId = "",
                clientSecret = "",
            ),
            wikidataImage = RawImageMetadata(
                largeUrl = "",
                thumbnailUrl = "",
                source = ImageSource.WIKIMEDIA,
            ),
            spotifyApi = FakeSpotifyApi(),
        )

        val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
            detailsModel = artistDetailsModel,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata.Wikimedia(
                    imageId = ImageId(
                        value = 1,
                    ),
                ),
                count = 0,
            ),
            artistImageMetadata,
        )
    }
}

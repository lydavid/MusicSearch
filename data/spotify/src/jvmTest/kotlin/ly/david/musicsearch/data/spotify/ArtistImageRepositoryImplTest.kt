package ly.david.musicsearch.data.spotify

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeSpotifyApi
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.spotify.auth.SpotifyOAuthInfo
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.wikimedia.WikimediaRepository
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

private const val LARGE_URL = "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/Radwinps2016.jpg"
private const val THUMBNAIL_URL =
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
        wikidataImage: ImageMetadata,
    ) = ArtistImageRepositoryImpl(
        spotifyOAuthInfo = spotifyOAuthInfo,
        spotifyApi = FakeSpotifyApi(),
        wikimediaRepository = object : WikimediaRepository {
            override suspend fun getWikipediaExtract(
                mbid: String,
                urls: List<RelationListItemModel>,
                languageTag: String,
                forceRefresh: Boolean,
            ): Result<WikipediaExtract> {
                error("Never called")
            }

            override suspend fun getWikimediaImage(urls: List<RelationListItemModel>): ImageMetadata {
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
    fun `with Spotify secrets`() = runTest {
        val artistImageRepository = createArtistImageRepository(
            spotifyOAuthInfo = SpotifyOAuthInfo(
                clientId = "id",
                clientSecret = "secret",
            ),
            wikidataImage = ImageMetadata(
                largeUrl = LARGE_URL,
                thumbnailUrl = THUMBNAIL_URL,
            ),
        )

        val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
            detailsModel = artistDetailsModel,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(
                        value = 1,
                    ),
                    largeUrl = "i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
                    thumbnailUrl = "i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228",
                ),
                count = 1,
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
            wikidataImage = ImageMetadata(
                largeUrl = LARGE_URL,
                thumbnailUrl = THUMBNAIL_URL,
            ),
        )

        val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
            detailsModel = artistDetailsModel.copy(
                urls = persistentListOf(),
            ),
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
                    imageId = ImageId(
                        value = 1,
                    ),
                    largeUrl = LARGE_URL,
                    thumbnailUrl = THUMBNAIL_URL,
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
                wikidataImage = ImageMetadata(
                    largeUrl = LARGE_URL,
                    thumbnailUrl = THUMBNAIL_URL,
                ),
            )

            val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
                detailsModel = artistDetailsModel,
                forceRefresh = false,
            )
            assertEquals(
                ImageMetadataWithCount(
                    imageMetadata = ImageMetadata(
                        imageId = ImageId(
                            value = 1,
                        ),
                        largeUrl = LARGE_URL,
                        thumbnailUrl = THUMBNAIL_URL,
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
            wikidataImage = ImageMetadata(),
        )

        val artistImageMetadata = artistImageRepository.getArtistImageMetadata(
            detailsModel = artistDetailsModel,
            forceRefresh = false,
        )
        assertEquals(
            ImageMetadataWithCount(
                imageMetadata = ImageMetadata(
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

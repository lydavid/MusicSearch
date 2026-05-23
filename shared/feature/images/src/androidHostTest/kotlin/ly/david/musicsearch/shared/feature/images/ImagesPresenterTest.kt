package ly.david.musicsearch.shared.feature.images

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.preferences.NoOpAppPreferences
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithEntity
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrlImpl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.SettingsScreen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImagesPresenterTest {

    private val navigator = FakeNavigator(
        root = SettingsScreen,
    )

    private fun createPresenter(
        imageMetadataList: List<ImageMetadataWithEntity>,
    ) = ImagesPresenter(
        screen = CoverArtsScreen(),
        navigator = navigator,
        appPreferences = object : NoOpAppPreferences() {
            override val imagesSortOption: Flow<ImagesSortOption>
                get() = flowOf(ImagesSortOption.RECENTLY_ADDED)
        },
        musicBrainzImageMetadataRepository = object : MusicBrainzImageMetadataRepository {
            override suspend fun getAndSaveImageMetadata(
                mbid: String,
                entity: MusicBrainzEntityType,
                forceRefresh: Boolean,
            ): ImageMetadataWithCount? {
                return null
            }

            override suspend fun saveImageMetadata(
                mbid: String,
                entity: MusicBrainzEntityType,
                itemsCount: Int,
            ) {
                // No-op
            }

            override fun observeAllImageMetadata(
                mbid: String?,
                query: String,
                sortOption: ImagesSortOption,
            ): Flow<PagingData<ImageMetadataWithEntity>> {
                return flowOf(PagingData.from(imageMetadataList))
            }

            override fun observeCountOfAllImageMetadata(): Flow<Long> {
                error("Not used")
            }
        },
        getMusicBrainzUrl = GetMusicBrainzUrlImpl(
            musicbrainzRepository = object : MusicbrainzRepository {
                override fun getBaseUrl(): String {
                    return "https://custommusicbrainz.org"
                }
            },
        ),
    )

    @Test
    fun empty() = runTest {
        val presenter = createPresenter(
            imageMetadataList = listOf(),
        )

        presenterTestOf({ presenter.present() }) {
            val state = awaitItem()
            assertEquals(listOf<ImageMetadataWithEntity>(), state.imageMetadataPagingDataFlow.asSnapshot())
        }
    }

    @Test
    fun simple() = runTest {
        val imageMetadataWithEntity = ImageMetadataWithEntity(
            imageMetadata = ImageMetadata.InternetArchive(
                imageId = ImageId(1L),
                rawThumbnailUrl = "a",
                rawLargeUrl = "b",
            ),
            musicBrainzEntity = MusicBrainzEntity(
                id = "r",
                type = MusicBrainzEntityType.RELEASE_GROUP,
            ),
        )
        val presenter = createPresenter(
            imageMetadataList = listOf(
                imageMetadataWithEntity,
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    imageMetadataWithEntity,
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals(ImagesTitle.All, state.title)
            assertEquals("", state.subtitle)
            assertEquals("", state.linkUrl)

            state.eventSink(
                ImagesUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)
            assertEquals(ImagesTitle.All, state.title)
            assertEquals("", state.subtitle)
            assertEquals("", state.linkUrl)

            state = awaitItem()
            assertEquals(
                imageMetadataWithEntity,
                state.selectedImageMetadata,
            )
            assertEquals(ImagesTitle.Selected(page = 1, totalPages = 1, typeAndComment = ""), state.title)
            assertEquals("", state.subtitle)
            assertEquals("https://b", state.linkUrl)
        }
    }

    @Test
    fun `with types and comment`() = runTest {
        val imageMetadataWithEntity = ImageMetadataWithEntity(
            imageMetadata = ImageMetadata.InternetArchive(
                imageId = ImageId(1L),
                rawThumbnailUrl = "a",
                rawLargeUrl = "b",
                types = persistentListOf("Booklet"),
                comment = "p. 14-15",
            ),
            musicBrainzEntity = MusicBrainzEntity(
                id = "r",
                type = MusicBrainzEntityType.RELEASE,
            ),
        )
        val presenter = createPresenter(
            imageMetadataList = listOf(
                imageMetadataWithEntity,
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    imageMetadataWithEntity,
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals(ImagesTitle.All, state.title)
            assertEquals("", state.subtitle)
            assertEquals("", state.linkUrl)

            state.eventSink(
                ImagesUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)

            state = awaitItem()
            assertEquals(
                imageMetadataWithEntity,
                state.selectedImageMetadata,
            )
            assertEquals(
                ImagesTitle.Selected(page = 1, totalPages = 1, typeAndComment = "Booklet (p. 14-15)"),
                state.title,
            )
            assertEquals("", state.subtitle)
            assertEquals("https://b", state.linkUrl)
        }
    }

    @Test
    fun `with mbid, name, and entity`() = runTest {
        val imageMetadataWithEntity = ImageMetadataWithEntity(
            imageMetadata = ImageMetadata.InternetArchive(
                imageId = ImageId(1L),
                rawThumbnailUrl = "a",
                rawLargeUrl = "b",
                types = persistentListOf("Booklet"),
                comment = "p. 14-15",
            ),
            musicBrainzEntity = MusicBrainzEntity(
                id = "c",
                type = MusicBrainzEntityType.RELEASE,
            ),
            name = "Release name",
            disambiguation = "that one",
        )
        val presenter = createPresenter(
            imageMetadataList = listOf(
                imageMetadataWithEntity,
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    imageMetadataWithEntity,
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals(ImagesTitle.All, state.title)
            assertEquals("", state.subtitle)
            assertEquals("", state.linkUrl)

            state.eventSink(
                ImagesUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)

            state = awaitItem()
            assertEquals(
                imageMetadataWithEntity,
                state.selectedImageMetadata,
            )
            assertEquals(
                ImagesTitle.Selected(page = 1, totalPages = 1, typeAndComment = "Booklet (p. 14-15)"),
                state.title,
            )
            assertEquals("Release name (that one)", state.subtitle)
            assertEquals("https://b", state.linkUrl)
        }
    }

    @Test
    fun `change page`() = runTest {
        val releaseImageMetadataWithEntity = ImageMetadataWithEntity(
            imageMetadata = ImageMetadata.InternetArchive(
                imageId = ImageId(1L),
                rawThumbnailUrl = "a",
                rawLargeUrl = "re",
                types = persistentListOf("Booklet"),
                comment = "p. 14-15",
            ),
            musicBrainzEntity = MusicBrainzEntity(
                id = "c",
                type = MusicBrainzEntityType.RELEASE,
            ),
            name = "Release name",
        )
        val eventImageMetadataWithEntity = ImageMetadataWithEntity(
            imageMetadata = ImageMetadata.InternetArchive(
                imageId = ImageId(2L),
                rawThumbnailUrl = "a",
                rawLargeUrl = "e",
            ),
            musicBrainzEntity = MusicBrainzEntity(
                id = "c",
                type = MusicBrainzEntityType.EVENT,
            ),
            name = "Event name",
        )
        val artistImageMetadataWithEntity = ImageMetadataWithEntity(
            imageMetadata = ImageMetadata.Wikimedia(
                imageId = ImageId(3L),
                rawThumbnailUrl = "a",
                rawLargeUrl = "art",
            ),
            musicBrainzEntity = MusicBrainzEntity(
                id = "c",
                type = MusicBrainzEntityType.ARTIST,
            ),
            name = "Artist name",
        )
        val presenter = createPresenter(
            imageMetadataList = listOf(
                releaseImageMetadataWithEntity,
                eventImageMetadataWithEntity,
                artistImageMetadataWithEntity,
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    releaseImageMetadataWithEntity,
                    eventImageMetadataWithEntity,
                    artistImageMetadataWithEntity,
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals(ImagesTitle.All, state.title)
            assertEquals("", state.subtitle)

            state.eventSink(
                ImagesUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)

            state = awaitItem()
            assertEquals(
                releaseImageMetadataWithEntity,
                state.selectedImageMetadata,
            )
            assertEquals(
                ImagesTitle.Selected(page = 1, totalPages = 3, typeAndComment = "Booklet (p. 14-15)"),
                state.title,
            )
            assertEquals("Release name", state.subtitle)
            assertEquals("https://re", state.linkUrl)

            state.eventSink(
                ImagesUiEvent.SelectImage(
                    index = 1,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(1, state.selectedImageIndex)
            assertEquals(
                eventImageMetadataWithEntity,
                state.selectedImageMetadata,
            )
            assertEquals(
                ImagesTitle.Selected(page = 2, totalPages = 3, typeAndComment = ""),
                state.title,
            )
            assertEquals("Event name", state.subtitle)
            assertEquals("https://e", state.linkUrl)

            state.eventSink(
                ImagesUiEvent.SelectImage(
                    index = 2,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(2, state.selectedImageIndex)
            assertEquals(
                artistImageMetadataWithEntity,
                state.selectedImageMetadata,
            )
            assertEquals(
                ImagesTitle.Selected(page = 3, totalPages = 3, typeAndComment = ""),
                state.title,
            )
            assertEquals("Artist name", state.subtitle)
            assertEquals("https://commons.wikimedia.org/wiki/File:art", state.linkUrl)

            state.eventSink(
                ImagesUiEvent.NavigateUp,
            )
            state = awaitItem()
            assertEquals(null, state.selectedImageIndex)
            assertEquals(null, state.selectedImageMetadata)
            assertEquals(ImagesTitle.All, state.title)
            assertEquals("", state.subtitle)
            assertEquals("", state.linkUrl)

            state.eventSink(
                ImagesUiEvent.NavigateUp,
            )
            assertEquals(FakeNavigator.PopEvent(poppedScreen = null), navigator.awaitPop())
        }
    }
}

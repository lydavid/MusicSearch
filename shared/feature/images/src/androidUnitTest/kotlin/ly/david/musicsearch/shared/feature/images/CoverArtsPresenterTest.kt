package ly.david.musicsearch.shared.feature.images

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.preferences.NoOpAppPreferences
import ly.david.musicsearch.shared.domain.coverarts.CoverArtsSortOption
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzCoverArtUrl
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository
import ly.david.musicsearch.ui.common.screen.CoverArtsScreen
import ly.david.musicsearch.ui.common.screen.SettingsScreen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CoverArtsPresenterTest {

    private val navigator = FakeNavigator(
        root = SettingsScreen,
    )

    private fun createPresenter(
        imageMetadataList: List<ImageMetadata>,
    ) = CoverArtsPresenter(
        screen = CoverArtsScreen(),
        navigator = navigator,
        appPreferences = object : NoOpAppPreferences() {
            override val coverArtsSortOption: Flow<CoverArtsSortOption>
                get() = flowOf(CoverArtsSortOption.RECENTLY_ADDED)
        },
        releaseImageRepository = object : ReleaseImageRepository {
            override suspend fun getReleaseImageMetadata(
                releaseId: String,
                forceRefresh: Boolean,
            ): ImageMetadata {
                return ImageMetadata()
            }

            override fun observeAllImageMetadata(
                mbid: String?,
                query: String,
                sortOption: CoverArtsSortOption,
            ): Flow<PagingData<ImageMetadata>> {
                return flowOf(PagingData.from(imageMetadataList))
            }

            override fun getNumberOfImagesById(mbid: String): Int {
                return imageMetadataList.size
            }
        },
        getMusicBrainzCoverArtUrl = GetMusicBrainzCoverArtUrl(
            getMusicBrainzUrl = object : GetMusicBrainzUrl {
                override fun invoke(
                    entity: MusicBrainzEntity,
                    entityId: String,
                ): String {
                    return ""
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
            assertEquals(listOf<ImageMetadata>(), state.imageMetadataPagingDataFlow.asSnapshot())
        }
    }

    @Test
    fun simple() = runTest {
        val presenter = createPresenter(
            imageMetadataList = listOf(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                ),
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 1,
                        thumbnailUrl = "a",
                        largeUrl = "b",
                    ),
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals("Cover arts", state.title)

            state.eventSink(
                CoverArtsUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)

            state = awaitItem()
            assertEquals(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                ),
                state.selectedImageMetadata,
            )
            assertEquals("[1/1]", state.title)
        }
    }

    @Test
    fun `with types and comment`() = runTest {
        val presenter = createPresenter(
            imageMetadataList = listOf(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    types = persistentListOf("Booklet"),
                    comment = "p. 14-15",
                ),
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 1,
                        thumbnailUrl = "a",
                        largeUrl = "b",
                        types = persistentListOf("Booklet"),
                        comment = "p. 14-15",
                    ),
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals("Cover arts", state.title)
            assertEquals("", state.subtitle)

            state.eventSink(
                CoverArtsUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)

            state = awaitItem()
            assertEquals(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    types = persistentListOf("Booklet"),
                    comment = "p. 14-15",
                ),
                state.selectedImageMetadata,
            )
            assertEquals("[1/1] (Booklet (p. 14-15))", state.title)
            assertEquals("", state.subtitle)
        }
    }

    @Test
    fun `with mbid, name, and entity`() = runTest {
        val presenter = createPresenter(
            imageMetadataList = listOf(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    types = persistentListOf("Booklet"),
                    comment = "p. 14-15",
                    mbid = "c",
                    name = "Release name",
                    entity = MusicBrainzEntity.RELEASE,
                ),
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 1,
                        thumbnailUrl = "a",
                        largeUrl = "b",
                        types = persistentListOf("Booklet"),
                        comment = "p. 14-15",
                        mbid = "c",
                        name = "Release name",
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals("Cover arts", state.title)
            assertEquals("", state.subtitle)

            state.eventSink(
                CoverArtsUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)

            state = awaitItem()
            assertEquals(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    types = persistentListOf("Booklet"),
                    comment = "p. 14-15",
                    mbid = "c",
                    name = "Release name",
                    entity = MusicBrainzEntity.RELEASE,
                ),
                state.selectedImageMetadata,
            )
            assertEquals("[1/1] (Booklet (p. 14-15))", state.title)
            assertEquals("Release name", state.subtitle)
        }
    }

    @Test
    fun `change page`() = runTest {
        val presenter = createPresenter(
            imageMetadataList = listOf(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    types = persistentListOf("Booklet"),
                    comment = "p. 14-15",
                    mbid = "c",
                    name = "Release name",
                    entity = MusicBrainzEntity.RELEASE,
                ),
                ImageMetadata(
                    databaseId = 2,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    mbid = "c",
                    name = "Release group name",
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                ),
                ImageMetadata(
                    databaseId = 3,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    mbid = "c",
                    name = "Artist name",
                    entity = MusicBrainzEntity.ARTIST,
                ),
            ),
        )

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            val snapshot = state.imageMetadataPagingDataFlow.asSnapshot()
            assertEquals(
                listOf(
                    ImageMetadata(
                        databaseId = 1,
                        thumbnailUrl = "a",
                        largeUrl = "b",
                        types = persistentListOf("Booklet"),
                        comment = "p. 14-15",
                        mbid = "c",
                        name = "Release name",
                        entity = MusicBrainzEntity.RELEASE,
                    ),
                    ImageMetadata(
                        databaseId = 2,
                        thumbnailUrl = "a",
                        largeUrl = "b",
                        mbid = "c",
                        name = "Release group name",
                        entity = MusicBrainzEntity.RELEASE_GROUP,
                    ),
                    ImageMetadata(
                        databaseId = 3,
                        thumbnailUrl = "a",
                        largeUrl = "b",
                        mbid = "c",
                        name = "Artist name",
                        entity = MusicBrainzEntity.ARTIST,
                    ),
                ),
                snapshot,
            )
            assertEquals(null, state.selectedImageIndex)
            assertEquals("Cover arts", state.title)
            assertEquals("", state.subtitle)

            state.eventSink(
                CoverArtsUiEvent.SelectImage(
                    index = 0,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(0, state.selectedImageIndex)

            state = awaitItem()
            assertEquals(
                ImageMetadata(
                    databaseId = 1,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    types = persistentListOf("Booklet"),
                    comment = "p. 14-15",
                    mbid = "c",
                    name = "Release name",
                    entity = MusicBrainzEntity.RELEASE,
                ),
                state.selectedImageMetadata,
            )
            assertEquals("[1/3] (Booklet (p. 14-15))", state.title)
            assertEquals("Release name", state.subtitle)

            state.eventSink(
                CoverArtsUiEvent.SelectImage(
                    index = 1,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(1, state.selectedImageIndex)
            assertEquals(
                ImageMetadata(
                    databaseId = 2,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    mbid = "c",
                    name = "Release group name",
                    entity = MusicBrainzEntity.RELEASE_GROUP,
                ),
                state.selectedImageMetadata,
            )
            assertEquals("[2/3]", state.title)
            assertEquals("Release group name", state.subtitle)

            state.eventSink(
                CoverArtsUiEvent.SelectImage(
                    index = 2,
                    imageMetadataSnapshot = snapshot,
                ),
            )
            state = awaitItem()
            assertEquals(2, state.selectedImageIndex)
            assertEquals(
                ImageMetadata(
                    databaseId = 3,
                    thumbnailUrl = "a",
                    largeUrl = "b",
                    mbid = "c",
                    name = "Artist name",
                    entity = MusicBrainzEntity.ARTIST,
                ),
                state.selectedImageMetadata,
            )
            assertEquals("[3/3]", state.title)
            assertEquals("Artist name", state.subtitle)

            state.eventSink(
                CoverArtsUiEvent.NavigateUp,
            )
            state = awaitItem()
            assertEquals(null, state.selectedImageIndex)
            assertEquals(null, state.selectedImageMetadata)
            assertEquals("Cover arts", state.title)
            assertEquals("", state.subtitle)

            state.eventSink(
                CoverArtsUiEvent.NavigateUp,
            )
            assertEquals(FakeNavigator.PopEvent(poppedScreen = null), navigator.awaitPop())
        }
    }
}

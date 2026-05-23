package ly.david.musicsearch.shared.feature.details.tag

import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import ly.david.data.test.clock.FixedClock
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagRepository
import ly.david.musicsearch.shared.domain.tag.VoteType
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.screen.SnackbarPopResult
import ly.david.musicsearch.ui.common.screen.TagDetailsScreen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Instant

@RunWith(RobolectricTestRunner::class)
class TagDetailsPresenterTest {
    private val now = Instant.parse("1970-01-02T05:00:00Z")

    private val tagRepository: TagRepository = mockk()

    private val genre = GenreChip(
        id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
        name = "art rock",
        count = 22,
    )
    private val screen = TagDetailsScreen(
        entity = MusicBrainzEntity(
            type = MusicBrainzEntityType.ARTIST,
            id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
        ),
        genreOrTag = genre,
    )
    private val navigator = FakeNavigator(
        root = screen,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createPresenter(
        screen: TagDetailsScreen,
        navigator: Navigator,
    ) = TagDetailsPresenter(
        screen = screen,
        navigator = navigator,
        tagRepository = tagRepository,
        coroutineScope = TestScope(context = UnconfinedTestDispatcher()),
        clock = FixedClock(now = now),
    )

    @Test
    fun `smoke test`() = runTest {
        val presenter = createPresenter(
            screen = screen,
            navigator = navigator,
        )

        presenterTestOf({ presenter.present() }) {
            val state = awaitItem()
            assertEquals(
                genre,
                state.genreOrTag,
            )

            state.eventSink(TagDetailsUiEvent.SearchGenreOrTag(name = genre.name))
            assertEquals(
                FakeNavigator.PopEvent(
                    poppedScreen = null,
                    result = SnackbarPopResult(
                        feedback = SearchScreen(
                            query = "tag:\"art rock\"",
                            entityType = MusicBrainzEntityType.ARTIST,
                        ),
                    ),
                ),
                navigator.awaitPop(),
            )

            state.eventSink(TagDetailsUiEvent.GoToGenre(id = genre.id))
            assertEquals(
                FakeNavigator.PopEvent(
                    poppedScreen = null,
                    result = SnackbarPopResult(
                        feedback = DetailsScreen(
                            id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                            entityType = MusicBrainzEntityType.GENRE,
                        ),
                    ),
                ),
                navigator.awaitPop(),
            )
        }
    }

    @Test
    fun upvote() = runTest {
        val presenter = createPresenter(
            screen = screen,
            navigator = navigator,
        )

        coEvery {
            tagRepository.voteOnTagForEntity(
                genreOrTag = any(),
                musicBrainzEntity = any(),
                voteType = any(),
            )
        } returns flow {
            emit(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
            )
        }

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            assertEquals(
                genre,
                state.genreOrTag,
            )

            state.eventSink(TagDetailsUiEvent.Vote(voteType = VoteType.Upvote))
            state = awaitItem()
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                state.feedback,
            )

            state = awaitItem()
            assertEquals(
                genre.copy(
                    count = 23,
                    voteType = VoteType.Upvote,
                ),
                state.genreOrTag,
            )
        }
    }

    @Test
    fun downvote() = runTest {
        val presenter = createPresenter(
            screen = screen,
            navigator = navigator,
        )

        coEvery {
            tagRepository.voteOnTagForEntity(
                genreOrTag = any(),
                musicBrainzEntity = any(),
                voteType = any(),
            )
        } returns flow {
            emit(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
            )
        }

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            assertEquals(
                genre,
                state.genreOrTag,
            )

            state.eventSink(TagDetailsUiEvent.Vote(voteType = VoteType.Downvote))
            state = awaitItem()
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                state.feedback,
            )

            state = awaitItem()
            assertEquals(
                genre.copy(
                    count = 21,
                    voteType = VoteType.Downvote,
                ),
                state.genreOrTag,
            )
        }
    }

    @Test
    fun `withdraw upvote`() = runTest {
        val modifiedGenre = genre.copy(
            count = 23,
            voteType = VoteType.Upvote,
        )
        val presenter = createPresenter(
            screen = screen.copy(
                genreOrTag = modifiedGenre,
            ),
            navigator = navigator,
        )

        coEvery {
            tagRepository.voteOnTagForEntity(
                genreOrTag = any(),
                musicBrainzEntity = any(),
                voteType = any(),
            )
        } returns flow {
            emit(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
            )
        }

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            assertEquals(
                modifiedGenre,
                state.genreOrTag,
            )

            state.eventSink(TagDetailsUiEvent.Vote(voteType = VoteType.Withdraw))
            state = awaitItem()
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                state.feedback,
            )

            state = awaitItem()
            assertEquals(
                genre.copy(
                    count = 22,
                    voteType = VoteType.Withdraw,
                ),
                state.genreOrTag,
            )
        }
    }

    @Test
    fun `withdraw downvote`() = runTest {
        val modifiedGenre = genre.copy(
            count = 21,
            voteType = VoteType.Downvote,
        )
        val presenter = createPresenter(
            screen = screen.copy(
                genreOrTag = modifiedGenre,
            ),
            navigator = navigator,
        )

        coEvery {
            tagRepository.voteOnTagForEntity(
                genreOrTag = any(),
                musicBrainzEntity = any(),
                voteType = any(),
            )
        } returns flow {
            emit(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
            )
        }

        presenterTestOf({ presenter.present() }) {
            var state = awaitItem()
            assertEquals(
                modifiedGenre,
                state.genreOrTag,
            )

            state.eventSink(TagDetailsUiEvent.Vote(voteType = VoteType.Withdraw))
            state = awaitItem()
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                state.feedback,
            )

            state = awaitItem()
            assertEquals(
                genre.copy(
                    count = 22,
                    voteType = VoteType.Withdraw,
                ),
                state.genreOrTag,
            )
        }
    }
}

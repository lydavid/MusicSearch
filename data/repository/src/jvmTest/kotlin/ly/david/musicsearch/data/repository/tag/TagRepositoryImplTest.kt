package ly.david.musicsearch.data.repository.tag

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.clock.FixedClock
import ly.david.data.test.davidBowieArtistMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.musicbrainz.api.TagApi
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.ArtistGender
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.artist.ArtistType
import ly.david.musicsearch.shared.domain.auth.MusicBrainzAuthStore
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.error.Action
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.domain.tag.TagRepository
import ly.david.musicsearch.shared.domain.tag.VoteType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class TagRepositoryImplTest : KoinTest, TestArtistRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val artistDao: ArtistDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val areaDao: AreaDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val aliasDao: AliasDao by inject()
    override val tagDao: TagDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()

    private val tagApi: TagApi = mockk()
    private val musicBrainzAuthStore: MusicBrainzAuthStore = mockk()
    private val now = testDateTimeInThePast

    private val detailsModel = ArtistDetailsModel(
        id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
        name = "David Bowie",
        type = ArtistType.PERSON,
        gender = ArtistGender.MALE,
        lifeSpan = LifeSpanUiModel(
            begin = "1947-01-08",
            end = "2016-01-10",
            ended = true,
        ),
        sortName = "Bowie, David",
        areaListItemModel = AreaListItemModel(
            id = "9d5dd675-3cf4-4296-9e39-67865ebee758",
            name = "England",
            countryCodes = persistentListOf(),
        ),
        ipis = persistentListOf("00003960406", "00015471209"),
        isnis = persistentListOf("0000000114448576", "0000000458257298"),
        lastUpdated = testDateTimeInThePast,
        listenBrainzUrl = "/artist/5441c29d-3602-4898-b1a1-b77fa23b8e50",
        genres = persistentListOf(
            GenreChip(
                id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                name = "art rock",
                count = 22,
            ),
            GenreChip(
                id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                name = "glam rock",
                count = 22,
            ),
        ),
        tags = persistentListOf(
            TagChip(
                name = "some spam tag",
                count = 99,
            ),
            TagChip(
                name = "british",
                count = 6,
            ),
            TagChip(
                name = "uk",
                count = 6,
            ),
        ),
    )

    private val artistRepositoryImpl: ArtistRepository
        get() = createArtistRepository(
            artistMusicBrainzModel = davidBowieArtistMusicBrainzModel.copy(
                sortName = "Bowie, David",
                area = AreaMusicBrainzNetworkModel(
                    id = "9d5dd675-3cf4-4296-9e39-67865ebee758",
                    name = "England",
                    countrySubDivisionCodes = listOf("GB-ENG"),
                ),
                ipis = listOf("00003960406", "00015471209"),
                isnis = listOf("0000000114448576", "0000000458257298"),
                relations = null,
            ),
        )

    @Test
    fun `try to vote - not logged in`() = runTest {
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = now,
        ).run {
            assertEquals(
                detailsModel,
                this,
            )
        }

        val tagRepository = TagRepositoryImpl(
            tagApi = tagApi,
            tagDao = tagDao,
            musicBrainzAuthStore = musicBrainzAuthStore,
            clock = FixedClock(now = now),
        )
        coEvery { tagApi.postUserTags(any(), any(), any()) } throws HandledException(
            userMessage = "",
            errorType = ErrorType.Unauthorized,
            errorResolution = ErrorResolution.Login,
        )
        coEvery { musicBrainzAuthStore.getAccessToken() } returns null

        tagRepository.voteOnTagForEntity(
            genreOrTag = TagChip(
                name = "some spam tag",
                count = 99,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Downvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Error(
                    data = TagRepository.TagFeedback.FailedToVote(
                        name = "some spam tag",
                        errorMessage = "",
                    ),
                    errorResolution = ErrorResolution.Login,
                    action = Action.Login,
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    tags = persistentListOf(
                        TagChip(
                            name = "some spam tag",
                            count = 99,
                        ),
                        TagChip(
                            name = "british",
                            count = 6,
                        ),
                        TagChip(
                            name = "uk",
                            count = 6,
                        ),
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `try to vote - logged in but without tag scope`() = runTest {
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = now,
        ).run {
            assertEquals(
                detailsModel,
                this,
            )
        }

        val tagRepository = TagRepositoryImpl(
            tagApi = tagApi,
            tagDao = tagDao,
            musicBrainzAuthStore = musicBrainzAuthStore,
            clock = FixedClock(now = now),
        )
        coEvery { tagApi.postUserTags(any(), any(), any()) } throws HandledException(
            userMessage = "",
            errorType = ErrorType.Unauthorized,
            errorResolution = ErrorResolution.Login,
        )
        coEvery { musicBrainzAuthStore.getAccessToken() } returns "some-token"

        tagRepository.voteOnTagForEntity(
            genreOrTag = TagChip(
                name = "some spam tag",
                count = 99,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Downvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Error(
                    data = TagRepository.TagFeedback.AuthMissingTagScope(
                        name = "some spam tag",
                    ),
                    errorResolution = ErrorResolution.Login,
                    action = Action.Login,
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    tags = persistentListOf(
                        TagChip(
                            name = "some spam tag",
                            count = 99,
                        ),
                        TagChip(
                            name = "british",
                            count = 6,
                        ),
                        TagChip(
                            name = "uk",
                            count = 6,
                        ),
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `try to vote - other error`() = runTest {
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = now,
        ).run {
            assertEquals(
                detailsModel,
                this,
            )
        }

        val tagRepository = TagRepositoryImpl(
            tagApi = tagApi,
            tagDao = tagDao,
            musicBrainzAuthStore = musicBrainzAuthStore,
            clock = FixedClock(now = now),
        )
        coEvery { tagApi.postUserTags(any(), any(), any()) } throws HandledException(
            userMessage = "other error",
            errorType = ErrorType.InternalServerError,
        )
        coEvery { musicBrainzAuthStore.getAccessToken() } returns "some-token"

        tagRepository.voteOnTagForEntity(
            genreOrTag = TagChip(
                name = "some spam tag",
                count = 99,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Downvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Error(
                    data = TagRepository.TagFeedback.FailedToVote(
                        name = "some spam tag",
                        errorMessage = "other error",
                    ),
                    errorResolution = ErrorResolution.Unknown,
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    tags = persistentListOf(
                        TagChip(
                            name = "some spam tag",
                            count = 99,
                        ),
                        TagChip(
                            name = "british",
                            count = 6,
                        ),
                        TagChip(
                            name = "uk",
                            count = 6,
                        ),
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `downvote, then withdraw`() = runTest {
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = now,
        ).run {
            assertEquals(
                detailsModel,
                this,
            )
        }

        val tagRepository = TagRepositoryImpl(
            tagApi = tagApi,
            tagDao = tagDao,
            musicBrainzAuthStore = musicBrainzAuthStore,
            clock = FixedClock(now = now),
        )
        coEvery { tagApi.postUserTags(any(), any(), any()) } returns Unit
        coEvery { musicBrainzAuthStore.getAccessToken() } returns "some-token"

        tagRepository.voteOnTagForEntity(
            genreOrTag = TagChip(
                name = "some spam tag",
                count = 99,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Downvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "some spam tag",
                        voteType = VoteType.Downvote,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    tags = persistentListOf(
                        TagChip(
                            name = "british",
                            count = 6,
                        ),
                        TagChip(
                            name = "uk",
                            count = 6,
                        ),
                        TagChip(
                            name = "some spam tag",
                            count = 98,
                            voteType = VoteType.Downvote,
                        ),
                    ),
                ),
                this,
            )
        }

        tagRepository.voteOnTagForEntity(
            genreOrTag = TagChip(
                name = "some spam tag",
                count = 98,
                voteType = VoteType.Downvote,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Withdraw,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "some spam tag",
                        voteType = VoteType.Withdraw,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    tags = persistentListOf(
                        TagChip(
                            name = "some spam tag",
                            count = 99,
                        ),
                        TagChip(
                            name = "british",
                            count = 6,
                        ),
                        TagChip(
                            name = "uk",
                            count = 6,
                        ),
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `upvote, then withdraw`() = runTest {
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = now,
        ).run {
            assertEquals(
                detailsModel,
                this,
            )
        }

        val tagRepository = TagRepositoryImpl(
            tagApi = tagApi,
            tagDao = tagDao,
            musicBrainzAuthStore = musicBrainzAuthStore,
            clock = FixedClock(now = now),
        )
        coEvery { tagApi.postUserTags(any(), any(), any()) } returns Unit
        coEvery { musicBrainzAuthStore.getAccessToken() } returns "some-token"

        tagRepository.voteOnTagForEntity(
            genreOrTag = GenreChip(
                id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                name = "glam rock",
                count = 22,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Upvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "glam rock",
                        voteType = VoteType.Upvote,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    genres = persistentListOf(
                        GenreChip(
                            id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                            name = "glam rock",
                            count = 23,
                            voteType = VoteType.Upvote,
                        ),
                        GenreChip(
                            id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                            name = "art rock",
                            count = 22,
                        ),
                    ),
                ),
                this,
            )
        }

        tagRepository.voteOnTagForEntity(
            genreOrTag = GenreChip(
                id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                name = "glam rock",
                count = 23,
                voteType = VoteType.Upvote,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Withdraw,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "glam rock",
                        voteType = VoteType.Withdraw,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    genres = persistentListOf(
                        GenreChip(
                            id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                            name = "art rock",
                            count = 22,
                        ),
                        GenreChip(
                            id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                            name = "glam rock",
                            count = 22,
                        ),
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `downvote, then upvote`() = runTest {
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = now,
        ).run {
            assertEquals(
                detailsModel,
                this,
            )
        }

        val tagRepository = TagRepositoryImpl(
            tagApi = tagApi,
            tagDao = tagDao,
            musicBrainzAuthStore = musicBrainzAuthStore,
            clock = FixedClock(now = now),
        )
        coEvery { tagApi.postUserTags(any(), any(), any()) } returns Unit
        coEvery { musicBrainzAuthStore.getAccessToken() } returns "some-token"

        tagRepository.voteOnTagForEntity(
            genreOrTag = GenreChip(
                id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                name = "glam rock",
                count = 22,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Downvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "glam rock",
                        voteType = VoteType.Downvote,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    genres = persistentListOf(
                        GenreChip(
                            id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                            name = "art rock",
                            count = 22,
                        ),
                        GenreChip(
                            id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                            name = "glam rock",
                            count = 21,
                            voteType = VoteType.Downvote,
                        ),
                    ),
                ),
                this,
            )
        }

        tagRepository.voteOnTagForEntity(
            genreOrTag = GenreChip(
                id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                name = "glam rock",
                count = 21,
                voteType = VoteType.Downvote,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Upvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "glam rock",
                        voteType = VoteType.Upvote,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    genres = persistentListOf(
                        GenreChip(
                            id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                            name = "glam rock",
                            count = 23,
                            voteType = VoteType.Upvote,
                        ),
                        GenreChip(
                            id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                            name = "art rock",
                            count = 22,
                        ),
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `upvote, then downvote`() = runTest {
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = now,
        ).run {
            assertEquals(
                detailsModel,
                this,
            )
        }

        val tagRepository = TagRepositoryImpl(
            tagApi = tagApi,
            tagDao = tagDao,
            musicBrainzAuthStore = musicBrainzAuthStore,
            clock = FixedClock(now = now),
        )
        coEvery { tagApi.postUserTags(any(), any(), any()) } returns Unit
        coEvery { musicBrainzAuthStore.getAccessToken() } returns "some-token"

        tagRepository.voteOnTagForEntity(
            genreOrTag = GenreChip(
                id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                name = "glam rock",
                count = 22,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Upvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "glam rock",
                        voteType = VoteType.Upvote,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    genres = persistentListOf(
                        GenreChip(
                            id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                            name = "glam rock",
                            count = 23,
                            voteType = VoteType.Upvote,
                        ),
                        GenreChip(
                            id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                            name = "art rock",
                            count = 22,
                        ),
                    ),
                ),
                this,
            )
        }

        tagRepository.voteOnTagForEntity(
            genreOrTag = GenreChip(
                id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                name = "glam rock",
                count = 23,
                voteType = VoteType.Upvote,
            ),
            musicBrainzEntity = MusicBrainzEntity(
                type = MusicBrainzEntityType.ARTIST,
                id = davidBowieArtistMusicBrainzModel.id,
            ),
            voteType = VoteType.Downvote,
        ).test {
            assertEquals(
                Feedback.Loading(
                    data = TagRepository.TagFeedback.Syncing,
                    time = now,
                ),
                awaitItem(),
            )
            assertEquals(
                Feedback.Success(
                    data = TagRepository.TagFeedback.Voted(
                        name = "glam rock",
                        voteType = VoteType.Downvote,
                    ),
                    time = now,
                ),
                awaitItem(),
            )
            awaitComplete()
        }
        artistRepositoryImpl.lookupArtist(
            artistId = davidBowieArtistMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            assertEquals(
                detailsModel.copy(
                    genres = persistentListOf(
                        GenreChip(
                            id = "b7ef058e-6d83-4ca4-8123-9724bff4648b",
                            name = "art rock",
                            count = 22,
                        ),
                        GenreChip(
                            id = "54d89e62-5bfb-42bc-a321-9230e6fdcd75",
                            name = "glam rock",
                            count = 21,
                            voteType = VoteType.Downvote,
                        ),
                    ),
                ),
                this,
            )
        }
    }
}

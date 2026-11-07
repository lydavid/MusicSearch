package ly.david.musicsearch.shared.feature.settings.services

import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.preferences.NoOpAppPreferences
import ly.david.musicsearch.shared.domain.image.ArtistImageSource
import ly.david.musicsearch.shared.domain.preferences.ListenBrainzInstance
import ly.david.musicsearch.shared.domain.preferences.MusicBrainzInstance
import ly.david.musicsearch.shared.domain.preferences.WikidataInstance
import ly.david.musicsearch.shared.feature.settings.internal.services.ServicesSettingsPresenter
import ly.david.musicsearch.shared.feature.settings.internal.services.ServicesSettingsUiEvent
import ly.david.musicsearch.ui.common.screen.ServicesSettingsScreen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ServicesSettingsPresenterTest {

    private val navigator = FakeNavigator(
        root = ServicesSettingsScreen,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createPresenter(
        navigator: Navigator,
        musicBrainzInstance: MusicBrainzInstance,
        listenBrainzInstance: ListenBrainzInstance,
        wikidataInstance: WikidataInstance = WikidataInstance.Default,
        artistImageSource: ArtistImageSource = ArtistImageSource.Wikimedia,
        hasDefaultSpotifyCredentials: Boolean = false,
    ) = ServicesSettingsPresenter(
        navigator = navigator,
        appPreferences = object : NoOpAppPreferences() {
            private val musicBrainzInstanceFlow = MutableStateFlow(musicBrainzInstance)

            override val musicBrainzInstance: Flow<MusicBrainzInstance>
                get() = musicBrainzInstanceFlow

            override fun setMusicBrainzInstance(instance: MusicBrainzInstance) {
                musicBrainzInstanceFlow.value = instance
            }

            private val listenBrainzInstanceFlow = MutableStateFlow(listenBrainzInstance)

            override val listenBrainzInstance: Flow<ListenBrainzInstance>
                get() = listenBrainzInstanceFlow

            override fun setListenBrainzInstance(instance: ListenBrainzInstance) {
                listenBrainzInstanceFlow.value = instance
            }

            private val wikidataInstanceFlow = MutableStateFlow(wikidataInstance)

            override val wikidataInstance: Flow<WikidataInstance>
                get() = wikidataInstanceFlow

            override fun setWikidataInstance(instance: WikidataInstance) {
                wikidataInstanceFlow.value = instance
            }

            private val artistImageSourceFlow = MutableStateFlow(artistImageSource)

            override val artistImageSource: Flow<ArtistImageSource>
                get() = artistImageSourceFlow

            override fun setArtistImageSource(source: ArtistImageSource) {
                artistImageSourceFlow.value = source
            }

            override val hasDefaultSpotifyCredentials: Boolean
                get() = hasDefaultSpotifyCredentials
        },
    )

    @Test
    fun `smoke test`() = runTest {
        val presenter = createPresenter(
            navigator = navigator,
            musicBrainzInstance = MusicBrainzInstance.Default,
            listenBrainzInstance = ListenBrainzInstance.Default,
            wikidataInstance = WikidataInstance.Default,
        )

        presenterTestOf({ presenter.present() }) {
            val state = awaitItem()
            assertEquals(
                MusicBrainzInstance.Default,
                state.musicBrainzInstance,
            )
            assertEquals(
                ListenBrainzInstance.Default,
                state.listenBrainzInstance,
            )
            assertEquals(
                WikidataInstance.Default,
                state.wikidataInstance,
            )

            // no changes when blank
            state.eventSink(
                ServicesSettingsUiEvent.ConfirmMusicBrainzInstance(
                    isCustom = true,
                    url = "",
                ),
            )
            assertEquals(
                MusicBrainzInstance.Default,
                state.musicBrainzInstance,
            )

            state.eventSink(
                ServicesSettingsUiEvent.ConfirmMusicBrainzInstance(
                    isCustom = true,
                    url = "https://musicbrainz.example.com",
                ),
            )
            awaitItem().run {
                assertEquals(
                    MusicBrainzInstance.Custom(
                        url = "https://musicbrainz.example.com",
                    ),
                    musicBrainzInstance,
                )
                assertEquals(
                    ListenBrainzInstance.Default,
                    state.listenBrainzInstance,
                )
            }

            state.eventSink(
                ServicesSettingsUiEvent.ConfirmListenBrainzInstance(
                    isCustom = true,
                    url = "https://listenbrainz.example.com",
                ),
            )
            awaitItem().run {
                assertEquals(
                    MusicBrainzInstance.Custom(
                        url = "https://musicbrainz.example.com",
                    ),
                    musicBrainzInstance,
                )
                assertEquals(
                    ListenBrainzInstance.Custom(
                        url = "https://listenbrainz.example.com",
                    ),
                    listenBrainzInstance,
                )
            }

            state.eventSink(
                ServicesSettingsUiEvent.ConfirmWikimediaInstance(
                    isCustom = true,
                    url = "https://wikidata.example.com",
                ),
            )
            awaitItem().run {
                assertEquals(
                    MusicBrainzInstance.Custom(
                        url = "https://musicbrainz.example.com",
                    ),
                    musicBrainzInstance,
                )
                assertEquals(
                    ListenBrainzInstance.Custom(
                        url = "https://listenbrainz.example.com",
                    ),
                    listenBrainzInstance,
                )
                assertEquals(
                    WikidataInstance.Custom(
                        url = "https://wikidata.example.com",
                    ),
                    wikidataInstance,
                )
            }

            state.eventSink(
                ServicesSettingsUiEvent.Reset,
            )
            awaitItem().run {
                assertEquals(
                    MusicBrainzInstance.Default,
                    musicBrainzInstance,
                )
                assertEquals(
                    ListenBrainzInstance.Custom(
                        url = "https://listenbrainz.example.com",
                    ),
                    listenBrainzInstance,
                )
                assertEquals(
                    WikidataInstance.Custom(
                        url = "https://wikidata.example.com",
                    ),
                    wikidataInstance,
                )
            }
            awaitItem().run {
                assertEquals(
                    MusicBrainzInstance.Default,
                    musicBrainzInstance,
                )
                assertEquals(
                    ListenBrainzInstance.Default,
                    listenBrainzInstance,
                )
                assertEquals(
                    WikidataInstance.Custom(
                        url = "https://wikidata.example.com",
                    ),
                    wikidataInstance,
                )
            }
            awaitItem().run {
                assertEquals(
                    MusicBrainzInstance.Default,
                    musicBrainzInstance,
                )
                assertEquals(
                    ListenBrainzInstance.Default,
                    listenBrainzInstance,
                )
                assertEquals(
                    WikidataInstance.Default,
                    wikidataInstance,
                )
            }

            state.eventSink(ServicesSettingsUiEvent.NavigateUp)
            assertEquals(
                FakeNavigator.PopEvent(
                    poppedScreen = null,
                ),
                navigator.awaitPop(),
            )
        }
    }

    @Test
    fun `confirm artist image source updates state`() = runTest {
        val presenter = createPresenter(
            navigator = navigator,
            musicBrainzInstance = MusicBrainzInstance.Default,
            listenBrainzInstance = ListenBrainzInstance.Default,
            artistImageSource = ArtistImageSource.Wikimedia,
            hasDefaultSpotifyCredentials = true,
        )

        presenterTestOf({ presenter.present() }) {
            val state = awaitItem()
            assertEquals(
                ArtistImageSource.Wikimedia,
                state.artistImageSource,
            )
            assertEquals(
                true,
                state.showDefaultSpotifyOption,
            )

            state.eventSink(
                ServicesSettingsUiEvent.ConfirmArtistImageSource(
                    source = ArtistImageSource.Spotify.Default,
                ),
            )
            awaitItem().run {
                assertEquals(
                    ArtistImageSource.Spotify.Default,
                    artistImageSource,
                )
            }
        }
    }

    @Test
    fun `reset falls back to spotify default artist image source when available`() = runTest {
        val presenter = createPresenter(
            navigator = navigator,
            musicBrainzInstance = MusicBrainzInstance.Default,
            listenBrainzInstance = ListenBrainzInstance.Default,
            artistImageSource = ArtistImageSource.Spotify.Default,
            hasDefaultSpotifyCredentials = true,
        )

        presenterTestOf({ presenter.present() }) {
            val state = awaitItem()
            assertEquals(
                ArtistImageSource.Wikimedia,
                state.artistImageSource,
            )
            awaitItem().run {
                assertEquals(
                    ArtistImageSource.Spotify.Default,
                    artistImageSource,
                )
            }

            state.eventSink(
                ServicesSettingsUiEvent.ConfirmArtistImageSource(
                    source = ArtistImageSource.Wikimedia,
                ),
            )
            awaitItem().run {
                assertEquals(
                    ArtistImageSource.Wikimedia,
                    artistImageSource,
                )
            }

            state.eventSink(ServicesSettingsUiEvent.Reset)
            awaitItem().run {
                assertEquals(
                    ArtistImageSource.Spotify.Default,
                    artistImageSource,
                )
            }
        }
    }

    @Test
    fun `reset falls back to wikimedia artist image source when no default spotify credentials`() = runTest {
        val presenter = createPresenter(
            navigator = navigator,
            musicBrainzInstance = MusicBrainzInstance.Default,
            listenBrainzInstance = ListenBrainzInstance.Default,
            artistImageSource = ArtistImageSource.Wikimedia,
            hasDefaultSpotifyCredentials = false,
        )

        presenterTestOf({ presenter.present() }) {
            val state = awaitItem()
            assertEquals(
                false,
                state.showDefaultSpotifyOption,
            )

            state.eventSink(
                ServicesSettingsUiEvent.ConfirmArtistImageSource(
                    source = ArtistImageSource.Spotify.Custom(
                        clientId = "id",
                        clientSecret = "secret",
                    ),
                ),
            )
            awaitItem().run {
                assertEquals(
                    ArtistImageSource.Spotify.Custom(
                        clientId = "id",
                        clientSecret = "secret",
                    ),
                    artistImageSource,
                )
            }

            state.eventSink(ServicesSettingsUiEvent.Reset)
            awaitItem().run {
                assertEquals(
                    ArtistImageSource.Wikimedia,
                    artistImageSource,
                )
            }
        }
    }
}

package ly.david.musicsearch.shared.feature.settings.services

import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.preferences.NoOpAppPreferences
import ly.david.musicsearch.shared.domain.preferences.MusicBrainzInstance
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
    ) = ServicesSettingsPresenter(
        navigator = navigator,
        appPreferences = object : NoOpAppPreferences() {
            private val instanceFlow = MutableStateFlow(musicBrainzInstance)

            override val musicBrainzInstance: Flow<MusicBrainzInstance>
                get() = instanceFlow

            override fun setMusicBrainzInstance(instance: MusicBrainzInstance) {
                instanceFlow.value = instance
            }
        },
    )

    @Test
    fun `smoke test`() = runTest {
        val presenter = createPresenter(
            navigator = navigator,
            musicBrainzInstance = MusicBrainzInstance.Default,
        )

        presenterTestOf({ presenter.present() }) {
            val state = awaitItem()
            assertEquals(
                MusicBrainzInstance.Default,
                state.musicBrainzInstance,
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
}

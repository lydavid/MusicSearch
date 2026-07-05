package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ly.david.musicsearch.shared.domain.LISTEN_BRAINZ_BASE_URL
import ly.david.musicsearch.shared.domain.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.shared.domain.WIKIDATA_BASE_URL
import ly.david.musicsearch.shared.domain.preferences.ListenBrainzInstance
import ly.david.musicsearch.shared.domain.preferences.MusicBrainzInstance
import ly.david.musicsearch.shared.domain.preferences.WikidataInstance
import ly.david.musicsearch.ui.common.topappbar.ScrollableTopAppBar
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.listenBrainzInstance
import musicsearch.ui.common.generated.resources.musicBrainzInstance
import musicsearch.ui.common.generated.resources.reset
import musicsearch.ui.common.generated.resources.services
import musicsearch.ui.common.generated.resources.wikidataInstance
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ServicesSettingsUi(
    state: ServicesSettingsUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            ScrollableTopAppBar(
                showBackButton = true,
                title = stringResource(Res.string.services),
                onBack = {
                    eventSink(ServicesSettingsUiEvent.NavigateUp)
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(innerPadding),
        ) {
            DefaultCustomInstanceSetting(
                title = stringResource(Res.string.musicBrainzInstance),
                subtitle = when (val instance = state.musicBrainzInstance) {
                    is MusicBrainzInstance.Custom -> {
                        instance.url
                    }

                    MusicBrainzInstance.Default -> {
                        "MusicBrainz ($MUSIC_BRAINZ_BASE_URL)"
                    }
                },
                initialSelectedCustom = state.musicBrainzInstance != MusicBrainzInstance.Default,
                initialTextInputValue = (state.musicBrainzInstance as? MusicBrainzInstance.Custom)?.url.orEmpty(),
                onConfirm = { isCustom, url ->
                    eventSink(ServicesSettingsUiEvent.ConfirmMusicBrainzInstance(isCustom, url))
                },
            )

            DefaultCustomInstanceSetting(
                title = stringResource(Res.string.listenBrainzInstance),
                subtitle = when (val instance = state.listenBrainzInstance) {
                    is ListenBrainzInstance.Custom -> {
                        instance.url
                    }

                    ListenBrainzInstance.Default -> {
                        "ListenBrainz ($LISTEN_BRAINZ_BASE_URL)"
                    }
                },
                initialSelectedCustom = state.listenBrainzInstance != ListenBrainzInstance.Default,
                initialTextInputValue = (state.listenBrainzInstance as? ListenBrainzInstance.Custom)?.url.orEmpty(),
                onConfirm = { isCustom, url ->
                    eventSink(ServicesSettingsUiEvent.ConfirmListenBrainzInstance(isCustom, url))
                },
            )

            DefaultCustomInstanceSetting(
                title = stringResource(Res.string.wikidataInstance),
                subtitle = when (val instance = state.wikidataInstance) {
                    is WikidataInstance.Custom -> {
                        instance.url
                    }

                    WikidataInstance.Default -> {
                        "Wikidata ($WIKIDATA_BASE_URL)"
                    }
                },
                initialSelectedCustom = state.wikidataInstance != WikidataInstance.Default,
                initialTextInputValue = (state.wikidataInstance as? WikidataInstance.Custom)?.url.orEmpty(),
                onConfirm = { isCustom, url ->
                    eventSink(ServicesSettingsUiEvent.ConfirmWikimediaInstance(isCustom, url))
                },
            )

            ArtistImageSourceSetting(
                initialSelectedArtistImageSource = state.artistImageSource,
                showDefaultSpotifyOption = state.showDefaultSpotifyOption,
                onConfirm = { source ->
                    eventSink(ServicesSettingsUiEvent.ConfirmArtistImageSource(source))
                },
            )

            TextButton(
                onClick = {
                    eventSink(
                        ServicesSettingsUiEvent.Reset,
                    )
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = stringResource(Res.string.reset),
                )
            }
        }
    }
}

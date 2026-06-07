package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.preferences.ListenBrainzInstance
import ly.david.musicsearch.shared.domain.preferences.MusicBrainzInstance
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewServicesSettingsUi() {
    PreviewTheme {
        Surface {
            ServicesSettingsUi(
                state = ServicesSettingsUiState(
                    musicBrainzInstance = MusicBrainzInstance.Default,
                    listenBrainzInstance = ListenBrainzInstance.Default,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewServicesSettingsUiCustom() {
    PreviewTheme {
        Surface {
            ServicesSettingsUi(
                state = ServicesSettingsUiState(
                    musicBrainzInstance = MusicBrainzInstance.Custom(
                        url = "https://musicbrainz.example.com",
                    ),
                    listenBrainzInstance = ListenBrainzInstance.Custom(
                        url = "https://listenbrainz.example.com",
                    ),
                ),
            )
        }
    }
}

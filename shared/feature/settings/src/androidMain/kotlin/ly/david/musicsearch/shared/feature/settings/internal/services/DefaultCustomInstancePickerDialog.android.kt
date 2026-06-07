package ly.david.musicsearch.shared.feature.settings.internal.services

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewDefaultCustomInstancePickerDialogDefault() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DefaultCustomInstancePickerDialog(
                title = "MusicBrainz instance",
                initialSelectedCustom = false,
                initialTextInputValue = "",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewDefaultCustomInstancePickerDialogCustom() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DefaultCustomInstancePickerDialog(
                title = "MusicBrainz instance",
                initialSelectedCustom = true,
                initialTextInputValue = "",
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewDefaultCustomInstancePickerDialogCustomWithText() {
    PreviewTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            DefaultCustomInstancePickerDialog(
                title = "MusicBrainz instance",
                initialSelectedCustom = true,
                initialTextInputValue = "https://musicbrainz.example.com",
            )
        }
    }
}

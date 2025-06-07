package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewSettingWithDialogChoices() {
    PreviewTheme {
        Surface {
            SettingWithDialogChoices(
                title = "Theme",
                choices = listOf("Light").toImmutableList(),
                selectedChoiceIndex = 0,
            )
        }
    }
}

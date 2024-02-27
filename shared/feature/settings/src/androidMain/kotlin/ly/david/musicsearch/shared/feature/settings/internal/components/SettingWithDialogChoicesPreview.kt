package ly.david.musicsearch.shared.feature.settings.internal.components

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.toImmutableList
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
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

package ly.david.musicsearch.shared.feature.details.releasegroup

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupScaffoldModel
import ly.david.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewReleaseGroupDetailsUi() {
    PreviewTheme {
        Surface {
            ReleaseGroupDetailsUi(
                releaseGroup = ReleaseGroupScaffoldModel(
                    id = "0a7f8b50-7888-4c50-9548-80144dad3460",
                    name = "夏枯れ",
                    primaryType = "Single",
                ),
            )
        }
    }
}

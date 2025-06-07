package ly.david.musicsearch.shared.feature.search

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewRecentSearchesHeader() {
    PreviewTheme {
        Surface {
            RecentSearchesHeader()
        }
    }
}

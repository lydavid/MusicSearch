package ly.david.musicsearch.feature.search.internal

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewRecentSearchesHeader() {
    PreviewTheme {
        Surface {
            RecentSearchesHeader()
        }
    }
}

package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.feature.stats.internal.addEntitiesStatsSection
import ly.david.musicsearch.ui.core.preview.DefaultPreviews
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addEntitiesStatsSection(
                    totalRemote = 10,
                    totalLocal = 7,
                    header = "Releases",
                    cachedLocalOfRemote = { local, remote ->
                        "Cached $local of $remote releases"
                    },
                )
            }
        }
    }
}

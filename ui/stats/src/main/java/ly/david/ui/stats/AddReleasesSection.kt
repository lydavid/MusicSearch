package ly.david.ui.stats

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

internal fun LazyListScope.addEntitiesStatsSection(
    totalRemote: Int?,
    totalLocal: Int,
    header: String,
    cachedLocalOfRemote: (Int, Int) -> String,
) {
    item {
        ListSeparatorHeader(header)

        LocalRemoteProgressBar(
            totalRemote = totalRemote,
            totalLocal = totalLocal,
            cachedLocalOfRemote = cachedLocalOfRemote
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

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

package ly.david.musicsearch.shared.feature.stats.internal

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader

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
            cachedLocalOfRemote = cachedLocalOfRemote,
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

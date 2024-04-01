package ly.david.musicsearch.shared.feature.stats.internal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.core.models.releasegroup.getDisplayTypes
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.core.theme.TextStyles

internal fun LazyListScope.addReleaseGroupsSection(
    totalRemote: Int?,
    totalLocal: Int,
    releaseGroupTypeCounts: List<ReleaseGroupTypeCount>,
) {
    item {
        val strings = LocalStrings.current
        ListSeparatorHeader(strings.releaseGroups)

        LocalRemoteProgressBar(
            totalRemote = totalRemote,
            totalLocal = totalLocal,
            cachedLocalOfRemote = strings.cachedReleaseGroups,
        )
    }
    items(releaseGroupTypeCounts) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
        ) {
            Text(
                style = TextStyles.getCardBodySubTextStyle(),
                text = "${it.getDisplayTypes()}: ${it.count}",
            )

            if (releaseGroupTypeCounts.isNotEmpty()) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(),
                    progress = it.count / releaseGroupTypeCounts.sumOf { it.count }.toFloat(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.Transparent,
                )
            }
        }
    }
    item {
        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

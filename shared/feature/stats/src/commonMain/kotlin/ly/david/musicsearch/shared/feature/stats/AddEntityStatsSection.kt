package ly.david.musicsearch.shared.feature.stats

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
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.ui.common.listitem.LastUpdatedText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles

internal fun LazyListScope.addEntityStatsSection(
    entityStats: EntityStats,
    header: String,
    now: Instant = Clock.System.now(),
) {
    item {
        val strings = LocalStrings.current
        Column {
            ListSeparatorHeader(header)

            CompletionProgressBar(
                totalCount = entityStats.totalRemote,
                currentCount = entityStats.totalLocal,
                formatProgressText = strings.cached,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
    val releaseGroupTypeCounts = entityStats.releaseGroupTypeCounts
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
                    progress = { it.count / releaseGroupTypeCounts.sumOf { it.count }.toFloat() },
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.Transparent,
                )
            }
        }
    }
    item {
        entityStats.lastUpdated?.let { lastUpdated ->
            LastUpdatedText(
                lastUpdated = lastUpdated,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                now = now,
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

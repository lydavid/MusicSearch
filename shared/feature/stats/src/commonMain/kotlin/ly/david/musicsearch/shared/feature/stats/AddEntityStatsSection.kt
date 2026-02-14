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
import ly.david.musicsearch.shared.domain.releasegroup.getDisplayTypes
import ly.david.musicsearch.ui.common.listitem.LastUpdatedText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.theme.TextStyles
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.getTitle
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.cached
import musicsearch.ui.common.generated.resources.collected
import musicsearch.ui.common.generated.resources.visited
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

internal fun LazyListScope.addEntityStatsSection(
    entityStats: EntityStats,
    tab: Tab,
    now: Instant = Clock.System.now(),
) {
    item {
        Column {
            ListSeparatorHeader(tab.getTitle())

            CompletionProgressBar(
                totalCount = entityStats.totalRemote,
                currentCount = entityStats.totalLocal,
                formatProgressText = stringResource(Res.string.cached),
                modifier = Modifier.padding(top = 4.dp),
            )

            CompletionProgressBar(
                totalCount = entityStats.totalRemote,
                currentCount = entityStats.totalVisited,
                formatProgressText = stringResource(Res.string.visited),
                modifier = Modifier.padding(top = 4.dp),
            )

            CompletionProgressBar(
                totalCount = entityStats.totalRemote,
                currentCount = entityStats.totalCollected,
                formatProgressText = stringResource(Res.string.collected),
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
                now = now,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

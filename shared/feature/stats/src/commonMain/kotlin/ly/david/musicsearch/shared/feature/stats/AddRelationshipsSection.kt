package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.relation.RelationStats
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.getName
import ly.david.musicsearch.ui.common.listitem.LastUpdatedText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.theme.TextStyles

internal fun LazyListScope.addRelationshipsSection(
    relationStats: RelationStats,
    now: Instant = Clock.System.now(),
) {
    val relationTypeCounts = relationStats.relationTypeCounts
    val totalRelations = relationTypeCounts.sumOf { it.count }
    val lastUpdated = relationStats.lastUpdated
    item {
        ListSeparatorHeader(LocalStrings.current.relationships)

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
            style = TextStyles.getCardBodyTextStyle(),
            text = if (lastUpdated == null) {
                "Tap Relationships tab to see more stats."
            } else {
                "Total relationships: $totalRelations"
            },
        )
    }
    items(relationTypeCounts) { relationTypeCount ->
        RelationTypeCountBar(
            relationTypeCount = relationTypeCount,
            totalRelations = totalRelations,
        )
    }
    item {
        lastUpdated?.let { lastUpdated ->
            LastUpdatedText(
                lastUpdated = lastUpdated,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                now = now,
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun RelationTypeCountBar(
    relationTypeCount: RelationTypeCount,
    totalRelations: Int,
) {
    val linkedEntity = relationTypeCount.linkedEntity

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            EntityIcon(
                modifier = Modifier.padding(end = 8.dp),
                entity = linkedEntity,
            )
            Text(
                style = TextStyles.getCardBodySubTextStyle(),
                text = "${linkedEntity.getName(LocalStrings.current)}: ${relationTypeCount.count}",
            )
        }

        if (totalRelations != 0) {
            LinearProgressIndicator(
                progress = { relationTypeCount.count / totalRelations.toFloat() },
                modifier = Modifier
                    .height(4.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = Color.Transparent,
            )
        }
    }
}

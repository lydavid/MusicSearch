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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.relation.RelationStats
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.ui.common.getIcon
import ly.david.musicsearch.ui.common.getName
import ly.david.musicsearch.ui.common.listitem.LastUpdatedText
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.text.TextWithIcon
import ly.david.musicsearch.ui.common.theme.TextStyles
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.relationships
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.Instant

internal fun LazyListScope.addRelationshipsSection(
    relationStats: RelationStats,
    now: Instant = Clock.System.now(),
) {
    val relationTypeCounts = relationStats.relationTypeCounts
    val totalRelations = relationTypeCounts.sumOf { it.count }
    val lastUpdated = relationStats.lastUpdated
    item {
        ListSeparatorHeader(stringResource(Res.string.relationships))

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
                now = now,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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
        val musicBrainzType = stringResource(linkedEntity.getName())
        TextWithIcon(
            imageVector = linkedEntity.getIcon() ?: return,
            text = "$musicBrainzType: ${relationTypeCount.count}",
            textStyle = TextStyles.getCardBodySubTextStyle(),
        )

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

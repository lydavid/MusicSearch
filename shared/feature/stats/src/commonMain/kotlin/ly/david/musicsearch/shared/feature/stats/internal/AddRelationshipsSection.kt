package ly.david.musicsearch.shared.feature.stats.internal

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.ui.core.LocalStrings
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.getName
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.core.theme.TextStyles

internal fun LazyListScope.addRelationshipsSection(
    totalRelations: Int?,
    relationTypeCounts: List<RelationTypeCount>,
) {
    item {
        val strings = LocalStrings.current
        ListSeparatorHeader(strings.relationships)

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = TextStyles.getCardBodyTextStyle(),
            text = if (totalRelations == null || relationTypeCounts.isEmpty()) {
                "No relationship stats available. Tap Relationships tab to fetch this entity's relationships."
            } else {
                "Total relationships: $totalRelations"
            },
        )
    }
    items(relationTypeCounts) { relationTypeCount ->
        val strings = LocalStrings.current
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
                    text = "${linkedEntity.getName(strings)}: ${relationTypeCount.count}",
                )
            }

            if (totalRelations != null && totalRelations != 0) {
                // Let's not round the corners because we don't expect this to fill to 100%
                // which makes it look strange
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
    item {
        Spacer(modifier = Modifier.padding(top = 16.dp))
    }
}

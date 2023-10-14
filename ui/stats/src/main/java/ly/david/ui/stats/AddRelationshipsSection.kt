package ly.david.ui.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.relation.RelationTypeCount
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.EntityIcon
import ly.david.ui.common.getDisplayText
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme
import ly.david.ui.core.theme.TextStyles

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
                    text = "${linkedEntity.getDisplayText(strings)}: ${relationTypeCount.count}",
                )
            }

            if (totalRelations != null && totalRelations != 0) {
                // Let's not round the corners because we don't expect this to fill to 100%
                // which makes it look strange
                LinearProgressIndicator(
                    modifier = Modifier
                        .height(4.dp)
                        .fillMaxWidth(),
                    progress = relationTypeCount.count / totalRelations.toFloat(),
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

// region Previews
@DefaultPreviews
@Composable
private fun Default() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    totalRelations = 49,
                    relationTypeCounts = listOf(
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.AREA, count = 1),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.ARTIST, count = 2),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.EVENT, count = 3),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.GENRE, count = 4),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.INSTRUMENT, count = 5),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.LABEL, count = 6),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.PLACE, count = 7),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.RECORDING, count = 6),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.RELEASE, count = 5),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.RELEASE_GROUP, count = 4),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.SERIES, count = 3),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.URL, count = 2),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.WORK, count = 1),
                    ),
                )
            }
        }
    }
}

@DefaultPreviews
@Composable
private fun NoRelationships() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    totalRelations = 0,
                    relationTypeCounts = listOf(),
                )
            }
        }
    }
}

@DefaultPreviews
@Composable
private fun NullRelationships() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    totalRelations = null,
                    relationTypeCounts = listOf(),
                )
            }
        }
    }
}
// endregion

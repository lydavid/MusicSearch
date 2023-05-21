package ly.david.mbjc.ui.stats

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.getDisplayTextRes
import ly.david.mbjc.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.preview.DefaultPreviews
import ly.david.ui.common.theme.PreviewTheme
import ly.david.ui.common.theme.TextStyles

internal fun LazyListScope.addRelationshipsSection(
    totalRelations: Int?,
    relationTypeCounts: List<RelationTypeCount>,
) {
    item {
        ListSeparatorHeader(text = stringResource(id = R.string.relationships))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            style = TextStyles.getCardBodyTextStyle(),
            text = if (totalRelations == null) {
                "No relationship stats available. Tap Relationships tab to fetch this entity's relationships."
            } else {
                "Total relationships: $totalRelations"
            }
        )
    }
    items(relationTypeCounts) { relationTypeCount ->

        val linkedResource = relationTypeCount.linkedResource

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (linkedResource == MusicBrainzResource.URL) {
                    Spacer(modifier = Modifier.padding(end = 32.dp))
                } else {
                    ResourceIcon(
                        modifier = Modifier.padding(end = 8.dp),
                        resource = linkedResource
                    )
                }
                Text(
                    style = TextStyles.getCardBodySubTextStyle(),
                    text = "${stringResource(id = linkedResource.getDisplayTextRes())}: ${relationTypeCount.count}"
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
                    trackColor = Color.Transparent
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
                        RelationTypeCount(linkedResource = MusicBrainzResource.AREA, count = 1),
                        RelationTypeCount(linkedResource = MusicBrainzResource.ARTIST, count = 2),
                        RelationTypeCount(linkedResource = MusicBrainzResource.EVENT, count = 3),
                        RelationTypeCount(linkedResource = MusicBrainzResource.GENRE, count = 4),
                        RelationTypeCount(linkedResource = MusicBrainzResource.INSTRUMENT, count = 5),
                        RelationTypeCount(linkedResource = MusicBrainzResource.LABEL, count = 6),
                        RelationTypeCount(linkedResource = MusicBrainzResource.PLACE, count = 7),
                        RelationTypeCount(linkedResource = MusicBrainzResource.RECORDING, count = 6),
                        RelationTypeCount(linkedResource = MusicBrainzResource.RELEASE, count = 5),
                        RelationTypeCount(linkedResource = MusicBrainzResource.RELEASE_GROUP, count = 4),
                        RelationTypeCount(linkedResource = MusicBrainzResource.SERIES, count = 3),
                        RelationTypeCount(linkedResource = MusicBrainzResource.URL, count = 2),
                        RelationTypeCount(linkedResource = MusicBrainzResource.WORK, count = 1),
                    )
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
                    relationTypeCounts = listOf()
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
                    relationTypeCounts = listOf()
                )
            }
        }
    }
}
// endregion

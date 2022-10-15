package ly.david.mbjc.ui.relation.stats

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ly.david.mbjc.R
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.common.ListSeparatorHeader
import ly.david.mbjc.ui.common.ResourceIcon
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme
import ly.david.mbjc.ui.theme.TextStyles

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
                "No relationship stats available. Visit Relationships tab to download this resource's relationships."
            } else {
                "Total number of relationships: $totalRelations"
            }
        )
    }
    items(relationTypeCounts) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            if (it.linkedResource == MusicBrainzResource.URL) {
                Spacer(modifier = Modifier.padding(end = 32.dp))
            } else {
                ResourceIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    resource = it.linkedResource
                )
            }
            Text(
                style = TextStyles.getCardBodyTextStyle(),
                text = "${it.linkedResource.displayText}: ${it.count}"
            )
        }
    }
}

@DefaultPreviews
@Composable
private fun Default() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    totalRelations = 696,
                    relationTypeCounts = listOf(
                        RelationTypeCount(linkedResource = MusicBrainzResource.ARTIST, 17),
                        RelationTypeCount(linkedResource = MusicBrainzResource.RECORDING, 397),
                        RelationTypeCount(linkedResource = MusicBrainzResource.URL, 2),
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

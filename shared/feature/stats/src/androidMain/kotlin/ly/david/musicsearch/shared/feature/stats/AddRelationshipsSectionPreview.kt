package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.relation.RelationTypeCount
import ly.david.musicsearch.shared.feature.stats.internal.addRelationshipsSection
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewAddRelationshipsSection() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    totalRelations = 49,
                    relationTypeCounts = listOf(
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.AREA,
                            count = 1,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            count = 2,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.EVENT,
                            count = 3,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.GENRE,
                            count = 4,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.INSTRUMENT,
                            count = 5,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.LABEL,
                            count = 6,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.PLACE,
                            count = 7,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.RECORDING,
                            count = 6,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.RELEASE,
                            count = 5,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.RELEASE_GROUP,
                            count = 4,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.SERIES,
                            count = 3,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.URL,
                            count = 2,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.WORK,
                            count = 1,
                        ),
                    ),
                )
            }
        }
    }
}

@DefaultPreviews
@Composable
internal fun PreviewAddRelationshipsSectionNoRelationships() {
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
internal fun PreviewAddRelationshipsSectionNullRelationships() {
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

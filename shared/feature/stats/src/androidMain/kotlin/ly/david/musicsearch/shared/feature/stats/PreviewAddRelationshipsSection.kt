package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationStats
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewAddRelationshipsSectionAll() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    relationStats = RelationStats(
                        relationTypeCounts = persistentListOf(
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.AREA,
                                count = 1,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.ARTIST,
                                count = 2,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.EVENT,
                                count = 3,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.GENRE,
                                count = 4,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.INSTRUMENT,
                                count = 5,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.LABEL,
                                count = 6,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.PLACE,
                                count = 7,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.RECORDING,
                                count = 6,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.RELEASE,
                                count = 5,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.RELEASE_GROUP,
                                count = 4,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.SERIES,
                                count = 3,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.URL,
                                count = 2,
                            ),
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.WORK,
                                count = 1,
                            ),
                        ),
                        lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
                    ),
                    now = Instant.parse("2025-04-26T16:42:20Z"),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAddRelationshipsSectionNoRelationships() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    relationStats = RelationStats(
                        lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
                    ),
                    now = Instant.parse("2025-04-26T16:42:20Z"),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAddRelationshipsSectionNullLastUpdated() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addRelationshipsSection(
                    relationStats = RelationStats(
                        relationTypeCounts = persistentListOf(
                            RelationTypeCount(
                                linkedEntity = MusicBrainzEntityType.URL,
                                count = 2,
                            ),
                        ),
                    ),
                    now = Instant.parse("2025-04-26T16:42:20Z"),
                )
            }
        }
    }
}

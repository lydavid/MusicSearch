package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
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
                                linkedEntity = MusicBrainzEntity.URL,
                                count = 2,
                            ),
                        )
                    ),
                    now = Instant.parse("2025-04-26T16:42:20Z"),
                )
            }
        }
    }
}

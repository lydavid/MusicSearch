package ly.david.musicsearch.share.feature.database

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.toImmutableMap
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewDatabaseUi() {
    PreviewTheme {
        Surface {
            DatabaseUi(
                state = DatabaseUiState(),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewDatabaseUiWithCounts() {
    PreviewTheme {
        Surface {
            DatabaseUi(
                state = DatabaseUiState(
                    countOfAllImages = 1_000_000,
                    entitiesCount = MusicBrainzEntityType.entries.associateWith { entity ->
                        // From https://musicbrainz.org/statistics on 2025-04-06
                        when (entity) {
                            MusicBrainzEntityType.AREA -> 119_549
                            MusicBrainzEntityType.ARTIST -> 2_582_422
                            MusicBrainzEntityType.EVENT -> 96_452
                            MusicBrainzEntityType.GENRE -> 2_026
                            MusicBrainzEntityType.INSTRUMENT -> 1_048
                            MusicBrainzEntityType.LABEL -> 301_448
                            MusicBrainzEntityType.PLACE -> 71_273
                            MusicBrainzEntityType.RECORDING -> 34_931_004
                            MusicBrainzEntityType.RELEASE -> 4_684_546
                            MusicBrainzEntityType.RELEASE_GROUP -> 3_654_363
                            MusicBrainzEntityType.SERIES -> 27_234
                            MusicBrainzEntityType.WORK -> 2_362_008
                            else -> 0
                        }
                    }.toImmutableMap(),
                ),
            )
        }
    }
}

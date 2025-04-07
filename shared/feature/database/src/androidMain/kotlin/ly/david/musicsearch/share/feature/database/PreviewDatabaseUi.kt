package ly.david.musicsearch.share.feature.database

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.toImmutableMap
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.core.theme.PreviewTheme

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
                    entitiesCount = MusicBrainzEntity.entries.associateWith { entity ->
                        // From https://musicbrainz.org/statistics on 2025-04-06
                        when (entity) {
                            MusicBrainzEntity.AREA -> 119_549L
                            MusicBrainzEntity.ARTIST -> 2_582_422
                            MusicBrainzEntity.EVENT -> 96_452
                            MusicBrainzEntity.GENRE -> 2_026
                            MusicBrainzEntity.INSTRUMENT -> 1_048
                            MusicBrainzEntity.LABEL -> 301_448
                            MusicBrainzEntity.PLACE -> 71_273
                            MusicBrainzEntity.RECORDING -> 34_931_004
                            MusicBrainzEntity.RELEASE -> 4_684_546
                            MusicBrainzEntity.RELEASE_GROUP -> 3_654_363
                            MusicBrainzEntity.SERIES -> 27_234
                            MusicBrainzEntity.WORK -> 2_362_008
                            else -> 0
                        }
                    }.toImmutableMap(),
                ),
            )
        }
    }
}

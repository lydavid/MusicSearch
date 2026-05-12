package ly.david.musicsearch.shared.feature.details.instrument

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.InstrumentDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val detailsUiState = DetailsUiState(
    browseMethod = BrowseMethod.ByEntity(
        entityId = "i1",
        entityType = MusicBrainzEntityType.INSTRUMENT,
    ),
    tabs = instrumentTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = InstrumentDetailsModel(
        id = "i1",
        name = "baroque guitar",
        disambiguation = "Baroque gut string guitar",
        type = "String instrument",
        description = "Predecessor of the modern classical guitar, " +
            "it had gut strings and even gut frets. " +
            "First described in 1555, it surpassed the Renaissance lute's popularity.",
        genres = persistentListOf(
            GenreChip(
                id = "",
                name = "baroque",
                count = 1,
            ),
        ),
        tags = persistentListOf(
            TagChip(
                name = "guitar",
                count = 1,
            ),
            TagChip(
                name = "strings",
                count = 1,
            ),
        ),
        lastUpdated = Instant.parse("2025-06-05T20:42:20Z"),
    ),
    detailsTabUiState = DetailsTabUiState(
        now = Instant.parse("2025-06-05T20:43:20Z"),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewInstrumentDetailsUi() {
    PreviewWithTransitionAndOverlays {
        InstrumentUi(
            state = detailsUiState,
        )
    }
}

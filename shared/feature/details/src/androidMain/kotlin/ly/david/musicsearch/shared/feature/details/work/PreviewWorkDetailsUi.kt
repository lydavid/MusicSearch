package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition

@PreviewLightDark
@Composable
internal fun PreviewWorkDetailsUi() {
    PreviewWithSharedElementTransition {
        WorkDetailsTabUi(
            work = WorkDetailsModel(
                id = "ea44224b-bf88-4f35-b10a-fe53a6c44ffc",
                name = "KEMURIKUSA",
                languages = listOf("jpn", "eng"),
                iswcs = listOf("T-927.551.670-6"),
                attributes = listOf(
                    WorkAttributeUiModel(
                        type = "BUMA/STEMRA ID",
                        typeId = "a6492434-b847-4f1b-9869-9184ade990ed",
                        value = "W-019368986",
                    ),
                    WorkAttributeUiModel(
                        type = "JASRAC ID",
                        typeId = "31048fcc-3dbb-3979-8f85-805afb933e0c",
                        value = "242-5984-5",
                    ),
                ),
                urls = persistentListOf(
                    RelationListItemModel(
                        id = "1",
                        linkedEntity = MusicBrainzEntityType.URL,
                        linkedEntityId = "2",
                        label = "lyrics page",
                        name = "https://genius.com/Genius-romanizations-nano-kemurikusa-romanized-lyrics",
                    ),
                ),
                lastUpdated = Instant.parse("2025-06-03T19:42:20Z"),
            ),
            detailsTabUiState = DetailsTabUiState(
                now = Instant.parse("2025-06-05T19:42:20Z"),
                totalUrls = 1,
            ),
        )
    }
}

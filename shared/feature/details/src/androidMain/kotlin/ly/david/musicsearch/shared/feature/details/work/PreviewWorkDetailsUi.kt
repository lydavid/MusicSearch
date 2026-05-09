package ly.david.musicsearch.shared.feature.details.work

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.shared.feature.details.utils.DetailsTabUiState
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.preview.PreviewWithTransitionAndOverlays
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Instant

private val workDetailsModel = WorkDetailsModel(
    id = "ea44224b-bf88-4f35-b10a-fe53a6c44ffc",
    name = "KEMURIKUSA",
    languages = persistentListOf("jpn", "eng"),
    iswcs = persistentListOf("T-927.551.670-6"),
    attributes = persistentListOf(
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
            type = "lyrics page",
            name = "https://genius.com/Genius-romanizations-nano-kemurikusa-romanized-lyrics",
        ),
    ),
    lastUpdated = Instant.parse("2025-06-03T19:42:20Z"),
)

private val detailsUiState = DetailsUiState(
    browseMethod = BrowseMethod.ByEntity(
        entityId = "ea44224b-bf88-4f35-b10a-fe53a6c44ffc",
        entityType = MusicBrainzEntityType.WORK,
    ),
    tabs = workTabs,
    selectedTab = Tab.DETAILS,
    detailsModel = workDetailsModel,
    detailsTabUiState = DetailsTabUiState(
        now = Instant.parse("2025-09-06T18:42:20Z"),
    ),
)

@PreviewLightDark
@Composable
internal fun PreviewWorkDetailsUi() {
    PreviewWithTransitionAndOverlays {
        WorkUi(
            state = detailsUiState,
        )
    }
}

@PreviewLightDark
@Composable
internal fun PreviewWorkDetailsUiWithListens() {
    PreviewWithTransitionAndOverlays {
        WorkUi(
            state = detailsUiState.copy(
                detailsModel = workDetailsModel.copy(
                    listenCount = 5,
                    latestListensTimestampsMs = persistentListOf(
                        1757116212000,
                        1740055177000,
                        1600055177000,
                    ),
                )
            ),
        )
    }
}

package ly.david.musicsearch.shared.feature.listens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.MutableStateFlow
import ly.david.musicsearch.shared.domain.list.FacetListItem
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.preview.PreviewWithSharedElementTransition
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

@PreviewLightDark
@Composable
internal fun PreviewFacetsBottomSheetContentRecordings() {
    PreviewWithSharedElementTransition {
        Surface {
            val facets = MutableStateFlow(
                PagingData.from(
                    data = listOf(
                        FacetListItem(
                            id = "",
                            name = "",
                            formattedArtistCredits = "",
                            count = 123,
                        ),
                        FacetListItem(
                            id = "1",
                            name = "COLORS",
                            formattedArtistCredits = "FLOW",
                            count = 12,
                        ),
                        FacetListItem(
                            id = "2",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            count = 9,
                        ),
                    ),
                ),
            )
            val filterState = rememberTopAppBarFilterState(
                initialFilterText = "Color",
                initialIsFilterMode = true,
            )
            FacetsBottomSheetContent(
                state = FacetsUiState(
                    selectedEntityFacet = MusicBrainzEntity(
                        id = "2",
                        type = MusicBrainzEntityType.RECORDING,
                    ),
                    filterState = filterState,
                    facetsPagingDataFlow = facets,
                ),
            )
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewFacetsBottomSheetContentReleases() {
    PreviewWithSharedElementTransition {
        Surface {
            val facets = MutableStateFlow(
                PagingData.from(
                    data = listOf(
                        FacetListItem(
                            id = "",
                            name = "",
                            formattedArtistCredits = "",
                            count = 5432,
                        ),
                        FacetListItem(
                            id = "1",
                            name = "幻燈 (Magic Lantern)",
                            formattedArtistCredits = "ヨルシカ",
                            count = 123,
                        ),
                        FacetListItem(
                            id = "2",
                            name = "Magic Lantern",
                            formattedArtistCredits = "Yorushika",
                            count = 123,
                        ),
                    ),
                ),
            )
            FacetsBottomSheetContent(
                state = FacetsUiState(
                    selectedEntityFacet = MusicBrainzEntity(
                        id = "2",
                        type = MusicBrainzEntityType.RELEASE,
                    ),
                    selectedTab = Tab.RELEASES,
                    facetsPagingDataFlow = facets,
                ),
            )
        }
    }
}

package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.core.models.artist.ArtistScaffoldModel
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.release.ReleasesByEntityUiState
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityUiState

@Stable
internal data class ArtistUiState(
    val title: String,
    val isError: Boolean,
    val artist: ArtistScaffoldModel?,
    val imageUrl: String,
    val tabs: List<ArtistTab>,
    val selectedTab: ArtistTab,
    val query: String,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (ArtistUiEvent) -> Unit,
) : CircuitUiState

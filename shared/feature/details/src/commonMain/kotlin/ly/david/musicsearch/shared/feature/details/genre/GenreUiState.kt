package ly.david.musicsearch.shared.feature.details.genre

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel

@Stable
internal data class GenreUiState(
    val title: String,
    val isError: Boolean,
    val genre: GenreMusicBrainzModel?,
    val eventSink: (GenreUiGenre) -> Unit,
) : CircuitUiState

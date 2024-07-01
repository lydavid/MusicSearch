package ly.david.musicsearch.shared.feature.details.genre

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzModel
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.ui.common.screen.DetailsScreen

// TODO: use repository
internal class GenrePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val musicBrainzApi: MusicBrainzApi,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val logger: Logger,
) : Presenter<GenreUiState> {

    @Composable
    override fun present(): GenreUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var genre: GenreMusicBrainzModel? by remember { mutableStateOf(null) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(forceRefreshDetails) {
            try {
                val genreListItemModel = musicBrainzApi.lookupGenre(screen.id)
                if (title.isEmpty()) {
                    title = genreListItemModel.getNameWithDisambiguation()
                }
                genre = genreListItemModel
                isError = false
            } catch (ex: RecoverableNetworkException) {
                logger.e(ex)
                isError = true
            }

            if (!recordedHistory) {
                incrementLookupHistory(
                    LookupHistory(
                        mbid = screen.id,
                        title = title,
                        entity = screen.entity,
                    ),
                )
                recordedHistory = true
            }
        }

        fun genreSink(genre: GenreUiEvent) {
            when (genre) {
                GenreUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                GenreUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return GenreUiState(
            title = title,
            isError = isError,
            genre = genre,
            eventSink = ::genreSink,
        )
    }
}

@Stable
internal data class GenreUiState(
    val title: String,
    val isError: Boolean,
    val genre: GenreMusicBrainzModel?,
    val eventSink: (GenreUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface GenreUiEvent : CircuitUiEvent {
    data object NavigateUp : GenreUiEvent
    data object ForceRefresh : GenreUiEvent
}

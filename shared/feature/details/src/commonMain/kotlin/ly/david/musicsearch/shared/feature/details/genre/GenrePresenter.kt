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
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.genre.GenreRepository
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import ly.david.musicsearch.shared.domain.network.resourceUri
import ly.david.musicsearch.ui.common.screen.DetailsScreen

internal class GenrePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: GenreRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val logger: Logger,
    private val musicbrainzRepository: MusicbrainzRepository,
) : Presenter<GenreUiState> {

    @Composable
    override fun present(): GenreUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var genre: GenreListItemModel? by remember { mutableStateOf(null) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(forceRefreshDetails) {
            try {
                val genreListItemModel = repository.lookupGenre(
                    id = screen.id,
                    forceRefresh = forceRefreshDetails,
                )
                title = genreListItemModel.getNameWithDisambiguation()
                genre = genreListItemModel
                isError = false
            } catch (ex: HandledException) {
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
            url = "${musicbrainzRepository.getBaseUrl()}/${screen.entity.resourceUri}/${screen.id}",
            eventSink = ::genreSink,
        )
    }
}

@Stable
internal data class GenreUiState(
    val title: String,
    val isError: Boolean,
    val genre: GenreListItemModel?,
    val url: String,
    val eventSink: (GenreUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface GenreUiEvent : CircuitUiEvent {
    data object NavigateUp : GenreUiEvent
    data object ForceRefresh : GenreUiEvent
}

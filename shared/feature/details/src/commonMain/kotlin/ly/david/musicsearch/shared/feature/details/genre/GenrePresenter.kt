package ly.david.musicsearch.shared.feature.details.genre

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.genre.GenreRepository
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.RecordVisit

internal class GenrePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: GenreRepository,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val logger: Logger,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
) : Presenter<GenreUiState>, RecordVisit {

    @Composable
    override fun present(): GenreUiState {
        var title by rememberSaveable { mutableStateOf("") }
        var handledException: HandledException? by rememberSaveable { mutableStateOf(null) }
        var genre: GenreListItemModel? by rememberRetained { mutableStateOf(null) }
        var forceRefreshDetails by remember { mutableStateOf(false) }

        LaunchedEffect(forceRefreshDetails) {
            try {
                val genreListItemModel = repository.lookupGenre(
                    id = screen.id,
                    forceRefresh = forceRefreshDetails,
                )
                title = genreListItemModel.getNameWithDisambiguation()
                genre = genreListItemModel
                handledException = null
            } catch (ex: HandledException) {
                logger.e(ex)
                handledException = ex
            }
        }

        RecordVisit(
            oldId = screen.id,
            mbid = genre?.id,
            title = title,
            entity = screen.entityType,
            searchHint = "",
        )

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
            handledException = handledException,
            genre = genre,
            url = getMusicBrainzUrl(screen.entityType, screen.id),
            eventSink = ::genreSink,
        )
    }
}

@Stable
internal data class GenreUiState(
    val title: String,
    val genre: GenreListItemModel?,
    val handledException: HandledException? = null,
    val url: String = "",
    val eventSink: (GenreUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface GenreUiEvent : CircuitUiEvent {
    data object NavigateUp : GenreUiEvent
    data object ForceRefresh : GenreUiEvent
}

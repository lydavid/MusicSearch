package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.listitem.TaggedEntityListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicbrainzRepository
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.usecase.GetEntitiesByTagOrGenre
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.GenreOrTagScreen
import ly.david.musicsearch.ui.common.screen.RecordVisit
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class GenreOrTagPresenter(
    private val screen: GenreOrTagScreen,
    private val navigator: Navigator,
    private val getEntitiesByTagOrGenre: GetEntitiesByTagOrGenre,
    override val incrementLookupHistory: IncrementLookupHistory,
    private val musicbrainzRepository: MusicbrainzRepository,
) : Presenter<GenreOrTagUiState>, RecordVisit {

    @Composable
    override fun present(): GenreOrTagUiState {
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        val lazyListState = rememberLazyListState()
        val genreOrTag = screen.genreOrTag
        val name = genreOrTag.name
        val isGenre = genreOrTag is GenreChip
        val url = musicbrainzRepository.getBaseUrl() + if (isGenre) {
            "/genre/${genreOrTag.id}"
        } else {
            "/tag/$name"
        }

        val pagingData: Flow<PagingData<TaggedEntityListItemModel>> by rememberRetained(
            name,
            query,
        ) {
            mutableStateOf(getEntitiesByTagOrGenre(name = name, query = query))
        }

        if (isGenre) {
            RecordVisit(
                oldId = genreOrTag.id,
                mbid = genreOrTag.id,
                title = name,
                entity = MusicBrainzEntityType.GENRE,
                searchHint = "",
            )
        }

        return GenreOrTagUiState(
            name = name,
            url = url,
            isGenre = isGenre,
            topAppBarFilterState = topAppBarFilterState,
            lazyListState = lazyListState,
            lazyPagingItems = pagingData.collectAsLazyPagingItems(),
            isPullToRefreshEnabled = true,
            eventSink = ::onEvent,
        )
    }

    private fun onEvent(event: EntitiesByTagUiEvent) {
        when (event) {
            is EntitiesByTagUiEvent.GoToEntity -> {
                navigator.goTo(
                    DetailsScreen(
                        entityType = event.entityType,
                        id = event.id,
                    ),
                )
            }

            EntitiesByTagUiEvent.NavigateUp -> navigator.pop()
        }
    }
}

@Stable
internal data class GenreOrTagUiState(
    val name: String,
    val url: String,
    val isGenre: Boolean,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val lazyListState: LazyListState = LazyListState(),
    val lazyPagingItems: LazyPagingItems<TaggedEntityListItemModel>,
    val isPullToRefreshEnabled: Boolean = true,
    val eventSink: (EntitiesByTagUiEvent) -> Unit = {},
) : CircuitUiState

internal sealed interface EntitiesByTagUiEvent : CircuitUiEvent {
    data class GoToEntity(
        val entityType: MusicBrainzEntityType,
        val id: String,
    ) : EntitiesByTagUiEvent

    data object NavigateUp : EntitiesByTagUiEvent
}

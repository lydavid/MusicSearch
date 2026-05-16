package ly.david.musicsearch.shared.feature.details.tag

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.withTime
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.tag.GenreChip
import ly.david.musicsearch.shared.domain.tag.GenreOrTag
import ly.david.musicsearch.shared.domain.tag.TagChip
import ly.david.musicsearch.shared.domain.tag.TagRepository
import ly.david.musicsearch.shared.domain.tag.VoteType
import ly.david.musicsearch.shared.domain.tag.getNewVoteCount
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.SearchScreen
import ly.david.musicsearch.ui.common.screen.SnackbarPopResult
import ly.david.musicsearch.ui.common.screen.TagDetailsScreen
import kotlin.time.Clock

class TagDetailsPresenter(
    private val screen: TagDetailsScreen,
    private val navigator: Navigator,
    private val tagRepository: TagRepository,
    // prefer external scope, so that vote goes through even when sheet dismissed
    private val coroutineScope: CoroutineScope,
    private val clock: Clock,
) : Presenter<TagDetailsUiState> {
    @Composable
    override fun present(): TagDetailsUiState {
        var genreOrTag by rememberSaveable { mutableStateOf(screen.genreOrTag) }
        var intermediateFeedback: Feedback<TagRepository.TagFeedback>? by remember { mutableStateOf(null) }

        fun eventSink(event: TagDetailsUiEvent) {
            when (event) {
                is TagDetailsUiEvent.Vote -> {
                    val newVoteType = event.voteType
                    val resolvedVoteType = when (val currentVoteType = genreOrTag.voteType) {
                        VoteType.Withdraw -> newVoteType // not actually possible
                        else -> if (currentVoteType == newVoteType) {
                            VoteType.Withdraw
                        } else {
                            newVoteType
                        }
                    }

                    // popping on terminal result let us use the call-site's snackbar host,
                    // and we don't have to manage in-memory genreOrTag for long, which could show a wrong count
                    // due to a race condition.
                    coroutineScope.launch {
                        tagRepository.voteOnTagForEntity(
                            genreOrTag = screen.genreOrTag,
                            musicBrainzEntity = screen.entity,
                            voteType = resolvedVoteType,
                        ).collect { feedback ->
                            when (feedback) {
                                is Feedback.Loading<*>,
                                is Feedback.Actionable<*>,
                                -> {
                                    intermediateFeedback = feedback
                                }

                                is Feedback.Error<*>,
                                is Feedback.Success<*>,
                                -> {
                                    navigator.pop(
                                        SnackbarPopResult(
                                            feedback = feedback.withTime(clock.now()),
                                        ),
                                    )
                                }
                            }
                        }
                    }

                    // for instant feedback, optimistically update
                    val newVoteCount = genreOrTag.getNewVoteCount(newVoteType = resolvedVoteType)
                    genreOrTag = when (genreOrTag) {
                        is GenreChip -> (genreOrTag as GenreChip).copy(
                            count = newVoteCount,
                            voteType = resolvedVoteType,
                        )

                        is TagChip -> (genreOrTag as TagChip).copy(
                            count = newVoteCount,
                            voteType = resolvedVoteType,
                        )
                    }
                }

                is TagDetailsUiEvent.SearchGenreOrTag -> {
                    navigator.pop(
                        SnackbarPopResult(
                            SearchScreen(
                                query = "tag:\"${event.name}\"",
                                entityType = screen.entity.type,
                            ),
                        ),
                    )
                }

                is TagDetailsUiEvent.GoToGenre -> {
                    navigator.pop(
                        SnackbarPopResult(
                            DetailsScreen(
                                id = event.id,
                                entityType = MusicBrainzEntityType.GENRE,
                            ),
                        ),
                    )
                }
            }
        }

        return TagDetailsUiState(
            genreOrTag = genreOrTag,
            feedback = intermediateFeedback,
            eventSink = {
                eventSink(it)
            },
        )
    }
}

data class TagDetailsUiState(
    val genreOrTag: GenreOrTag,
    val feedback: Feedback<TagRepository.TagFeedback>? = null,
    val eventSink: (TagDetailsUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface TagDetailsUiEvent : CircuitUiEvent {
    data class SearchGenreOrTag(val name: String) : TagDetailsUiEvent
    data class GoToGenre(val id: String) : TagDetailsUiEvent
    data class Vote(val voteType: VoteType) : TagDetailsUiEvent
}

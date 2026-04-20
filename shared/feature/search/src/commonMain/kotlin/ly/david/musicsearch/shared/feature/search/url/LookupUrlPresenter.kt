package ly.david.musicsearch.shared.feature.search.url

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.preferences.AppPreferences
import ly.david.musicsearch.shared.domain.url.LookupUrlRepository
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.screen.LookupUrlScreen

internal class LookupUrlPresenter(
    private val screen: LookupUrlScreen,
    private val navigator: Navigator,
    private val appPreferences: AppPreferences,
    private val lookupUrlRepository: LookupUrlRepository,
) : Presenter<LookupUrlUiState> {

    @Composable
    override fun present(): LookupUrlUiState {
        var query by rememberSaveable { mutableStateOf(screen.query.orEmpty()) }
        var excludeParameters by rememberSaveable { mutableStateOf(false) }
        var searchLocalDatabase by rememberSaveable { mutableStateOf(false) }
        var result: LookupUrlUiState.Result? by rememberRetained { mutableStateOf(null) }
        val coroutineScope = rememberCoroutineScope()
        val scrollToHideTopAppBar by appPreferences.scrollToHideTopAppBar.collectAsRetainedState(false)

        fun eventSink(event: LookupUrlUiEvent) {
            when (event) {
                is LookupUrlUiEvent.UpdateQuery -> {
                    query = event.query
                }

                LookupUrlUiEvent.ToggleExcludeParameters -> {
                    excludeParameters = !excludeParameters
                }

                is LookupUrlUiEvent.SetSearchLocalDatabase -> {
                    searchLocalDatabase = event.searchLocalDatabase
                }

                LookupUrlUiEvent.LookupUrl -> {
                    if (query.isBlank()) {
                        result = LookupUrlUiState.Result.Error.CannotBeEmpty
                        return
                    }
                    coroutineScope.launch {
                        val urlToLookup = if (excludeParameters) {
                            query.substringBefore("?")
                        } else {
                            query
                        }
                        result = LookupUrlUiState.Result.Loading
                        result = try {
                            LookupUrlUiState.Result.Success(
                                listItemModels = lookupUrlRepository.getEntitiesLinkedToUrl(
                                    url = urlToLookup,
                                    searchLocalDatabase = searchLocalDatabase,
                                ).toPersistentList(),
                            )
                        } catch (ex: HandledException) {
                            when (ex.errorType) {
                                ErrorType.BadRequest -> LookupUrlUiState.Result.Error.BadRequest(url = urlToLookup)
                                ErrorType.NotFound -> LookupUrlUiState.Result.Success(
                                    listItemModels = persistentListOf(),
                                )
                                else -> LookupUrlUiState.Result.Error.Other(ex.message.orEmpty())
                            }
                        }
                    }
                }

                is LookupUrlUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entityType = event.entityType,
                                id = event.id,
                            ),
                        ),
                    )
                }

                LookupUrlUiEvent.NavigateUp -> {
                    navigator.pop()
                }
            }
        }

        return LookupUrlUiState(
            urlToLookup = query,
            result = result,
            excludeParameters = excludeParameters,
            searchLocalDatabase = searchLocalDatabase,
            scrollToHideTopAppBar = scrollToHideTopAppBar,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class LookupUrlUiState(
    val urlToLookup: String,
    val result: Result?,
    val excludeParameters: Boolean = false,
    val searchLocalDatabase: Boolean = false,
    val scrollToHideTopAppBar: Boolean = false,
    val eventSink: (LookupUrlUiEvent) -> Unit = {},
) : CircuitUiState {

    sealed interface Result {
        data object Loading : Result
        data class Success(val listItemModels: ImmutableList<RelationListItemModel>) : Result
        sealed interface Error : Result {
            data object CannotBeEmpty : Error
            data class BadRequest(val url: String) : Error
            data class Other(val message: String) : Error
        }
    }
}

internal sealed interface LookupUrlUiEvent : CircuitUiEvent {
    data class UpdateQuery(val query: String) : LookupUrlUiEvent
    data object ToggleExcludeParameters : LookupUrlUiEvent
    data class SetSearchLocalDatabase(val searchLocalDatabase: Boolean) : LookupUrlUiEvent
    data object LookupUrl : LookupUrlUiEvent
    data class ClickItem(
        val entityType: MusicBrainzEntityType,
        val id: String,
    ) : LookupUrlUiEvent

    data object NavigateUp : LookupUrlUiEvent
}

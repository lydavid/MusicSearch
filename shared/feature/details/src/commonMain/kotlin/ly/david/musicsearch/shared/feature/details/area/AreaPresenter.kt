package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.core.models.area.showReleases
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.screens.DetailsScreen
import ly.david.ui.common.paging.RelationsList

internal class AreaPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: AreaRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val appPreferences: AppPreferences,
    private val relationsList: RelationsList,
    private val logger: Logger,
) : Presenter<AreaUiState> {
//    MusicBrainzEntityViewModel,
//    IRelationsList by relationsList {

    @Composable
    override fun present(): AreaUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsState(true)
        var area: AreaScaffoldModel? by remember { mutableStateOf(null) }
        var tabs: List<AreaTab> by rememberSaveable {
            mutableStateOf(AreaTab.entries.filter { it != AreaTab.RELEASES })
        }

        LaunchedEffect(Unit) {
            try {
                val areaScaffoldModel = repository.lookupArea(screen.id)
                if (title.isEmpty()) {
                    title = areaScaffoldModel.getNameWithDisambiguation()
                }
                if (areaScaffoldModel.showReleases()) {
                    tabs = AreaTab.entries
                }
                area = areaScaffoldModel
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
                        searchHint = area?.sortName.orEmpty(),
                    ),
                )
                recordedHistory = true
            }
        }

        fun eventSink(event: AreaUiEvent) {
            when (event) {
                AreaUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is AreaUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is AreaUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return AreaUiState(
            title = title,
            isError = isError,
            area = area,
            tabs = tabs,
            query = query,
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            eventSink = ::eventSink,
        )
    }

//
//    init {
//        relationsList.scope = viewModelScope
//        relationsList.entity = entity
//    }
//
//    fun loadDataForTab(
//        areaId: String,
//        selectedTab: AreaTab,
//    ) {
//        when (selectedTab) {
//            AreaTab.DETAILS -> {

//            }
//
//            AreaTab.RELATIONSHIPS -> loadRelations(areaId)
//            else -> {
//                // Not handled here.
//            }
//        }
//    }
}

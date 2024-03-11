package ly.david.ui.common.release

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.domain.release.ReleaseImageRepository
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity

class ReleasesByEntityPresenter(
    private val getReleasesByEntity: GetReleasesByEntity,
    private val appPreferences: AppPreferences,
    private val releaseImageRepository: ReleaseImageRepository,
    private val logger: Logger,
) : Presenter<ReleasesByEntityUiState> {
    @Composable
    override fun present(): ReleasesByEntityUiState {
        val scope = rememberCoroutineScope()
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsState(true)
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var releaseListItems: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            releaseListItems = getReleasesByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                ),
            )
        }

        fun eventSink(event: ReleasesByEntityUiEvent) {
            when (event) {
                is ReleasesByEntityUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        try {
                            releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
                                releaseId = event.entityId,
                                thumbnail = true,
                            )
                        } catch (ex: Exception) {
                            logger.e(ex)
                        }
                    }
                }

                is ReleasesByEntityUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is ReleasesByEntityUiEvent.GetReleases -> {
                    id = event.byEntityId
                    entity = event.byEntity
                }

                is ReleasesByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ReleasesByEntityUiState(
            lazyPagingItems = releaseListItems.collectAsLazyPagingItems(),
            showMoreInfo = showMoreInfoInReleaseListItem,
            eventSink = ::eventSink,
        )
    }
}

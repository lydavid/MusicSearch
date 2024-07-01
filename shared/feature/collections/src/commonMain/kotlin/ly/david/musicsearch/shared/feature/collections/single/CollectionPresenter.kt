package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import app.cash.paging.insertSeparators
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.models.ActionableResult
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.listitem.CollectionListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.area.usecase.GetAreasByEntity
import ly.david.musicsearch.shared.domain.collection.usecase.DeleteFromCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetCollection
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.instrument.usecase.GetInstrumentsByEntity
import ly.david.musicsearch.shared.domain.place.usecase.GetPlacesByEntity
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordingsByEntity
import ly.david.musicsearch.shared.domain.series.usecase.GetSeriesByEntity
import ly.david.ui.common.artist.ArtistsByEntityPresenter
import ly.david.ui.common.artist.ArtistsByEntityUiEvent
import ly.david.ui.common.artist.ArtistsByEntityUiState
import ly.david.ui.common.event.EventsByEntityPresenter
import ly.david.ui.common.event.EventsByEntityUiEvent
import ly.david.ui.common.event.EventsByEntityUiState
import ly.david.ui.common.label.LabelsByEntityPresenter
import ly.david.ui.common.label.LabelsByEntityUiEvent
import ly.david.ui.common.label.LabelsByEntityUiState
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent
import ly.david.ui.common.release.ReleasesByEntityUiState
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityPresenter
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityUiEvent
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityUiState
import ly.david.ui.common.screen.CollectionScreen
import ly.david.ui.common.screen.DetailsScreen
import ly.david.ui.common.work.WorksByEntityPresenter
import ly.david.ui.common.work.WorksByEntityUiEvent
import ly.david.ui.common.work.WorksByEntityUiState

internal class CollectionPresenter(
    private val screen: CollectionScreen,
    private val navigator: Navigator,
    private val getCollectionUseCase: GetCollection,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val getAreasByEntity: GetAreasByEntity,
    private val artistsByEntityPresenter: ArtistsByEntityPresenter,
    private val getInstrumentsByEntity: GetInstrumentsByEntity,
    private val labelsByEntityPresenter: LabelsByEntityPresenter,
    private val getPlacesByEntity: GetPlacesByEntity,
    private val getRecordingsByEntity: GetRecordingsByEntity,
    private val eventsByEntityPresenter: EventsByEntityPresenter,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val releaseGroupsByEntityPresenter: ReleaseGroupsByEntityPresenter,
    private val worksByEntityPresenter: WorksByEntityPresenter,
    private val getSeriesByEntity: GetSeriesByEntity,
    private val deleteFromCollection: DeleteFromCollection,
) : Presenter<CollectionUiState> {
    @Composable
    override fun present(): CollectionUiState {
        val collectionId = screen.id

        val scope = rememberCoroutineScope()
        var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
        var actionableResult: ActionableResult? by remember { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var collectableItems: Flow<PagingData<ListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val artistsByEntityUiState = artistsByEntityPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink
        val eventsByEntityUiState = eventsByEntityPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val labelsByEntityUiState = labelsByEntityPresenter.present()
        val labelsEventSink = labelsByEntityUiState.eventSink
        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val releaseGroupsByEntityUiState = releaseGroupsByEntityPresenter.present()
        val releaseGroupsEventSink = releaseGroupsByEntityUiState.eventSink
        val worksByEntityUiState = worksByEntityPresenter.present()
        val worksEventSink = worksByEntityUiState.eventSink

        LaunchedEffect(Unit) {
            val nonNullCollection = getCollectionUseCase(collectionId)
            collection = nonNullCollection
            isRemote = nonNullCollection.isRemote

            if (!recordedHistory) {
                incrementLookupHistory(
                    LookupHistory(
                        mbid = collectionId,
                        title = nonNullCollection.name,
                        entity = MusicBrainzEntity.COLLECTION,
                    ),
                )
                recordedHistory = true
            }
        }

        LaunchedEffect(
            key1 = query,
        ) {
            when (collection?.entity) {
                MusicBrainzEntity.AREA -> {
                    collectableItems = getAreasByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.ARTIST -> {
                    artistsEventSink(
                        ArtistsByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    artistsEventSink(ArtistsByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.EVENT -> {
                    eventsEventSink(
                        EventsByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    eventsEventSink(EventsByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    collectableItems = getInstrumentsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.LABEL -> {
                    labelsEventSink(
                        LabelsByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    labelsEventSink(LabelsByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.PLACE -> {
                    collectableItems = getPlacesByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.RECORDING -> {
                    collectableItems = getRecordingsByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.RELEASE -> {
                    releasesEventSink(
                        ReleasesByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    releasesEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    releaseGroupsEventSink(
                        ReleaseGroupsByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    releaseGroupsEventSink(ReleaseGroupsByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.SERIES -> {
                    collectableItems = getSeriesByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                        listFilters = ListFilters(
                            query = query,
                            isRemote = isRemote,
                        ),
                    ).map { pagingData ->
                        pagingData.insertSeparators { _, _ ->
                            null
                        }
                    }
                }

                MusicBrainzEntity.WORK -> {
                    worksEventSink(
                        WorksByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    worksEventSink(WorksByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.COLLECTION,
                MusicBrainzEntity.GENRE,
                MusicBrainzEntity.URL,
                -> {
                    error("${collection?.entity} by collection not supported")
                }

                null -> {
                    // no-op
                }
            }
        }

        fun eventSink(event: CollectionUiEvent) {
            when (event) {
                is CollectionUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is CollectionUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                                title = event.title,
                            ),
                        ),
                    )
                }

                is CollectionUiEvent.MarkItemForDeletion -> {
                    scope.launch {
                        actionableResult = deleteFromCollection(
                            collectionId = collection?.id ?: return@launch,
                            entityId = event.collectableId,
                            entityName = event.name,
                        )
                    }
                }

                is CollectionUiEvent.UnMarkItemForDeletion -> {
                }

                is CollectionUiEvent.DeleteItem -> {
                }

                is CollectionUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return CollectionUiState(
            collection = collection,
            actionableResult = actionableResult,
            query = query,
            lazyPagingItems = collectableItems.collectAsLazyPagingItems(),
            artistsByEntityUiState = artistsByEntityUiState,
            eventsByEntityUiState = eventsByEntityUiState,
            labelsByEntityUiState = labelsByEntityUiState,
            releasesByEntityUiState = releasesByEntityUiState,
            releaseGroupsByEntityUiState = releaseGroupsByEntityUiState,
            worksByEntityUiState = worksByEntityUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class CollectionUiState(
    val collection: CollectionListItemModel?,
    val actionableResult: ActionableResult?,
    val query: String,
    val lazyPagingItems: LazyPagingItems<ListItemModel>,
    val artistsByEntityUiState: ArtistsByEntityUiState,
    val eventsByEntityUiState: EventsByEntityUiState,
    val labelsByEntityUiState: LabelsByEntityUiState,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    val worksByEntityUiState: WorksByEntityUiState,
    val eventSink: (CollectionUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface CollectionUiEvent : CircuitUiEvent {
    data object NavigateUp : CollectionUiEvent

    data class UpdateQuery(val query: String) : CollectionUiEvent

    data class MarkItemForDeletion(
        val collectableId: String,
        val name: String,
    ) : CollectionUiEvent

    data class UnMarkItemForDeletion(val collectableId: String) : CollectionUiEvent

    data class DeleteItem(
        val collectableId: String,
        val name: String,
    ) : CollectionUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : CollectionUiEvent
}

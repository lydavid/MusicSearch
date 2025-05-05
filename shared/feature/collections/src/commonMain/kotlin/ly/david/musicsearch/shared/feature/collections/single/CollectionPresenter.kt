package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentSet
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.usecase.GetCollection
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.area.AreasListPresenter
import ly.david.musicsearch.ui.common.area.AreasListUiEvent
import ly.david.musicsearch.ui.common.area.AreasListUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListUiEvent
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.genre.GenresListPresenter
import ly.david.musicsearch.ui.common.genre.GenresListUiEvent
import ly.david.musicsearch.ui.common.genre.GenresListUiState
import ly.david.musicsearch.ui.common.instrument.InstrumentsListPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsListUiEvent
import ly.david.musicsearch.ui.common.instrument.InstrumentsListUiState
import ly.david.musicsearch.ui.common.label.LabelsListPresenter
import ly.david.musicsearch.ui.common.label.LabelsListUiEvent
import ly.david.musicsearch.ui.common.label.LabelsListUiState
import ly.david.musicsearch.ui.common.place.PlacesListPresenter
import ly.david.musicsearch.ui.common.place.PlacesListUiEvent
import ly.david.musicsearch.ui.common.place.PlacesListUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsListUiEvent
import ly.david.musicsearch.ui.common.recording.RecordingsListUiState
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiState
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.series.SeriesListPresenter
import ly.david.musicsearch.ui.common.series.SeriesListUiEvent
import ly.david.musicsearch.ui.common.series.SeriesListUiState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.work.WorksListPresenter
import ly.david.musicsearch.ui.common.work.WorksListUiEvent
import ly.david.musicsearch.ui.common.work.WorksListUiState

internal class CollectionPresenter(
    private val screen: CollectionScreen,
    private val navigator: Navigator,
    private val getCollection: GetCollection,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val areasListPresenter: AreasListPresenter,
    private val artistsListPresenter: ArtistsListPresenter,
    private val eventsListPresenter: EventsListPresenter,
    private val genresListPresenter: GenresListPresenter,
    private val instrumentsListPresenter: InstrumentsListPresenter,
    private val labelsListPresenter: LabelsListPresenter,
    private val placesListPresenter: PlacesListPresenter,
    private val recordingsListPresenter: RecordingsListPresenter,
    private val releasesListPresenter: ReleasesListPresenter,
    private val releaseGroupsListPresenter: ReleaseGroupsListPresenter,
    private val seriesListPresenter: SeriesListPresenter,
    private val worksListPresenter: WorksListPresenter,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val collectionRepository: CollectionRepository,
) : Presenter<CollectionUiState> {
    @Composable
    override fun present(): CollectionUiState {
        val collectionId = screen.collectionId

        var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
        var title: String by rememberSaveable { mutableStateOf("") }
        var actionableResult: ActionableResult? by remember { mutableStateOf(null) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val topAppBarEditState: TopAppBarEditState = rememberTopAppBarEditState()
        var selectedIds: Set<String> by rememberSaveable { mutableStateOf(setOf()) }

        val areasByEntityUiState = areasListPresenter.present()
        val areasEventSink = areasByEntityUiState.eventSink
        val artistsByEntityUiState = artistsListPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink
        val eventsByEntityUiState = eventsListPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val genresByEntityUiState = genresListPresenter.present()
        val genresEventSink = genresByEntityUiState.eventSink
        val instrumentsByEntityUiState = instrumentsListPresenter.present()
        val instrumentsEventSink = instrumentsByEntityUiState.eventSink
        val labelsByEntityUiState = labelsListPresenter.present()
        val labelsEventSink = labelsByEntityUiState.eventSink
        val placesByEntityUiState = placesListPresenter.present()
        val placesEventSink = placesByEntityUiState.eventSink
        val recordingsByEntityUiState = recordingsListPresenter.present()
        val recordingsEventSink = recordingsByEntityUiState.eventSink
        val releasesByEntityUiState = releasesListPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val releaseGroupsByEntityUiState = releaseGroupsListPresenter.present()
        val releaseGroupsEventSink = releaseGroupsByEntityUiState.eventSink
        val seriesByEntityUiState = seriesListPresenter.present()
        val seriesEventSink = seriesByEntityUiState.eventSink
        val worksByEntityUiState = worksListPresenter.present()
        val worksEventSink = worksByEntityUiState.eventSink

        var oneShotNewCollectableId: String? by rememberRetained {
            mutableStateOf(screen.collectableId)
        }

        LaunchedEffect(Unit) {
            val nonNullCollection = getCollection(collectionId) ?: return@LaunchedEffect
            collection = nonNullCollection
            isRemote = nonNullCollection.isRemote
            title = nonNullCollection.name

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

            collectionRepository.addToCollection(
                collectionId = nonNullCollection.id,
                entity = nonNullCollection.entity,
                entityId = oneShotNewCollectableId ?: return@LaunchedEffect,
            )
            oneShotNewCollectableId = null
        }

        LaunchedEffect(topAppBarEditState.isEditMode) {
            if (!topAppBarEditState.isEditMode) {
                selectedIds = setOf()
            }
            if (selectedIds.isEmpty()) {
                topAppBarEditState.customTitle = ""
            }
        }

        LaunchedEffect(
            key1 = query,
        ) {
            val entity = collection?.entity ?: return@LaunchedEffect
            val browseMethod = BrowseMethod.ByEntity(
                entityId = collectionId,
                entity = MusicBrainzEntity.COLLECTION,
            )
            when (entity) {
                MusicBrainzEntity.AREA -> {
                    areasEventSink(
                        AreasListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    areasEventSink(AreasListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.ARTIST -> {
                    artistsEventSink(
                        ArtistsListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    artistsEventSink(ArtistsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.EVENT -> {
                    eventsEventSink(
                        EventsListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    eventsEventSink(EventsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.GENRE -> {
                    genresEventSink(
                        GenresListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    genresEventSink(GenresListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    instrumentsEventSink(
                        InstrumentsListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    instrumentsEventSink(InstrumentsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.LABEL -> {
                    labelsEventSink(
                        LabelsListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    labelsEventSink(LabelsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.PLACE -> {
                    placesEventSink(
                        PlacesListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    placesEventSink(PlacesListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RECORDING -> {
                    recordingsEventSink(
                        RecordingsListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    recordingsEventSink(RecordingsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RELEASE -> {
                    releasesEventSink(
                        ReleasesListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releasesEventSink(ReleasesListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    releaseGroupsEventSink(
                        ReleaseGroupsListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.SERIES -> {
                    seriesEventSink(
                        SeriesListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    seriesEventSink(SeriesListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.WORK -> {
                    worksEventSink(
                        WorksListUiEvent.Get(
                            browseMethod = browseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    worksEventSink(WorksListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.COLLECTION,
                MusicBrainzEntity.URL,
                -> {
                    error("${collection?.entity} by collection not supported")
                }
            }
        }

        fun eventSink(event: CollectionUiEvent) {
            when (event) {
                is CollectionUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is CollectionUiEvent.ToggleSelectItem -> {
                    selectedIds = if (selectedIds.contains(event.collectableId)) {
                        selectedIds.minus(event.collectableId)
                    } else {
                        selectedIds.plus(event.collectableId)
                    }
                    topAppBarEditState.toggleEditMode(selectedIds.isNotEmpty())
                    topAppBarEditState.customTitle = "Selected ${selectedIds.size}"
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

                is CollectionUiEvent.MarkSelectedItemsAsDeleted -> {
                    actionableResult = collectionRepository.markDeletedFromCollection(
                        collection = collection ?: return,
                        collectableIds = selectedIds,
                    )
                    selectedIds = setOf()
                    topAppBarEditState.toggleEditMode(false)
                }

                is CollectionUiEvent.UnMarkItemsAsDeleted -> {
                    collectionRepository.unMarkDeletedFromCollection(
                        collectionId = collection?.id ?: return,
                    )
                }
            }
        }

        suspend fun suspendEventSink(event: SuspendCollectionUiEvent) {
            when (event) {
                is SuspendCollectionUiEvent.DeleteItemsMarkedAsDeleted -> {
                    // We cannot launch a new scope if we want to run this as part of the cancellation of the parent scope.
                    actionableResult = collectionRepository.deleteFromCollection(
                        collection = collection ?: return,
                    )
                }
            }
        }

        return CollectionUiState(
            title = title,
            collection = collection,
            url = getMusicBrainzUrl(MusicBrainzEntity.COLLECTION, screen.collectionId),
            actionableResult = actionableResult,
            topAppBarFilterState = topAppBarFilterState,
            topAppBarEditState = topAppBarEditState,
            selectedIds = selectedIds.toPersistentSet(),
            areasListUiState = areasByEntityUiState,
            artistsListUiState = artistsByEntityUiState,
            eventsListUiState = eventsByEntityUiState,
            genresListUiState = genresByEntityUiState,
            instrumentsListUiState = instrumentsByEntityUiState,
            labelsListUiState = labelsByEntityUiState,
            placesListUiState = placesByEntityUiState,
            recordingsListUiState = recordingsByEntityUiState,
            releasesListUiState = releasesByEntityUiState,
            releaseGroupsListUiState = releaseGroupsByEntityUiState,
            seriesListUiState = seriesByEntityUiState,
            worksListUiState = worksByEntityUiState,
            eventSink = ::eventSink,
            suspendEventSink = ::suspendEventSink,
        )
    }
}

@Stable
internal data class CollectionUiState(
    val title: String,
    val collection: CollectionListItemModel?,
    val actionableResult: ActionableResult?,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val url: String,
    val topAppBarEditState: TopAppBarEditState,
    val selectedIds: ImmutableSet<String>,
    val areasListUiState: AreasListUiState,
    val artistsListUiState: ArtistsListUiState,
    val eventsListUiState: EventsListUiState,
    val genresListUiState: GenresListUiState,
    val instrumentsListUiState: InstrumentsListUiState,
    val labelsListUiState: LabelsListUiState,
    val placesListUiState: PlacesListUiState,
    val recordingsListUiState: RecordingsListUiState,
    val releasesListUiState: ReleasesListUiState,
    val releaseGroupsListUiState: ReleaseGroupsListUiState,
    val seriesListUiState: SeriesListUiState,
    val worksListUiState: WorksListUiState,
    val eventSink: (CollectionUiEvent) -> Unit,
    val suspendEventSink: suspend (SuspendCollectionUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface CollectionUiEvent : CircuitUiEvent {
    data object NavigateUp : CollectionUiEvent

    data class ToggleSelectItem(
        val collectableId: String,
    ) : CollectionUiEvent

    data object MarkSelectedItemsAsDeleted : CollectionUiEvent

    data object UnMarkItemsAsDeleted : CollectionUiEvent

    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : CollectionUiEvent
}

internal sealed interface SuspendCollectionUiEvent : CircuitUiEvent {
    data object DeleteItemsMarkedAsDeleted : SuspendCollectionUiEvent
}

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
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.collection.usecase.DeleteFromCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetCollection
import ly.david.musicsearch.shared.domain.error.ActionableResult
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.area.AreasByEntityPresenter
import ly.david.musicsearch.ui.common.area.AreasByEntityUiEvent
import ly.david.musicsearch.ui.common.area.AreasByEntityUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.event.EventsByEntityPresenter
import ly.david.musicsearch.ui.common.event.EventsByEntityUiEvent
import ly.david.musicsearch.ui.common.event.EventsByEntityUiState
import ly.david.musicsearch.ui.common.genre.GenresByEntityPresenter
import ly.david.musicsearch.ui.common.genre.GenresByEntityUiEvent
import ly.david.musicsearch.ui.common.genre.GenresByEntityUiState
import ly.david.musicsearch.ui.common.instrument.InstrumentsByEntityPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsByEntityUiEvent
import ly.david.musicsearch.ui.common.instrument.InstrumentsByEntityUiState
import ly.david.musicsearch.ui.common.label.LabelsByEntityPresenter
import ly.david.musicsearch.ui.common.label.LabelsByEntityUiEvent
import ly.david.musicsearch.ui.common.label.LabelsByEntityUiState
import ly.david.musicsearch.ui.common.place.PlacesByEntityPresenter
import ly.david.musicsearch.ui.common.place.PlacesByEntityUiEvent
import ly.david.musicsearch.ui.common.place.PlacesByEntityUiState
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityUiEvent
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityUiState
import ly.david.musicsearch.ui.common.release.ReleasesByEntityPresenter
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiState
import ly.david.musicsearch.ui.common.screen.CollectionScreen
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.series.SeriesByEntityPresenter
import ly.david.musicsearch.ui.common.series.SeriesByEntityUiEvent
import ly.david.musicsearch.ui.common.series.SeriesByEntityUiState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarEditState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState
import ly.david.musicsearch.ui.common.work.WorksByEntityPresenter
import ly.david.musicsearch.ui.common.work.WorksByEntityUiEvent
import ly.david.musicsearch.ui.common.work.WorksByEntityUiState

internal class CollectionPresenter(
    private val screen: CollectionScreen,
    private val navigator: Navigator,
    private val getCollection: GetCollection,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val areasByEntityPresenter: AreasByEntityPresenter,
    private val artistsListPresenter: ArtistsListPresenter,
    private val eventsByEntityPresenter: EventsByEntityPresenter,
    private val genresByEntityPresenter: GenresByEntityPresenter,
    private val instrumentsByEntityPresenter: InstrumentsByEntityPresenter,
    private val labelsByEntityPresenter: LabelsByEntityPresenter,
    private val placesByEntityPresenter: PlacesByEntityPresenter,
    private val recordingsByEntityPresenter: RecordingsByEntityPresenter,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val releaseGroupsByEntityPresenter: ReleaseGroupsByEntityPresenter,
    private val seriesByEntityPresenter: SeriesByEntityPresenter,
    private val worksByEntityPresenter: WorksByEntityPresenter,
    private val deleteFromCollection: DeleteFromCollection,
    private val getMusicBrainzUrl: GetMusicBrainzUrl,
    private val collectionRepository: CollectionRepository,
) : Presenter<CollectionUiState> {
    @Composable
    override fun present(): CollectionUiState {
        val collectionId = screen.collectionId

        val scope = rememberCoroutineScope()
        var collection: CollectionListItemModel? by remember { mutableStateOf(null) }
        var title: String by rememberSaveable { mutableStateOf("") }
        var actionableResult: ActionableResult? by remember { mutableStateOf(null) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        val topAppBarEditState: TopAppBarEditState = rememberTopAppBarEditState()

        val areasByEntityUiState = areasByEntityPresenter.present()
        val areasEventSink = areasByEntityUiState.eventSink
        val artistsByEntityUiState = artistsListPresenter.present()
        val artistsEventSink = artistsByEntityUiState.eventSink
        val eventsByEntityUiState = eventsByEntityPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val genresByEntityUiState = genresByEntityPresenter.present()
        val genresEventSink = genresByEntityUiState.eventSink
        val instrumentsByEntityUiState = instrumentsByEntityPresenter.present()
        val instrumentsEventSink = instrumentsByEntityUiState.eventSink
        val labelsByEntityUiState = labelsByEntityPresenter.present()
        val labelsEventSink = labelsByEntityUiState.eventSink
        val placesByEntityUiState = placesByEntityPresenter.present()
        val placesEventSink = placesByEntityUiState.eventSink
        val recordingsByEntityUiState = recordingsByEntityPresenter.present()
        val recordingsEventSink = recordingsByEntityUiState.eventSink
        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val releaseGroupsByEntityUiState = releaseGroupsByEntityPresenter.present()
        val releaseGroupsEventSink = releaseGroupsByEntityUiState.eventSink
        val seriesByEntityUiState = seriesByEntityPresenter.present()
        val seriesEventSink = seriesByEntityUiState.eventSink
        val worksByEntityUiState = worksByEntityPresenter.present()
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

        LaunchedEffect(
            key1 = query,
        ) {
            when (collection?.entity) {
                MusicBrainzEntity.AREA -> {
                    areasEventSink(
                        AreasByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    areasEventSink(AreasByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.ARTIST -> {
                    artistsEventSink(
                        ArtistsListUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    artistsEventSink(ArtistsListUiEvent.UpdateQuery(query))
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

                MusicBrainzEntity.GENRE -> {
                    genresEventSink(
                        GenresByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    genresEventSink(GenresByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    instrumentsEventSink(
                        InstrumentsByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    instrumentsEventSink(InstrumentsByEntityUiEvent.UpdateQuery(query))
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
                    placesEventSink(
                        PlacesByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    placesEventSink(PlacesByEntityUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RECORDING -> {
                    recordingsEventSink(
                        RecordingsByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    recordingsEventSink(RecordingsByEntityUiEvent.UpdateQuery(query))
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
                    seriesEventSink(
                        SeriesByEntityUiEvent.Get(
                            byEntityId = collectionId,
                            byEntity = MusicBrainzEntity.COLLECTION,
                            isRemote = isRemote,
                        ),
                    )
                    seriesEventSink(SeriesByEntityUiEvent.UpdateQuery(query))
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
            }
        }

        return CollectionUiState(
            title = title,
            collection = collection,
            url = getMusicBrainzUrl(MusicBrainzEntity.COLLECTION, screen.collectionId),
            actionableResult = actionableResult,
            topAppBarFilterState = topAppBarFilterState,
            topAppBarEditState = topAppBarEditState,
            areasByEntityUiState = areasByEntityUiState,
            artistsListUiState = artistsByEntityUiState,
            eventsByEntityUiState = eventsByEntityUiState,
            genresByEntityUiState = genresByEntityUiState,
            instrumentsByEntityUiState = instrumentsByEntityUiState,
            labelsByEntityUiState = labelsByEntityUiState,
            placesByEntityUiState = placesByEntityUiState,
            recordingsByEntityUiState = recordingsByEntityUiState,
            releasesByEntityUiState = releasesByEntityUiState,
            releaseGroupsByEntityUiState = releaseGroupsByEntityUiState,
            seriesByEntityUiState = seriesByEntityUiState,
            worksByEntityUiState = worksByEntityUiState,
            eventSink = ::eventSink,
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
    val areasByEntityUiState: AreasByEntityUiState,
    val artistsListUiState: ArtistsListUiState,
    val eventsByEntityUiState: EventsByEntityUiState,
    val genresByEntityUiState: GenresByEntityUiState,
    val instrumentsByEntityUiState: InstrumentsByEntityUiState,
    val labelsByEntityUiState: LabelsByEntityUiState,
    val placesByEntityUiState: PlacesByEntityUiState,
    val recordingsByEntityUiState: RecordingsByEntityUiState,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    val seriesByEntityUiState: SeriesByEntityUiState,
    val worksByEntityUiState: WorksByEntityUiState,
    val eventSink: (CollectionUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface CollectionUiEvent : CircuitUiEvent {
    data object NavigateUp : CollectionUiEvent

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

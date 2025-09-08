package ly.david.musicsearch.ui.common.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.ui.common.area.AreasListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.genre.GenresListPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsListPresenter
import ly.david.musicsearch.ui.common.label.LabelsListPresenter
import ly.david.musicsearch.ui.common.place.PlacesListPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsListPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListPresenter
import ly.david.musicsearch.ui.common.series.SeriesListPresenter
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiEvent
import ly.david.musicsearch.ui.common.work.WorksListPresenter

class AllEntitiesListPresenter(
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
    private val tracksByReleasePresenter: TracksByReleasePresenter,
    private val relationsPresenter: RelationsPresenter,
) : Presenter<AllEntitiesListUiState> {

    @Suppress("CyclomaticComplexMethod")
    @Composable
    override fun present(): AllEntitiesListUiState {
        var tab: Tab? by rememberSaveable { mutableStateOf(null) }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }
        var mostListenedTrackCount: Long by rememberSaveable { mutableLongStateOf(0) }

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
        val tracksByReleaseUiState = tracksByReleasePresenter.present()
        val tracksEventSink = tracksByReleaseUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(
            tab,
            browseMethod,
            query,
            isRemote,
        ) {
            val capturedBrowseMethod = browseMethod ?: return@LaunchedEffect
            when (tab) {
                Tab.AREAS -> {
                    areasEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    areasEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.ARTISTS -> {
                    artistsEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    artistsEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.EVENTS -> {
                    eventsEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    eventsEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.GENRES -> {
                    genresEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    genresEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.INSTRUMENTS -> {
                    instrumentsEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    instrumentsEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.LABELS -> {
                    labelsEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    labelsEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.PLACES -> {
                    placesEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    placesEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.RECORDINGS -> {
                    recordingsEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    recordingsEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.RELEASES -> {
                    releasesEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releasesEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.RELEASE_GROUPS -> {
                    releaseGroupsEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releaseGroupsEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.SERIES -> {
                    seriesEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    seriesEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.WORKS -> {
                    worksEventSink(
                        EntitiesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    worksEventSink(EntitiesListUiEvent.UpdateQuery(query))
                }

                Tab.RELATIONSHIPS -> {
                    val browseByEntity = capturedBrowseMethod as? BrowseMethod.ByEntity ?: return@LaunchedEffect
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = browseByEntity.entityId,
                            byEntity = browseByEntity.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                Tab.TRACKS -> {
                    val browseByEntity = capturedBrowseMethod as? BrowseMethod.ByEntity ?: return@LaunchedEffect
                    tracksEventSink(
                        TracksByReleaseUiEvent.Get(
                            byReleaseId = browseByEntity.entityId,
                            mostListenedTrackCount = mostListenedTrackCount,
                        ),
                    )
                    tracksEventSink(TracksByReleaseUiEvent.UpdateQuery(query))
                }

                Tab.DETAILS,
                Tab.STATS,
                null,
                -> {
                    // no-op
                }
            }
        }

        return AllEntitiesListUiState(
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
            relationsUiState = relationsUiState,
            tracksByReleaseUiState = tracksByReleaseUiState,
            eventSink = { event ->
                handleEvent(
                    event = event,
                    onTabChanged = { tab = it },
                    onBrowseMethodChanged = { browseMethod = it },
                    onQueryChanged = { query = it },
                    onIsRemoteChanged = { isRemote = it },
                    onMostListenedTrackCountChanged = { mostListenedTrackCount = it },
                )
            },
        )
    }

    private fun handleEvent(
        event: AllEntitiesListUiEvent,
        onTabChanged: (Tab?) -> Unit,
        onBrowseMethodChanged: (BrowseMethod?) -> Unit,
        onQueryChanged: (String) -> Unit,
        onIsRemoteChanged: (Boolean) -> Unit,
        onMostListenedTrackCountChanged: (Long) -> Unit,
    ) {
        when (event) {
            is AllEntitiesListUiEvent.Get -> {
                onTabChanged(event.tab)
                onBrowseMethodChanged(event.browseMethod)
                onQueryChanged(event.query)
                onIsRemoteChanged(event.isRemote)
                onMostListenedTrackCountChanged(event.mostListenedTrackCount)
            }
        }
    }
}

sealed interface AllEntitiesListUiEvent : CircuitUiEvent {
    data class Get(
        val tab: Tab?,
        val browseMethod: BrowseMethod,
        val query: String,
        val isRemote: Boolean,
        val mostListenedTrackCount: Long = 0,
    ) : AllEntitiesListUiEvent
}

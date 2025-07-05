package ly.david.musicsearch.ui.common.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.ui.common.area.AreasListPresenter
import ly.david.musicsearch.ui.common.area.AreasListUiEvent
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListUiEvent
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListUiEvent
import ly.david.musicsearch.ui.common.genre.GenresListPresenter
import ly.david.musicsearch.ui.common.genre.GenresListUiEvent
import ly.david.musicsearch.ui.common.instrument.InstrumentsListPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsListUiEvent
import ly.david.musicsearch.ui.common.label.LabelsListPresenter
import ly.david.musicsearch.ui.common.label.LabelsListUiEvent
import ly.david.musicsearch.ui.common.place.PlacesListPresenter
import ly.david.musicsearch.ui.common.place.PlacesListUiEvent
import ly.david.musicsearch.ui.common.recording.RecordingsListPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsListUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.series.SeriesListPresenter
import ly.david.musicsearch.ui.common.series.SeriesListUiEvent
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.track.TracksByEntityUiEvent
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.work.WorksListPresenter
import ly.david.musicsearch.ui.common.work.WorksListUiEvent

class EntitiesListPresenter(
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
) : Presenter<EntitiesListUiState> {

    @Suppress("CyclomaticComplexMethod")
    @Composable
    override fun present(): EntitiesListUiState {
        var tab: Tab? by rememberSaveable { mutableStateOf(null) }
        var browseMethod: BrowseMethod? by rememberSaveable(saver = BrowseMethodSaver) { mutableStateOf(null) }
        var query by rememberSaveable { mutableStateOf("") }
        var isRemote: Boolean by rememberSaveable { mutableStateOf(false) }

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
                        AreasListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    areasEventSink(AreasListUiEvent.UpdateQuery(query))
                }

                Tab.ARTISTS -> {
                    artistsEventSink(
                        ArtistsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    artistsEventSink(ArtistsListUiEvent.UpdateQuery(query))
                }

                Tab.EVENTS -> {
                    eventsEventSink(
                        EventsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    eventsEventSink(EventsListUiEvent.UpdateQuery(query))
                }

                Tab.GENRES -> {
                    genresEventSink(
                        GenresListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    genresEventSink(GenresListUiEvent.UpdateQuery(query))
                }

                Tab.INSTRUMENTS -> {
                    instrumentsEventSink(
                        InstrumentsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    instrumentsEventSink(InstrumentsListUiEvent.UpdateQuery(query))
                }

                Tab.LABELS -> {
                    labelsEventSink(
                        LabelsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    labelsEventSink(LabelsListUiEvent.UpdateQuery(query))
                }

                Tab.PLACES -> {
                    placesEventSink(
                        PlacesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    placesEventSink(PlacesListUiEvent.UpdateQuery(query))
                }

                Tab.RECORDINGS -> {
                    recordingsEventSink(
                        RecordingsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    recordingsEventSink(RecordingsListUiEvent.UpdateQuery(query))
                }

                Tab.RELEASES -> {
                    releasesEventSink(
                        ReleasesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releasesEventSink(ReleasesListUiEvent.UpdateQuery(query))
                }

                Tab.RELEASE_GROUPS -> {
                    releaseGroupsEventSink(
                        ReleaseGroupsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateQuery(query))
                }

                Tab.SERIES -> {
                    seriesEventSink(
                        SeriesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    seriesEventSink(SeriesListUiEvent.UpdateQuery(query))
                }

                Tab.WORKS -> {
                    worksEventSink(
                        WorksListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    worksEventSink(WorksListUiEvent.UpdateQuery(query))
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
                        TracksByEntityUiEvent.Get(
                            byEntityId = browseByEntity.entityId,
                        ),
                    )
                    tracksEventSink(TracksByEntityUiEvent.UpdateQuery(query))
                }

                Tab.DETAILS,
                Tab.STATS,
                null,
                -> {
                    // no-op
                }
            }
        }

        fun evenSink(event: EntitiesListUiEvent) {
            when (event) {
                is EntitiesListUiEvent.Get -> {
                    tab = event.tab
                    browseMethod = event.browseMethod
                    query = event.query
                    isRemote = event.isRemote
                }
            }
        }

        return EntitiesListUiState(
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
            eventSink = ::evenSink,
        )
    }
}

sealed interface EntitiesListUiEvent : CircuitUiEvent {
    data class Get(
        val tab: Tab?,
        val browseMethod: BrowseMethod,
        val query: String = "",
        val isRemote: Boolean,
    ) : EntitiesListUiEvent
}

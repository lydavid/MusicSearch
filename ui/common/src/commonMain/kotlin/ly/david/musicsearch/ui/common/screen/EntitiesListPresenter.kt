package ly.david.musicsearch.ui.common.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.shared.domain.BrowseMethod
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
import ly.david.musicsearch.ui.common.series.SeriesListPresenter
import ly.david.musicsearch.ui.common.series.SeriesListUiEvent
import ly.david.musicsearch.ui.common.series.SeriesListUiState
import ly.david.musicsearch.ui.common.topappbar.BrowseMethodSaver
import ly.david.musicsearch.ui.common.work.WorksListPresenter
import ly.david.musicsearch.ui.common.work.WorksListUiEvent
import ly.david.musicsearch.ui.common.work.WorksListUiState

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
) : Presenter<EntitiesListUiState> {

    @Suppress("CyclomaticComplexMethod")
    @Composable
    override fun present(): EntitiesListUiState {
        var entityTab: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
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

        LaunchedEffect(
            entityTab,
            browseMethod,
            query,
            isRemote,
        ) {
            val capturedEntityTab = entityTab ?: return@LaunchedEffect
            val capturedBrowseMethod = browseMethod ?: return@LaunchedEffect
            when (capturedEntityTab) {
                MusicBrainzEntity.AREA -> {
                    areasEventSink(
                        AreasListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    areasEventSink(AreasListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.ARTIST -> {
                    artistsEventSink(
                        ArtistsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    artistsEventSink(ArtistsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.EVENT -> {
                    eventsEventSink(
                        EventsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    eventsEventSink(EventsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.GENRE -> {
                    genresEventSink(
                        GenresListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    genresEventSink(GenresListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.INSTRUMENT -> {
                    instrumentsEventSink(
                        InstrumentsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    instrumentsEventSink(InstrumentsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.LABEL -> {
                    labelsEventSink(
                        LabelsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    labelsEventSink(LabelsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.PLACE -> {
                    placesEventSink(
                        PlacesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    placesEventSink(PlacesListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RECORDING -> {
                    recordingsEventSink(
                        RecordingsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    recordingsEventSink(RecordingsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RELEASE -> {
                    releasesEventSink(
                        ReleasesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releasesEventSink(ReleasesListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.RELEASE_GROUP -> {
                    releaseGroupsEventSink(
                        ReleaseGroupsListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.SERIES -> {
                    seriesEventSink(
                        SeriesListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    seriesEventSink(SeriesListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.WORK -> {
                    worksEventSink(
                        WorksListUiEvent.Get(
                            browseMethod = capturedBrowseMethod,
                            isRemote = isRemote,
                        ),
                    )
                    worksEventSink(WorksListUiEvent.UpdateQuery(query))
                }

                MusicBrainzEntity.COLLECTION,
                MusicBrainzEntity.URL,
                -> {
                    error("$capturedEntityTab by collection not supported")
                }
            }
        }

        fun evenSink(event: EntitiesListUiEvent) {
            when (event) {
                is EntitiesListUiEvent.Get -> {
                    entityTab = event.entityTab
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
            eventSink = ::evenSink,
        )
    }
}

@Stable
data class EntitiesListUiState(
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
    val eventSink: (EntitiesListUiEvent) -> Unit = {},
) : CircuitUiState

sealed interface EntitiesListUiEvent : CircuitUiEvent {
    data class Get(
        val entityTab: MusicBrainzEntity,
        val browseMethod: BrowseMethod,
        val query: String = "",
        val isRemote: Boolean,
    ) : EntitiesListUiEvent
}

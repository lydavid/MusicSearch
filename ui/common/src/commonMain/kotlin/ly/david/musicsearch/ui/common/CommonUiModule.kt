package ly.david.musicsearch.ui.common

import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import ly.david.musicsearch.ui.common.area.AreasListPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsListPresenter
import ly.david.musicsearch.ui.common.event.EventsListPresenter
import ly.david.musicsearch.ui.common.genre.GenresListPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsListPresenter
import ly.david.musicsearch.ui.common.label.LabelsListPresenter
import ly.david.musicsearch.ui.common.list.AllEntitiesListPresenter
import ly.david.musicsearch.ui.common.place.PlacesListPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsListPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenterImpl
import ly.david.musicsearch.ui.common.release.ReleaseStatusesBottomSheetContent
import ly.david.musicsearch.ui.common.release.ReleaseStatusesPresenter
import ly.david.musicsearch.ui.common.release.ReleaseStatusesUiState
import ly.david.musicsearch.ui.common.release.ReleasesListPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListPresenter
import ly.david.musicsearch.ui.common.screen.ReleaseStatusesScreen
import ly.david.musicsearch.ui.common.series.SeriesListPresenter
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.work.WorksListPresenter
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val commonUiModule = module {
    includes(platformModule)

    singleOf(::AreasListPresenter)
    singleOf(::ArtistsListPresenter)
    singleOf(::EventsListPresenter)
    singleOf(::GenresListPresenter)
    singleOf(::InstrumentsListPresenter)
    singleOf(::LabelsListPresenter)
    singleOf(::PlacesListPresenter)
    singleOf(::RecordingsListPresenter)
    singleOf(::RelationsPresenterImpl) bind RelationsPresenter::class
    singleOf(::ReleasesListPresenter)
    singleOf(::ReleaseGroupsListPresenter)
    singleOf(::TracksByReleasePresenter)
    singleOf(::SeriesListPresenter)
    singleOf(::WorksListPresenter)
    singleOf(::AllEntitiesListPresenter)

    single(named("ReleaseStatusesScreen")) {
        Presenter.Factory { screen, _, _ ->
            when (screen) {
                is ReleaseStatusesScreen -> {
                    ReleaseStatusesPresenter(
                        screen = screen,
                        appPreferences = get(),
                        observeCountOfEachStatus = get(),
                    )
                }

                else -> null
            }
        }
    }
    single(named("ReleaseStatusesScreen")) {
        Ui.Factory { screen, _ ->
            when (screen) {
                is ReleaseStatusesScreen -> {
                    ui<ReleaseStatusesUiState> { state, _ ->
                        ReleaseStatusesBottomSheetContent(
                            state = state,
                        )
                    }
                }

                else -> null
            }
        }
    }
}

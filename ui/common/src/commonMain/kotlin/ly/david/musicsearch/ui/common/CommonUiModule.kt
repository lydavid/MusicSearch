package ly.david.musicsearch.ui.common

import ly.david.musicsearch.ui.common.area.AreasByEntityPresenter
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityPresenter
import ly.david.musicsearch.ui.common.event.EventsByEntityPresenter
import ly.david.musicsearch.ui.common.genre.GenresByEntityPresenter
import ly.david.musicsearch.ui.common.instrument.InstrumentsByEntityPresenter
import ly.david.musicsearch.ui.common.label.LabelsByEntityPresenter
import ly.david.musicsearch.ui.common.place.PlacesByEntityPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenterImpl
import ly.david.musicsearch.ui.common.release.ReleasesByEntityPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityPresenter
import ly.david.musicsearch.ui.common.series.SeriesByEntityPresenter
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.work.WorksByEntityPresenter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonUiModule = module {
    includes(platformModule)

    singleOf(::AreasByEntityPresenter)
    singleOf(::ArtistsByEntityPresenter)
    singleOf(::EventsByEntityPresenter)
    singleOf(::GenresByEntityPresenter)
    singleOf(::InstrumentsByEntityPresenter)
    singleOf(::LabelsByEntityPresenter)
    singleOf(::PlacesByEntityPresenter)
    singleOf(::RecordingsByEntityPresenter)
    singleOf(::RelationsPresenterImpl) bind RelationsPresenter::class
    singleOf(::ReleasesByEntityPresenter)
    singleOf(::ReleaseGroupsByEntityPresenter)
    singleOf(::TracksByReleasePresenter)
    singleOf(::SeriesByEntityPresenter)
    singleOf(::WorksByEntityPresenter)
}

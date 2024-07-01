package ly.david.musicsearch.ui.common

import ly.david.musicsearch.ui.common.artist.ArtistsByEntityPresenter
import ly.david.musicsearch.ui.common.event.EventsByEntityPresenter
import ly.david.musicsearch.ui.common.label.LabelsByEntityPresenter
import ly.david.musicsearch.ui.common.paging.RelationsList
import ly.david.musicsearch.ui.common.place.PlacesByEntityPresenter
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityPresenter
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.release.ReleasesByEntityPresenter
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityPresenter
import ly.david.musicsearch.ui.common.track.TracksByReleasePresenter
import ly.david.musicsearch.ui.common.work.WorksByEntityPresenter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonUiModule = module {
    singleOf(::ArtistsByEntityPresenter)
    singleOf(::EventsByEntityPresenter)
    singleOf(::LabelsByEntityPresenter)
    singleOf(::PlacesByEntityPresenter)
    singleOf(::RecordingsByEntityPresenter)
    singleOf(::RelationsPresenter)
    singleOf(::ReleasesByEntityPresenter)
    singleOf(::ReleaseGroupsByEntityPresenter)
    singleOf(::TracksByReleasePresenter)
    singleOf(::WorksByEntityPresenter)

    factory {
        RelationsList(get())
    }
}

package ly.david.ui.common

import ly.david.ui.common.artist.ArtistsByEntityPresenter
import ly.david.ui.common.event.EventsByEntityPresenter
import ly.david.ui.common.label.LabelsByEntityPresenter
import ly.david.ui.common.paging.RelationsList
import ly.david.ui.common.place.PlacesByEntityPresenter
import ly.david.ui.common.recording.RecordingsByEntityPresenter
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityPresenter
import ly.david.ui.common.track.TracksByReleasePresenter
import ly.david.ui.common.work.WorksByEntityPresenter
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

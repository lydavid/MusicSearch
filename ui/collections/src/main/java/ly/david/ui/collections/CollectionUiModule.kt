package ly.david.ui.collections

import ly.david.ui.collections.areas.AreasByCollectionViewModel
import ly.david.ui.collections.artists.ArtistsByCollectionViewModel
import ly.david.ui.collections.events.EventsByCollectionViewModel
import ly.david.ui.collections.instruments.InstrumentsByCollectionViewModel
import ly.david.ui.collections.labels.LabelsByCollectionViewModel
import ly.david.ui.collections.places.PlacesByCollectionViewModel
import ly.david.ui.collections.recordings.RecordingsByCollectionViewModel
import ly.david.ui.collections.releasegroups.ReleaseGroupsByCollectionViewModel
import ly.david.ui.collections.releases.ReleasesByCollectionViewModel
import ly.david.ui.collections.series.SeriesByCollectionViewModel
import ly.david.ui.collections.works.WorksByCollectionViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module

@Module
@ComponentScan
class CollectionUiModule

val collectionUiModule = module {
    viewModelOf(::AreasByCollectionViewModel)
    viewModelOf(::ArtistsByCollectionViewModel)
    viewModelOf(::EventsByCollectionViewModel)
    viewModelOf(::InstrumentsByCollectionViewModel)
    viewModelOf(::LabelsByCollectionViewModel)
    viewModelOf(::PlacesByCollectionViewModel)
    viewModelOf(::RecordingsByCollectionViewModel)
    viewModelOf(::ReleasesByCollectionViewModel)
    viewModelOf(::ReleaseGroupsByCollectionViewModel)
    viewModelOf(::SeriesByCollectionViewModel)
    viewModelOf(::WorksByCollectionViewModel)
    viewModelOf(::CollectionListViewModel)
}

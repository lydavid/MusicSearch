package ly.david.musicsearch.data.repository.di

import ly.david.musicsearch.data.repository.AreaRepositoryImpl
import ly.david.musicsearch.data.repository.ArtistRepositoryImpl
import ly.david.musicsearch.data.repository.BrowseEntityCountRepositoryImpl
import ly.david.musicsearch.data.repository.CollectionRepositoryImpl
import ly.david.musicsearch.data.repository.EventRepositoryImpl
import ly.david.musicsearch.data.repository.InstrumentRepositoryImpl
import ly.david.musicsearch.data.repository.LabelRepositoryImpl
import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.NowPlayingHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.PlaceRepositoryImpl
import ly.david.musicsearch.data.repository.RecordingRepositoryImpl
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.SearchHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.SearchResultsRepositoryImpl
import ly.david.musicsearch.data.repository.SeriesRepositoryImpl
import ly.david.musicsearch.data.repository.WorkRepositoryImpl
import ly.david.musicsearch.data.repository.event.EventsByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.place.PlacesByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.release.ReleaseRepositoryImpl
import ly.david.musicsearch.data.repository.release.ReleasesByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.releasegroup.ReleaseGroupRepositoryImpl
import ly.david.musicsearch.data.repository.releasegroup.ReleaseGroupsByEntityRepositoryImpl
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.artist.ArtistRepository
import ly.david.musicsearch.domain.browse.BrowseEntityCountRepository
import ly.david.musicsearch.domain.collection.CollectionRepository
import ly.david.musicsearch.domain.event.EventRepository
import ly.david.musicsearch.domain.event.EventsByEntityRepository
import ly.david.musicsearch.domain.history.LookupHistoryRepository
import ly.david.musicsearch.domain.instrument.InstrumentRepository
import ly.david.musicsearch.domain.label.LabelRepository
import ly.david.musicsearch.domain.nowplaying.NowPlayingHistoryRepository
import ly.david.musicsearch.domain.place.PlaceRepository
import ly.david.musicsearch.domain.place.PlacesByEntityRepository
import ly.david.musicsearch.domain.recording.RecordingRepository
import ly.david.musicsearch.domain.relation.RelationRepository
import ly.david.musicsearch.domain.release.ReleaseRepository
import ly.david.musicsearch.domain.release.ReleasesByEntityRepository
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupsByEntityRepository
import ly.david.musicsearch.domain.search.history.SearchHistoryRepository
import ly.david.musicsearch.domain.search.results.SearchResultsRepository
import ly.david.musicsearch.domain.series.SeriesRepository
import ly.david.musicsearch.domain.work.WorkRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDataModule = module {
    singleOf(::AreaRepositoryImpl) bind AreaRepository::class
    singleOf(::ArtistRepositoryImpl) bind ArtistRepository::class
    singleOf(::BrowseEntityCountRepositoryImpl) bind BrowseEntityCountRepository::class
    singleOf(::CollectionRepositoryImpl) bind CollectionRepository::class
    singleOf(::EventRepositoryImpl) bind EventRepository::class
    singleOf(::EventsByEntityRepositoryImpl) bind EventsByEntityRepository::class
    singleOf(::InstrumentRepositoryImpl) bind InstrumentRepository::class
    singleOf(::LabelRepositoryImpl) bind LabelRepository::class
    singleOf(::LookupHistoryRepositoryImpl) bind LookupHistoryRepository::class
    singleOf(::NowPlayingHistoryRepositoryImpl) bind NowPlayingHistoryRepository::class
    singleOf(::PlaceRepositoryImpl) bind PlaceRepository::class
    singleOf(::PlacesByEntityRepositoryImpl) bind PlacesByEntityRepository::class
    singleOf(::RecordingRepositoryImpl) bind RecordingRepository::class
    singleOf(::RelationRepositoryImpl) bind RelationRepository::class
    singleOf(::ReleaseRepositoryImpl) bind ReleaseRepository::class
    singleOf(::ReleasesByEntityRepositoryImpl) bind ReleasesByEntityRepository::class
    singleOf(::ReleaseGroupRepositoryImpl) bind ReleaseGroupRepository::class
    singleOf(::ReleaseGroupsByEntityRepositoryImpl) bind ReleaseGroupsByEntityRepository::class
    singleOf(::SearchResultsRepositoryImpl) bind SearchResultsRepository::class
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
    singleOf(::SeriesRepositoryImpl) bind SeriesRepository::class
    singleOf(::WorkRepositoryImpl) bind WorkRepository::class
}

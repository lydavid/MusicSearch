package ly.david.musicsearch.data.repository.di

import ly.david.musicsearch.data.repository.BrowseEntityCountRepositoryImpl
import ly.david.musicsearch.data.repository.collection.CollectionRepositoryImpl
import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.NowPlayingHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.search.SearchHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.search.SearchResultsRepositoryImpl
import ly.david.musicsearch.data.repository.SpotifyHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.area.AreaRepositoryImpl
import ly.david.musicsearch.data.repository.area.AreasByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.artist.ArtistCollaborationRepositoryImpl
import ly.david.musicsearch.data.repository.artist.ArtistRepositoryImpl
import ly.david.musicsearch.data.repository.artist.ArtistsByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.event.EventRepositoryImpl
import ly.david.musicsearch.data.repository.event.EventsByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.genre.GenresByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.genre.GenreRepositoryImpl
import ly.david.musicsearch.data.repository.image.ImageMetadataRepositoryImpl
import ly.david.musicsearch.data.repository.instrument.InstrumentRepositoryImpl
import ly.david.musicsearch.data.repository.instrument.InstrumentsByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.label.LabelRepositoryImpl
import ly.david.musicsearch.data.repository.label.LabelsByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.metadata.MetadataRepositoryImpl
import ly.david.musicsearch.data.repository.place.PlaceRepositoryImpl
import ly.david.musicsearch.data.repository.place.PlacesByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.recording.RecordingRepositoryImpl
import ly.david.musicsearch.data.repository.recording.RecordingsByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.release.ReleaseRepositoryImpl
import ly.david.musicsearch.data.repository.release.ReleasesByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.releasegroup.ReleaseGroupRepositoryImpl
import ly.david.musicsearch.data.repository.releasegroup.ReleaseGroupsByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.series.SeriesByEntityRepositoryImpl
import ly.david.musicsearch.data.repository.series.SeriesRepositoryImpl
import ly.david.musicsearch.data.repository.work.WorkRepositoryImpl
import ly.david.musicsearch.data.repository.work.WorksByEntityRepositoryImpl
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.area.AreasByEntityRepository
import ly.david.musicsearch.shared.domain.artist.ArtistCollaborationRepository
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.artist.ArtistsByEntityRepository
import ly.david.musicsearch.shared.domain.browse.BrowseEntityCountRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.genre.GenresByEntityRepository
import ly.david.musicsearch.shared.domain.genre.GenreRepository
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentsByEntityRepository
import ly.david.musicsearch.shared.domain.label.LabelRepository
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository
import ly.david.musicsearch.shared.domain.metadata.MetadataRepository
import ly.david.musicsearch.shared.domain.nowplaying.NowPlayingHistoryRepository
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.place.PlacesByEntityRepository
import ly.david.musicsearch.shared.domain.recording.RecordingRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsByEntityRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import ly.david.musicsearch.shared.domain.release.ReleasesByEntityRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsByEntityRepository
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository
import ly.david.musicsearch.shared.domain.series.SeriesByEntityRepository
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import ly.david.musicsearch.shared.domain.spotify.SpotifyHistoryRepository
import ly.david.musicsearch.shared.domain.work.WorkRepository
import ly.david.musicsearch.shared.domain.work.WorksByEntityRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDataModule = module {
    singleOf(::AreaRepositoryImpl) bind AreaRepository::class
    singleOf(::AreasByEntityRepositoryImpl) bind AreasByEntityRepository::class
    singleOf(::ArtistRepositoryImpl) bind ArtistRepository::class
    singleOf(::ArtistsByEntityRepositoryImpl) bind ArtistsByEntityRepository::class
    singleOf(::ArtistCollaborationRepositoryImpl) bind ArtistCollaborationRepository::class
    singleOf(::BrowseEntityCountRepositoryImpl) bind BrowseEntityCountRepository::class
    singleOf(::CollectionRepositoryImpl) bind CollectionRepository::class
    singleOf(::EventRepositoryImpl) bind EventRepository::class
    singleOf(::EventsByEntityRepositoryImpl) bind EventsByEntityRepository::class
    singleOf(::GenresByEntityRepositoryImpl) bind GenresByEntityRepository::class
    singleOf(::InstrumentRepositoryImpl) bind InstrumentRepository::class
    singleOf(::InstrumentsByEntityRepositoryImpl) bind InstrumentsByEntityRepository::class
    singleOf(::GenreRepositoryImpl) bind GenreRepository::class
    singleOf(::LabelRepositoryImpl) bind LabelRepository::class
    singleOf(::LabelsByEntityRepositoryImpl) bind LabelsByEntityRepository::class
    singleOf(::LookupHistoryRepositoryImpl) bind LookupHistoryRepository::class
    singleOf(::NowPlayingHistoryRepositoryImpl) bind NowPlayingHistoryRepository::class
    singleOf(::PlaceRepositoryImpl) bind PlaceRepository::class
    singleOf(::PlacesByEntityRepositoryImpl) bind PlacesByEntityRepository::class
    singleOf(::RecordingRepositoryImpl) bind RecordingRepository::class
    singleOf(::RecordingsByEntityRepositoryImpl) bind RecordingsByEntityRepository::class
    singleOf(::RelationRepositoryImpl) bind RelationRepository::class
    singleOf(::ReleaseRepositoryImpl) bind ReleaseRepository::class
    singleOf(::ReleasesByEntityRepositoryImpl) bind ReleasesByEntityRepository::class
    singleOf(::ReleaseGroupRepositoryImpl) bind ReleaseGroupRepository::class
    singleOf(::ReleaseGroupsByEntityRepositoryImpl) bind ReleaseGroupsByEntityRepository::class
    singleOf(::SearchResultsRepositoryImpl) bind SearchResultsRepository::class
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
    singleOf(::SpotifyHistoryRepositoryImpl) bind SpotifyHistoryRepository::class
    singleOf(::SeriesRepositoryImpl) bind SeriesRepository::class
    singleOf(::SeriesByEntityRepositoryImpl) bind SeriesByEntityRepository::class
    singleOf(::WorkRepositoryImpl) bind WorkRepository::class
    singleOf(::WorksByEntityRepositoryImpl) bind WorksByEntityRepository::class
    singleOf(::MetadataRepositoryImpl) bind MetadataRepository::class
    singleOf(::ImageMetadataRepositoryImpl) bind ImageMetadataRepository::class
}

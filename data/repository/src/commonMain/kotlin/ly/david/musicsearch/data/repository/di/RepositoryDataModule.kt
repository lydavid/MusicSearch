package ly.david.musicsearch.data.repository.di

import ly.david.musicsearch.data.repository.BrowseRemoteMetadataRepositoryImpl
import ly.david.musicsearch.data.repository.LookupHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.NowPlayingHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.SpotifyHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.area.AreaRepositoryImpl
import ly.david.musicsearch.data.repository.area.AreasListRepositoryImpl
import ly.david.musicsearch.data.repository.artist.ArtistCollaborationRepositoryImpl
import ly.david.musicsearch.data.repository.artist.ArtistRepositoryImpl
import ly.david.musicsearch.data.repository.artist.ArtistsListRepositoryImpl
import ly.david.musicsearch.data.repository.collection.CollectionRepositoryImpl
import ly.david.musicsearch.data.repository.event.EventRepositoryImpl
import ly.david.musicsearch.data.repository.event.EventsListRepositoryImpl
import ly.david.musicsearch.data.repository.genre.GenreRepositoryImpl
import ly.david.musicsearch.data.repository.genre.GenresListRepositoryImpl
import ly.david.musicsearch.data.repository.image.MusicBrainzImageMetadataRepositoryImpl
import ly.david.musicsearch.data.repository.instrument.InstrumentRepositoryImpl
import ly.david.musicsearch.data.repository.instrument.InstrumentsListRepositoryImpl
import ly.david.musicsearch.data.repository.label.LabelRepositoryImpl
import ly.david.musicsearch.data.repository.label.LabelsListRepositoryImpl
import ly.david.musicsearch.data.repository.list.EntitiesListRepositoryImpl
import ly.david.musicsearch.data.repository.list.ObserveLocalCountImpl
import ly.david.musicsearch.data.repository.list.ObserveVisitedCountImpl
import ly.david.musicsearch.data.repository.metadata.MetadataRepositoryImpl
import ly.david.musicsearch.data.repository.place.PlaceRepositoryImpl
import ly.david.musicsearch.data.repository.place.PlacesListRepositoryImpl
import ly.david.musicsearch.data.repository.recording.RecordingRepositoryImpl
import ly.david.musicsearch.data.repository.recording.RecordingsListRepositoryImpl
import ly.david.musicsearch.data.repository.release.ReleaseRepositoryImpl
import ly.david.musicsearch.data.repository.release.ReleasesListRepositoryImpl
import ly.david.musicsearch.data.repository.releasegroup.ObserveCountOfEachAlbumTypeImpl
import ly.david.musicsearch.data.repository.releasegroup.ReleaseGroupRepositoryImpl
import ly.david.musicsearch.data.repository.releasegroup.ReleaseGroupsListRepositoryImpl
import ly.david.musicsearch.data.repository.search.SearchHistoryRepositoryImpl
import ly.david.musicsearch.data.repository.search.SearchResultsRepositoryImpl
import ly.david.musicsearch.data.repository.series.SeriesListRepositoryImpl
import ly.david.musicsearch.data.repository.series.SeriesRepositoryImpl
import ly.david.musicsearch.data.repository.work.WorkRepositoryImpl
import ly.david.musicsearch.data.repository.work.WorksListRepositoryImpl
import ly.david.musicsearch.shared.domain.area.AreaRepository
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.artist.ArtistCollaborationRepository
import ly.david.musicsearch.shared.domain.artist.ArtistRepository
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.browse.BrowseRemoteMetadataRepository
import ly.david.musicsearch.shared.domain.collection.CollectionRepository
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.genre.GenreRepository
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.history.LookupHistoryRepository
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepositoryImpl
import ly.david.musicsearch.shared.domain.image.MusicBrainzImageMetadataRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.shared.domain.instrument.InstrumentsListRepository
import ly.david.musicsearch.shared.domain.label.LabelRepository
import ly.david.musicsearch.shared.domain.label.LabelsListRepository
import ly.david.musicsearch.shared.domain.list.EntitiesListRepository
import ly.david.musicsearch.shared.domain.list.ObserveLocalCount
import ly.david.musicsearch.shared.domain.list.ObserveVisitedCount
import ly.david.musicsearch.shared.domain.metadata.MetadataRepository
import ly.david.musicsearch.shared.domain.nowplaying.NowPlayingHistoryRepository
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import ly.david.musicsearch.shared.domain.recording.RecordingRepository
import ly.david.musicsearch.shared.domain.recording.RecordingsListRepository
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.release.ReleaseRepository
import ly.david.musicsearch.shared.domain.release.ReleasesListRepository
import ly.david.musicsearch.shared.domain.releasegroup.ObserveCountOfEachAlbumType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupRepository
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository
import ly.david.musicsearch.shared.domain.series.SeriesListRepository
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import ly.david.musicsearch.shared.domain.spotify.SpotifyHistoryRepository
import ly.david.musicsearch.shared.domain.work.WorkRepository
import ly.david.musicsearch.shared.domain.work.WorksListRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryDataModule = module {
    singleOf(::AreaRepositoryImpl) bind AreaRepository::class
    singleOf(::AreasListRepositoryImpl) bind AreasListRepository::class
    singleOf(::ArtistRepositoryImpl) bind ArtistRepository::class
    singleOf(::ArtistsListRepositoryImpl) bind ArtistsListRepository::class
    singleOf(::ArtistCollaborationRepositoryImpl) bind ArtistCollaborationRepository::class
    singleOf(::BrowseRemoteMetadataRepositoryImpl) bind BrowseRemoteMetadataRepository::class
    singleOf(::CollectionRepositoryImpl) bind CollectionRepository::class
    singleOf(::EventRepositoryImpl) bind EventRepository::class
    singleOf(::EventsListRepositoryImpl) bind EventsListRepository::class
    singleOf(::GenresListRepositoryImpl) bind GenresListRepository::class
    singleOf(::InstrumentRepositoryImpl) bind InstrumentRepository::class
    singleOf(::InstrumentsListRepositoryImpl) bind InstrumentsListRepository::class
    singleOf(::GenreRepositoryImpl) bind GenreRepository::class
    singleOf(::LabelRepositoryImpl) bind LabelRepository::class
    singleOf(::LabelsListRepositoryImpl) bind LabelsListRepository::class
    singleOf(::LookupHistoryRepositoryImpl) bind LookupHistoryRepository::class
    singleOf(::NowPlayingHistoryRepositoryImpl) bind NowPlayingHistoryRepository::class
    singleOf(::PlaceRepositoryImpl) bind PlaceRepository::class
    singleOf(::PlacesListRepositoryImpl) bind PlacesListRepository::class
    singleOf(::RecordingRepositoryImpl) bind RecordingRepository::class
    singleOf(::RecordingsListRepositoryImpl) bind RecordingsListRepository::class
    singleOf(::RelationRepositoryImpl) bind RelationRepository::class
    singleOf(::ReleaseRepositoryImpl) bind ReleaseRepository::class
    singleOf(::ReleasesListRepositoryImpl) bind ReleasesListRepository::class
    singleOf(::ReleaseGroupRepositoryImpl) bind ReleaseGroupRepository::class
    singleOf(::ReleaseGroupsListRepositoryImpl) bind ReleaseGroupsListRepository::class
    singleOf(::ObserveCountOfEachAlbumTypeImpl) bind ObserveCountOfEachAlbumType::class
    singleOf(::SearchResultsRepositoryImpl) bind SearchResultsRepository::class
    singleOf(::SearchHistoryRepositoryImpl) bind SearchHistoryRepository::class
    singleOf(::SpotifyHistoryRepositoryImpl) bind SpotifyHistoryRepository::class
    singleOf(::SeriesRepositoryImpl) bind SeriesRepository::class
    singleOf(::SeriesListRepositoryImpl) bind SeriesListRepository::class
    singleOf(::WorkRepositoryImpl) bind WorkRepository::class
    singleOf(::WorksListRepositoryImpl) bind WorksListRepository::class
    singleOf(::EntitiesListRepositoryImpl) bind EntitiesListRepository::class
    singleOf(::MetadataRepositoryImpl) bind MetadataRepository::class
    singleOf(::ImageMetadataRepositoryImpl) bind ImageMetadataRepository::class
    singleOf(::MusicBrainzImageMetadataRepositoryImpl) bind MusicBrainzImageMetadataRepository::class
    singleOf(::ObserveLocalCountImpl) bind ObserveLocalCount::class
    singleOf(::ObserveVisitedCountImpl) bind ObserveVisitedCount::class
}

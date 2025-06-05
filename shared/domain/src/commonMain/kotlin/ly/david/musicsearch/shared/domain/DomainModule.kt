package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.area.usecase.GetAreas
import ly.david.musicsearch.shared.domain.area.usecase.GetAreasImpl
import ly.david.musicsearch.shared.domain.artist.usecase.GetArtists
import ly.david.musicsearch.shared.domain.artist.usecase.GetArtistsImpl
import ly.david.musicsearch.shared.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.DeleteCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.collection.usecase.GetCollection
import ly.david.musicsearch.shared.domain.event.usecase.GetEvents
import ly.david.musicsearch.shared.domain.event.usecase.GetEventsImpl
import ly.david.musicsearch.shared.domain.genre.usecase.GetGenres
import ly.david.musicsearch.shared.domain.genre.usecase.GetGenresImpl
import ly.david.musicsearch.shared.domain.history.usecase.DeleteLookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.DeleteLookupHistoryImpl
import ly.david.musicsearch.shared.domain.history.usecase.GetPagedHistory
import ly.david.musicsearch.shared.domain.history.usecase.GetPagedHistoryImpl
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistoryImpl
import ly.david.musicsearch.shared.domain.history.usecase.MarkLookupHistoryForDeletion
import ly.david.musicsearch.shared.domain.history.usecase.MarkLookupHistoryForDeletionImpl
import ly.david.musicsearch.shared.domain.history.usecase.UnMarkLookupHistoryForDeletion
import ly.david.musicsearch.shared.domain.history.usecase.UnMarkLookupHistoryForDeletionImpl
import ly.david.musicsearch.shared.domain.instrument.usecase.GetInstruments
import ly.david.musicsearch.shared.domain.instrument.usecase.GetInstrumentsImpl
import ly.david.musicsearch.shared.domain.label.usecase.GetLabels
import ly.david.musicsearch.shared.domain.label.usecase.GetLabelsImpl
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzCoverArtUrl
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrlImpl
import ly.david.musicsearch.shared.domain.nowplaying.usecase.DeleteNowPlayingHistory
import ly.david.musicsearch.shared.domain.nowplaying.usecase.GetNowPlayingHistory
import ly.david.musicsearch.shared.domain.place.usecase.GetPlaces
import ly.david.musicsearch.shared.domain.place.usecase.GetPlacesImpl
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordings
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordingsImpl
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationshipsImpl
import ly.david.musicsearch.shared.domain.release.usecase.GetReleases
import ly.david.musicsearch.shared.domain.release.usecase.GetReleasesImpl
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByReleaseImpl
import ly.david.musicsearch.shared.domain.releasegroup.usecase.GetReleaseGroups
import ly.david.musicsearch.shared.domain.releasegroup.usecase.GetReleaseGroupsImpl
import ly.david.musicsearch.shared.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.shared.domain.search.results.usecase.GetSearchResults
import ly.david.musicsearch.shared.domain.series.usecase.GetSeries
import ly.david.musicsearch.shared.domain.series.usecase.GetSeriesImpl
import ly.david.musicsearch.shared.domain.work.usecase.GetWorks
import ly.david.musicsearch.shared.domain.work.usecase.GetWorksImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetAreasImpl) bind GetAreas::class
    singleOf(::GetArtistsImpl) bind GetArtists::class
    singleOf(::GetEventsImpl) bind GetEvents::class
    singleOf(::GetGenresImpl) bind GetGenres::class
    singleOf(::GetInstrumentsImpl) bind GetInstruments::class
    singleOf(::GetLabelsImpl) bind GetLabels::class
    singleOf(::GetPlacesImpl) bind GetPlaces::class
    singleOf(::GetRecordingsImpl) bind GetRecordings::class
    singleOf(::GetReleasesImpl) bind GetReleases::class
    singleOf(::GetReleaseGroupsImpl) bind GetReleaseGroups::class
    singleOf(::GetSeriesImpl) bind GetSeries::class
    singleOf(::GetWorksImpl) bind GetWorks::class

    singleOf(::ObserveBrowseEntityCount)
    singleOf(::CreateCollection)
    singleOf(::DeleteCollection)
    singleOf(::GetAllCollections)
    singleOf(::GetCollection)
    singleOf(::GetCollection)
    singleOf(::DeleteLookupHistoryImpl) bind DeleteLookupHistory::class
    singleOf(::GetPagedHistoryImpl) bind GetPagedHistory::class
    singleOf(::IncrementLookupHistoryImpl) bind IncrementLookupHistory::class
    singleOf(::MarkLookupHistoryForDeletionImpl) bind MarkLookupHistoryForDeletion::class
    singleOf(::UnMarkLookupHistoryForDeletionImpl) bind UnMarkLookupHistoryForDeletion::class
    singleOf(::DeleteNowPlayingHistory)
    singleOf(::GetNowPlayingHistory)
    singleOf(::GetCountOfEachRelationshipTypeUseCase)
    singleOf(::GetEntityRelationshipsImpl) bind GetEntityRelationships::class
    singleOf(::GetTracksByReleaseImpl) bind GetTracksByRelease::class
    singleOf(::DeleteSearchHistory)
    singleOf(::GetSearchHistory)
    singleOf(::RecordSearchHistory)
    singleOf(::GetSearchResults)
    singleOf(::GetMusicBrainzUrlImpl) bind GetMusicBrainzUrl::class
    singleOf(::GetMusicBrainzCoverArtUrl)
}

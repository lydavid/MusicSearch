package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.area.usecase.GetAreasByEntity
import ly.david.musicsearch.shared.domain.artist.usecase.GetArtistsByEntity
import ly.david.musicsearch.shared.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.DeleteFromCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.collection.usecase.GetCollection
import ly.david.musicsearch.shared.domain.event.usecase.GetEventsByEntity
import ly.david.musicsearch.shared.domain.history.usecase.DeleteLookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.GetPagedHistory
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.history.usecase.MarkLookupHistoryForDeletion
import ly.david.musicsearch.shared.domain.history.usecase.UnMarkLookupHistoryForDeletion
import ly.david.musicsearch.shared.domain.instrument.usecase.GetInstrumentsByEntity
import ly.david.musicsearch.shared.domain.label.usecase.GetLabelsByEntity
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.nowplaying.usecase.DeleteNowPlayingHistory
import ly.david.musicsearch.shared.domain.nowplaying.usecase.GetNowPlayingHistory
import ly.david.musicsearch.shared.domain.place.usecase.GetPlacesByEntity
import ly.david.musicsearch.shared.domain.recording.usecase.GetRecordingsByEntity
import ly.david.musicsearch.shared.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships
import ly.david.musicsearch.shared.domain.release.usecase.GetReleasesByEntity
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease
import ly.david.musicsearch.shared.domain.releasegroup.usecase.GetReleaseGroupsByEntity
import ly.david.musicsearch.shared.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.shared.domain.search.results.usecase.GetSearchResults
import ly.david.musicsearch.shared.domain.series.usecase.GetSeriesByEntity
import ly.david.musicsearch.shared.domain.work.usecase.GetWorksByEntity
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetAreasByEntity)
    singleOf(::GetArtistsByEntity)
    singleOf(::ObserveBrowseEntityCount)
    singleOf(::CreateCollection)
    singleOf(::DeleteFromCollection)
    singleOf(::GetAllCollections)
    singleOf(::GetCollection)
    singleOf(::GetCollection)
    singleOf(::GetEventsByEntity)
    singleOf(::DeleteLookupHistory)
    singleOf(::GetPagedHistory)
    singleOf(::IncrementLookupHistory)
    singleOf(::MarkLookupHistoryForDeletion)
    singleOf(::UnMarkLookupHistoryForDeletion)
    singleOf(::GetInstrumentsByEntity)
    singleOf(::GetLabelsByEntity)
    singleOf(::DeleteNowPlayingHistory)
    singleOf(::GetNowPlayingHistory)
    singleOf(::GetPlacesByEntity)
    singleOf(::GetRecordingsByEntity)
    singleOf(::GetCountOfEachRelationshipTypeUseCase)
    singleOf(::GetEntityRelationships)
    singleOf(::GetReleasesByEntity)
    singleOf(::GetTracksByRelease)
    singleOf(::GetReleaseGroupsByEntity)
    singleOf(::DeleteSearchHistory)
    singleOf(::GetSearchHistory)
    singleOf(::RecordSearchHistory)
    singleOf(::GetSeriesByEntity)
    singleOf(::GetWorksByEntity)
    singleOf(::GetSearchResults)
    singleOf(::GetMusicBrainzUrl)
}

package ly.david.musicsearch.shared.domain

import ly.david.musicsearch.shared.domain.collection.usecase.CreateCollection
import ly.david.musicsearch.shared.domain.collection.usecase.GetAllCollections
import ly.david.musicsearch.shared.domain.collection.usecase.GetCollection
import ly.david.musicsearch.shared.domain.coroutine.coroutineDispatchersModule
import ly.david.musicsearch.shared.domain.coroutine.coroutinesScopesModule
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
import ly.david.musicsearch.shared.domain.list.GetEntities
import ly.david.musicsearch.shared.domain.list.GetEntitiesImpl
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzCoverArtUrl
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrl
import ly.david.musicsearch.shared.domain.musicbrainz.usecase.GetMusicBrainzUrlImpl
import ly.david.musicsearch.shared.domain.nowplaying.usecase.DeleteNowPlayingHistory
import ly.david.musicsearch.shared.domain.nowplaying.usecase.GetNowPlayingHistory
import ly.david.musicsearch.shared.domain.relation.usecase.ObserveRelationStatsUseCase
import ly.david.musicsearch.shared.domain.relation.usecase.ObserveRelationStatsUseCaseImpl
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationships
import ly.david.musicsearch.shared.domain.relation.usecase.GetEntityRelationshipsImpl
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByRelease
import ly.david.musicsearch.shared.domain.release.usecase.GetTracksByReleaseImpl
import ly.david.musicsearch.shared.domain.search.history.usecase.DeleteSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.GetSearchHistory
import ly.david.musicsearch.shared.domain.search.history.usecase.RecordSearchHistory
import ly.david.musicsearch.shared.domain.search.results.usecase.GetSearchResults
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    includes(
        coroutinesScopesModule,
        coroutineDispatchersModule,
    )

    singleOf(::GetEntitiesImpl) bind GetEntities::class
    singleOf(::CreateCollection)
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
    singleOf(::ObserveRelationStatsUseCaseImpl) bind ObserveRelationStatsUseCase::class
    singleOf(::GetEntityRelationshipsImpl) bind GetEntityRelationships::class
    singleOf(::GetTracksByReleaseImpl) bind GetTracksByRelease::class
    singleOf(::DeleteSearchHistory)
    singleOf(::GetSearchHistory)
    singleOf(::RecordSearchHistory)
    singleOf(::GetSearchResults)
    singleOf(::GetMusicBrainzUrlImpl) bind GetMusicBrainzUrl::class
    singleOf(::GetMusicBrainzCoverArtUrl)
}

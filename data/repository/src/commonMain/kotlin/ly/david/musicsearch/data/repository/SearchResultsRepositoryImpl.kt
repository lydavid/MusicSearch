package ly.david.musicsearch.data.repository

import androidx.paging.ExperimentalPagingApi
import app.cash.paging.Pager
import app.cash.paging.RemoteMediator
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.SearchResultDao
import ly.david.musicsearch.data.musicbrainz.api.SearchApi
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.SearchMusicBrainzRemoteMediator
import ly.david.musicsearch.data.repository.internal.paging.insertFooterItemForNonEmpty
import ly.david.musicsearch.shared.domain.listitem.EndOfList
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository

@OptIn(ExperimentalPagingApi::class)
class SearchResultsRepositoryImpl(
    private val searchApi: SearchApi,
    private val searchResultDao: SearchResultDao,
    private val artistDao: ArtistDao,
) : SearchResultsRepository {

    override fun observeSearchResults(
        entity: MusicBrainzEntity,
        query: String,
    ) = Pager(
        config = CommonPagingConfig.pagingConfig,
        remoteMediator = getRemoteMediator(
            entity = entity,
            query = query,
        ),
        pagingSourceFactory = {
            searchResultDao.getArtistsSearchResult()
        },
    ).flow.map { pagingData ->
        pagingData.insertFooterItemForNonEmpty(item = EndOfList)
    }

    private fun getRemoteMediator(
        entity: MusicBrainzEntity,
        query: String,
    ): RemoteMediator<Int, ListItemModel> {
        return SearchMusicBrainzRemoteMediator(
            searchApi = searchApi,
            searchResultDao = searchResultDao,
            artistDao = artistDao,
            entity = entity,
            queryString = query,
        )
    }
}

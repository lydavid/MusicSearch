package ly.david.musicsearch.data.repository

import app.cash.paging.Pager
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.shared.domain.listitem.EndOfList
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.SearchMusicBrainzPagingSource
import ly.david.musicsearch.data.repository.internal.paging.insertFooterItemForNonEmpty
import ly.david.musicsearch.shared.domain.search.results.SearchResultsRepository

class SearchResultsRepositoryImpl(
    private val musicBrainzApi: MusicBrainzApi,
) : SearchResultsRepository {
    override fun observeSearchResults(
        entity: MusicBrainzEntity,
        query: String,
    ) = Pager(
        config = CommonPagingConfig.pagingConfig,
        pagingSourceFactory = {
            SearchMusicBrainzPagingSource(
                searchApi = musicBrainzApi,
                entity = entity,
                queryString = query,
            )
        },
    ).flow.map { pagingData ->
        pagingData.insertFooterItemForNonEmpty(item = EndOfList)
    }
}

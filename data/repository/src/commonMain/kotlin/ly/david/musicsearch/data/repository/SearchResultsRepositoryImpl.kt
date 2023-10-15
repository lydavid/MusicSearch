package ly.david.musicsearch.data.repository

import androidx.paging.Pager
import kotlinx.coroutines.flow.map
import ly.david.data.musicbrainz.api.MusicBrainzApi
import ly.david.musicsearch.core.models.listitem.EndOfList
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.data.repository.internal.paging.SearchMusicBrainzPagingSource
import ly.david.musicsearch.data.repository.internal.paging.insertFooterItemForNonEmpty
import ly.david.musicsearch.domain.search.results.SearchResultsRepository

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

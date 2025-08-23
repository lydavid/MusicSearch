package ly.david.musicsearch.shared.domain.listen

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ListensListRepository {
    fun observeListens(
        username: String,
        query: String,
    ): Flow<PagingData<ListenListItemModel>>
}

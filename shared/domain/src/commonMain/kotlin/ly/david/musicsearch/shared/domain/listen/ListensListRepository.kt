package ly.david.musicsearch.shared.domain.listen

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.Identifiable

interface ListensListRepository {
    fun observeListens(
        username: String,
        query: String,
        reachedLatest: Boolean,
        reachedOldest: Boolean,
        onReachedLatest: (Boolean) -> Unit,
        onReachedOldest: (Boolean) -> Unit,
    ): Flow<PagingData<Identifiable>>

    fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?>
}

package ly.david.musicsearch.shared.domain.listen

import app.cash.paging.PagingSource

interface ListenDao {
    fun insert(
        listens: List<Listen>,
    )

    fun deleteListensByUser(username: String)

    fun getListensByUser(
        username: String,
        query: String,
    ): PagingSource<Int, ListenListItemModel>

    fun getLatestTimestampMsByUser(username: String): Long?

    fun getOldestTimestampMsByUser(username: String): Long?
}

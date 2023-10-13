package ly.david.musicsearch.domain.history

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.core.common.getDateFormatted
import ly.david.musicsearch.data.core.history.LookupHistoryForListItem
import ly.david.musicsearch.domain.listitem.ListItemModel
import ly.david.musicsearch.domain.listitem.ListSeparator
import ly.david.musicsearch.domain.listitem.LookupHistoryListItemModel
import ly.david.musicsearch.domain.listitem.toLookupHistoryListItemModel
import ly.david.musicsearch.domain.paging.MusicBrainzPagingConfig
import org.koin.core.annotation.Single

@Single
class GetPagedHistory(
    private val lookupHistoryRepository: LookupHistoryRepository,
) {
    operator fun invoke(
        query: String,
        sortOption: HistorySortOption,
    ): Flow<PagingData<ListItemModel>> =
        Pager(
            config = MusicBrainzPagingConfig.pagingConfig,
            pagingSourceFactory = {
                lookupHistoryRepository.getAllLookupHistory(
                    query = query,
                    sortOption = sortOption
                )
            }
        ).flow.map { pagingData ->
            pagingData
                .map(LookupHistoryForListItem::toLookupHistoryListItemModel)
                .insertSeparators {
                        before: LookupHistoryListItemModel?,
                        after: LookupHistoryListItemModel?,
                    ->
                    getListSeparator(
                        before = before,
                        after = after,
                        sortOption = sortOption
                    )
                }
        }

    private fun getListSeparator(
        before: LookupHistoryListItemModel?,
        after: LookupHistoryListItemModel?,
        sortOption: HistorySortOption,
    ): ListSeparator? {
        if (sortOption != HistorySortOption.RECENTLY_VISITED &&
            sortOption != HistorySortOption.LEAST_RECENTLY_VISITED
        ) {
            return null
        }

        val beforeDate = before?.lastAccessed?.getDateFormatted()
        val afterDate = after?.lastAccessed?.getDateFormatted()
        if (beforeDate == afterDate || afterDate == null) {
            return null
        }

        return ListSeparator(
            id = afterDate,
            text = afterDate,
        )
    }
}

package ly.david.musicsearch.shared.domain.paging

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertSeparators

fun <T : Any> PagingData<T>.insertFooterItemForNonEmpty(
    terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
    item: T,
): PagingData<T> = insertSeparators(terminalSeparatorType) { before, after ->
    if (before != null && after == null) item else null
}

package ly.david.musicsearch.shared.domain.paging

import app.cash.paging.PagingData
import app.cash.paging.TerminalSeparatorType
import app.cash.paging.insertSeparators

fun <T : Any> PagingData<T>.insertFooterItemForNonEmpty(
    terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
    item: T,
): PagingData<T> = insertSeparators(terminalSeparatorType) { before, after ->
    if (before != null && after == null) item else null
}

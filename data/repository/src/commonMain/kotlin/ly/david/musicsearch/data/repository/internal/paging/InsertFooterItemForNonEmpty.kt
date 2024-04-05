package ly.david.musicsearch.data.repository.internal.paging

import app.cash.paging.PagingData
import app.cash.paging.TerminalSeparatorType
import app.cash.paging.insertSeparators

/**
 * Inserts a footer, only if there was data before it.
 */
internal fun <T : Any> PagingData<T>.insertFooterItemForNonEmpty(
    terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
    item: T,
): PagingData<T> = insertSeparators(terminalSeparatorType) { before, after ->
    if (before != null && after == null) item else null
}

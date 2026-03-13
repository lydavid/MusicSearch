package ly.david.musicsearch.data.repository.internal.paging

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertSeparators

/**
 * Inserts a header, only if there is data after it.
 */
internal fun <T : Any> PagingData<T>.insertHeaderItemForNonEmpty(
    terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
    item: T,
): PagingData<T> = insertSeparators(terminalSeparatorType) { before, after ->
    if (before == null && after != null) item else null
}

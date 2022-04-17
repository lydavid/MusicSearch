package ly.david.mbjc.ui.common.paging

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertSeparators

/**
 * Inserts a footer, only if there was data before it.
 */
internal fun <T : Any> PagingData<T>.insertFooterItemForNonEmpty(
    terminalSeparatorType: TerminalSeparatorType = TerminalSeparatorType.FULLY_COMPLETE,
    item: T,
): PagingData<T> = insertSeparators(terminalSeparatorType) { before, after ->
    if (before != null && after == null) item else null
}

package ly.david.ui.history

import ly.david.musicsearch.data.core.listitem.LookupHistoryListItemModel

interface DeleteHistoryDelegate {
    fun delete(history: LookupHistoryListItemModel)
    fun deleteAll()
}

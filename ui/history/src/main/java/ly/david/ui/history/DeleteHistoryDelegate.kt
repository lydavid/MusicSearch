package ly.david.ui.history

import ly.david.musicsearch.domain.listitem.LookupHistoryListItemModel

interface DeleteHistoryDelegate {
    fun delete(history: LookupHistoryListItemModel)
    fun deleteAll()
}

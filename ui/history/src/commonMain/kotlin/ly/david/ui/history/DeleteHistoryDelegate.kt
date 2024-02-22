package ly.david.ui.history

import ly.david.musicsearch.core.models.listitem.LookupHistoryListItemModel

interface DeleteHistoryDelegate {
    fun delete(history: LookupHistoryListItemModel)
    fun deleteAll()
}

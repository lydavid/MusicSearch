package ly.david.ui.history

import ly.david.data.persistence.history.LookupHistoryRoomModel

interface DeleteHistoryDelegate {
    fun delete(history: LookupHistoryRoomModel)
    fun deleteAll()
}

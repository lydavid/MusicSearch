package ly.david.ui.history

import ly.david.data.room.history.LookupHistoryRoomModel

interface DeleteHistoryDelegate {
    fun delete(history: LookupHistoryRoomModel)
    fun deleteAll()
}

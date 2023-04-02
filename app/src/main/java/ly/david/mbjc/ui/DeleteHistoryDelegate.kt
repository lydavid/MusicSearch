package ly.david.mbjc.ui

import ly.david.data.persistence.history.LookupHistoryRoomModel

internal interface DeleteHistoryDelegate {
    fun delete(history: LookupHistoryRoomModel)
    fun deleteAll()
}

package ly.david.mbjc.ui.common.history

import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistoryRoomModel
import ly.david.data.persistence.history.LookupHistoryDao

internal interface RecordLookupHistory {
    val lookupHistoryDao: LookupHistoryDao

    suspend fun recordLookupHistory(
        resourceId: String,
        resource: MusicBrainzResource,
        summary: String,
        searchHint: String = ""
    ) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistoryRoomModel(
                title = summary,
                resource = resource,
                id = resourceId,
                searchHint = searchHint
            )
        )
    }
}

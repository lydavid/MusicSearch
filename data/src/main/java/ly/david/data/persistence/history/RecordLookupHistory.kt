package ly.david.data.persistence.history

import ly.david.data.network.MusicBrainzResource

interface RecordLookupHistory {
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

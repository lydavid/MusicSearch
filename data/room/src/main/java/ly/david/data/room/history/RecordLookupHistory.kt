package ly.david.data.room.history

import ly.david.data.core.network.MusicBrainzEntity

interface RecordLookupHistory {
    val lookupHistoryDao: LookupHistoryDao

    suspend fun recordLookupHistory(
        entityId: String,
        entity: MusicBrainzEntity,
        summary: String,
        searchHint: String = "",
    ) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistoryRoomModel(
                title = summary,
                entity = entity,
                id = entityId,
                searchHint = searchHint
            )
        )
    }
}

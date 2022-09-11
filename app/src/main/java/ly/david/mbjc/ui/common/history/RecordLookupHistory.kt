package ly.david.mbjc.ui.common.history

import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.history.LookupHistory
import ly.david.mbjc.data.persistence.history.LookupHistoryDao

internal interface RecordLookupHistory {
    val lookupHistoryDao: LookupHistoryDao

    suspend fun recordLookupHistory(
        resourceId: String,
        resource: MusicBrainzResource,
        summary: String,
    ) {
        lookupHistoryDao.incrementOrInsertLookupHistory(
            LookupHistory(
                summary = summary,
                resource = resource,
                mbid = resourceId
            )
        )
    }
}

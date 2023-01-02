package ly.david.mbjc.ui.common.history

import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.history.LookupHistory
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
            LookupHistory(
                title = summary,
                resource = resource,
                mbid = resourceId,
                searchHint = searchHint
            )
        )
    }
}

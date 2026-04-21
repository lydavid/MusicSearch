package ly.david.musicsearch.data.database.adapter

import app.cash.sqldelight.ColumnAdapter
import ly.david.musicsearch.shared.domain.release.ReleaseStatus

internal object ReleaseStatusIntColumnAdapter : ColumnAdapter<ReleaseStatus, Long> {
    override fun decode(databaseValue: Long): ReleaseStatus {
        return ReleaseStatus.entries.first { it.order == databaseValue.toInt() }
    }

    override fun encode(value: ReleaseStatus): Long {
        return value.order.toLong()
    }
}

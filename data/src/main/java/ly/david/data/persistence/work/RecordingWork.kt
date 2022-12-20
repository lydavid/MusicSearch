package ly.david.data.persistence.work

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "recording_work",
    primaryKeys = ["recording_id", "work_id"],
)
data class RecordingWork(
    @ColumnInfo(name = "recording_id")
    val recordingId: String,

    @ColumnInfo(name = "work_id")
    val workId: String,
)

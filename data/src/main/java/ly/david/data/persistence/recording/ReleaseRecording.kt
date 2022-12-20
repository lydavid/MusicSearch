package ly.david.data.persistence.recording

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "recording_release",
    primaryKeys = ["release_id", "recording_id"],
)
data class ReleaseRecording(
    @ColumnInfo(name = "release_id")
    val releaseId: String,

    @ColumnInfo(name = "recording_id")
    val recordingId: String,
)

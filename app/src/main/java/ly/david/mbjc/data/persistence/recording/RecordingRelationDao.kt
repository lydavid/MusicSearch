package ly.david.mbjc.data.persistence.recording

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class RecordingRelationDao : BaseDao<RecordingRelationRoomModel> {

    @Query(
        """
            SELECT rel.*
            FROM relations rel
            INNER JOIN recordings rec ON rel.resource_id = rec.id
            WHERE rec.id = :recordingId AND rel.resource = "recording"
            ORDER BY rel.`order`
        """
    )
    abstract fun getRelationsForRecording(recordingId: String): PagingSource<Int, RecordingRelationRoomModel>
}

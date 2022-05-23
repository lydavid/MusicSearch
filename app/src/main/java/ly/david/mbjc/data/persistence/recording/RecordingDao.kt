package ly.david.mbjc.data.persistence.recording

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao
import ly.david.mbjc.data.persistence.RecordingRoomModel

@Dao
internal abstract class RecordingDao : BaseDao<RecordingRoomModel> {

    // TODO: get recording with all relationships

    @Query("SELECT * FROM recordings WHERE id = :recordingId")
    abstract suspend fun getRecording(recordingId: String): RecordingRoomModel?

//    SELECT r.*
//    FROM releases r
//    INNER JOIN releases_release_groups rrg ON r.id = rrg.release_id
//    INNER JOIN release_groups rg ON rg.id = rrg.release_group_id
//    WHERE rg.id = :releaseGroupId
    // TODO: incorrect???
    //  we need to get Relation as final result
    //  get all recording ids
//    @Query(
//        """
//            SELECT rel.*
//            FROM relations rel
//            INNER JOIN recordings_relations rr ON rel.resource_id = rr.linked_resource_id
//            INNER JOIN recordings rec ON rr.recording_id = rec.id
//            WHERE rec.id = :recordingId
//        """
//    )
//    abstract fun getRelationsForRecording(recordingId: String): PagingSource<Int, RecordingRelationRoomModel>
}

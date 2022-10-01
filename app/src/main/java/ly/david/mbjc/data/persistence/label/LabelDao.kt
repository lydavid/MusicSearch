package ly.david.mbjc.data.persistence.label

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class LabelDao : BaseDao<LabelRoomModel> {

    @Query("SELECT * FROM labels WHERE id = :labelId")
    abstract suspend fun getLabel(labelId: String): LabelRoomModel?

    @Query(
        """
        UPDATE labels 
        SET release_count = :releaseCount
        WHERE id = :labelId
        """
    )
    abstract suspend fun setReleaseCount(labelId: String, releaseCount: Int)

    // TODO: assuming mbid are unique between tables, we can extract hasDefaultRelations to its own table
    //  or we can use "artist:9388cee2-7d57-4598-905f-106019b267d3"
    @Query(
        """
        UPDATE labels
        SET has_default_relations = :hasDefaultRelations
        WHERE id = :labelId
        """
    )
    abstract suspend fun setHasDefaultRelations(labelId: String, hasDefaultRelations: Boolean)
}

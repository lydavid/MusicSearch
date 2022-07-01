package ly.david.mbjc.data.persistence

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query

@Dao
internal abstract class RelationDao : BaseDao<RelationRoomModel> {

    @Query(
        """
            SELECT rel.*
            FROM relations rel
            INNER JOIN recordings rec ON rel.resource_id = rec.id
            WHERE rec.id = :resourceId
            ORDER BY rel.`order`
        """
    )
    abstract fun getRelationsForResource(
        resourceId: String
    ): PagingSource<Int, RelationRoomModel>
}

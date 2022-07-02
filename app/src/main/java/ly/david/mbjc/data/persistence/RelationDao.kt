package ly.david.mbjc.data.persistence

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query

@Dao
internal abstract class RelationDao : BaseDao<RelationRoomModel> {

    @Query(
        """
            SELECT *
            FROM relations
            WHERE resource_id = :resourceId
            ORDER BY `order`
        """
    )
    abstract fun getRelationsForResource(
        resourceId: String
    ): PagingSource<Int, RelationRoomModel>
}

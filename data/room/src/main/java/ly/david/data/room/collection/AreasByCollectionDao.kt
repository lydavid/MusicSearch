package ly.david.data.room.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.area.AreaRoomModel

interface AreasByCollectionDao {

    companion object {
        private const val AREAS_BY_COLLECTION = """
            FROM area a
            INNER JOIN collection_entity ce ON a.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_AREA_BY_COLLECTION = """
            SELECT a.*
            $AREAS_BY_COLLECTION
        """

        private const val SELECT_AREA_ID_BY_COLLECTION = """
            SELECT a.id
            $AREAS_BY_COLLECTION
        """

        private const val ORDER_BY_DATE_NAME = """
            ORDER BY a.begin, a.end, a.name
        """

        private const val FILTERED = """
            AND (
                a.name LIKE :query OR a.disambiguation LIKE :query
                OR a.sort_name LIKE :query OR a.type LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM area WHERE id IN (
        $SELECT_AREA_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteAreasByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $AREAS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfAreasByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_AREA_BY_COLLECTION
        $ORDER_BY_DATE_NAME
    """
    )
    fun getAreasByCollection(collectionId: String): PagingSource<Int, AreaRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_AREA_BY_COLLECTION
        $FILTERED
        $ORDER_BY_DATE_NAME
    """
    )
    fun getAreasByCollectionFiltered(
        collectionId: String,
        query: String
    ): PagingSource<Int, AreaRoomModel>
}

package ly.david.data.room.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.work.WorkRoomModel

interface WorksByCollectionDao {

    companion object {
        private const val WORKS_BY_COLLECTION = """
            FROM work w
            INNER JOIN collection_entity ce ON w.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_WORK_BY_COLLECTION = """
            SELECT w.*
            $WORKS_BY_COLLECTION
        """

        private const val SELECT_WORK_ID_BY_COLLECTION = """
            SELECT w.id
            $WORKS_BY_COLLECTION
        """

        private const val FILTERED = """
            AND (
                w.name LIKE :query OR w.disambiguation LIKE :query
                OR w.type LIKE :query OR w.language LIKE :query OR w.iswcs LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM work WHERE id IN (
        $SELECT_WORK_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteWorksByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $WORKS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfWorksByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_WORK_BY_COLLECTION
    """
    )
    fun getWorksByCollection(collectionId: String): PagingSource<Int, WorkRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_WORK_BY_COLLECTION
        $FILTERED
    """
    )
    fun getWorksByCollectionFiltered(
        collectionId: String,
        query: String,
    ): PagingSource<Int, WorkRoomModel>
}

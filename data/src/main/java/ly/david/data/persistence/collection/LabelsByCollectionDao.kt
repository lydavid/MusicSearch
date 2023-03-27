package ly.david.data.persistence.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.label.LabelRoomModel

interface LabelsByCollectionDao {

    companion object {
        private const val LABELS_BY_COLLECTION = """
            FROM label l
            INNER JOIN collection_entity ce ON l.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_LABEL_BY_COLLECTION = """
            SELECT l.*
            $LABELS_BY_COLLECTION
        """

        private const val SELECT_LABEL_ID_BY_COLLECTION = """
            SELECT l.id
            $LABELS_BY_COLLECTION
        """

        private const val FILTERED = """
            AND (
                l.name LIKE :query OR l.disambiguation LIKE :query
                OR l.type LIKE :query OR l.label_code LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM label WHERE id IN (
        $SELECT_LABEL_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteLabelsByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $LABELS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfLabelsByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_LABEL_BY_COLLECTION
    """
    )
    fun getLabelsByCollection(collectionId: String): PagingSource<Int, LabelRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_LABEL_BY_COLLECTION
        $FILTERED
    """
    )
    fun getLabelsByCollectionFiltered(
        collectionId: String,
        query: String
    ): PagingSource<Int, LabelRoomModel>
}

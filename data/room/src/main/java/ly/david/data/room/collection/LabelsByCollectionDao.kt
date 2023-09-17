package ly.david.data.room.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.label.LabelRoomModel

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
        query: String,
    ): PagingSource<Int, LabelRoomModel>
}

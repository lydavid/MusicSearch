package ly.david.data.persistence.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.series.SeriesRoomModel

interface SeriesByCollectionDao {

    companion object {
        private const val SERIES_BY_COLLECTION = """
            FROM series s
            INNER JOIN collection_entity ce ON s.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_SERIES_BY_COLLECTION = """
            SELECT s.*
            $SERIES_BY_COLLECTION
        """

        private const val SELECT_SERIES_ID_BY_COLLECTION = """
            SELECT s.id
            $SERIES_BY_COLLECTION
        """

        private const val FILTERED = """
            AND (
                s.name LIKE :query OR s.disambiguation LIKE :query
                OR s.type LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM series WHERE id IN (
        $SELECT_SERIES_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteSeriesByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $SERIES_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfSeriesByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_SERIES_BY_COLLECTION
    """
    )
    fun getSeriesByCollection(collectionId: String): PagingSource<Int, SeriesRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_SERIES_BY_COLLECTION
        $FILTERED
    """
    )
    fun getSeriesByCollectionFiltered(
        collectionId: String,
        query: String
    ): PagingSource<Int, SeriesRoomModel>
}

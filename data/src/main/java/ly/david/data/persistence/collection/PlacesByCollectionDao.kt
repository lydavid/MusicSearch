package ly.david.data.persistence.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.place.PlaceRoomModel

interface PlacesByCollectionDao {

    companion object {
        private const val PLACES_BY_COLLECTION = """
            FROM place p
            INNER JOIN collection_entity ce ON p.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_PLACE_BY_COLLECTION = """
            SELECT p.*
            $PLACES_BY_COLLECTION
        """

        private const val SELECT_PLACE_ID_BY_COLLECTION = """
            SELECT p.id
            $PLACES_BY_COLLECTION
        """

        private const val ORDER_BY_ADDRESS = """
            ORDER BY p.name, p.address
        """

        private const val FILTERED = """
            AND (
                p.name LIKE :query OR p.disambiguation LIKE :query
                OR p.address LIKE :query OR p.type LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM place WHERE id IN (
        $SELECT_PLACE_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deletePlacesByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $PLACES_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfPlacesByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_PLACE_BY_COLLECTION
        $ORDER_BY_ADDRESS
    """
    )
    fun getPlacesByCollection(collectionId: String): PagingSource<Int, PlaceRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_PLACE_BY_COLLECTION
        $FILTERED
        $ORDER_BY_ADDRESS
    """
    )
    fun getPlacesByCollectionFiltered(
        collectionId: String,
        query: String
    ): PagingSource<Int, PlaceRoomModel>
}

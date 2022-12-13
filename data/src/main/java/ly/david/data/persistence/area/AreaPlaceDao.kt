package ly.david.data.persistence.area

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.place.PlaceRoomModel

@Dao
abstract class AreaPlaceDao : BaseDao<AreaPlace>() {

    companion object {
        private const val PLACES_BY_AREA = """
            FROM places p
            INNER JOIN area_place ap ON p.id = ap.place_id
            INNER JOIN areas a ON a.id = ap.area_id
            WHERE a.id = :areaId
        """

        private const val SELECT_PLACE_BY_AREA = """
            SELECT p.*
            $PLACES_BY_AREA
        """

        private const val SELECT_PLACE_ID_BY_AREA = """
            SELECT p.id
            $PLACES_BY_AREA
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
        DELETE FROM places WHERE id IN (
        $SELECT_PLACE_ID_BY_AREA
        )
        """
    )
    abstract suspend fun deletePlacesByArea(areaId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $PLACES_BY_AREA
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfPlacesByArea(areaId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_PLACE_BY_AREA
        $ORDER_BY_ADDRESS
    """
    )
    abstract fun getPlacesByArea(areaId: String): PagingSource<Int, PlaceRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_PLACE_BY_AREA
        $FILTERED
        $ORDER_BY_ADDRESS
    """
    )
    abstract fun getPlacesByArea(
        areaId: String,
        query: String
    ): PagingSource<Int, PlaceRoomModel>
}

package ly.david.data.room.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.artist.ArtistRoomModel

interface ArtistsByCollectionDao {

    companion object {
        private const val ARTISTS_BY_COLLECTION = """
            FROM artist a
            INNER JOIN collection_entity ce ON a.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            WHERE c.id = :collectionId
        """

        private const val SELECT_ARTIST_BY_COLLECTION = """
            SELECT a.*
            $ARTISTS_BY_COLLECTION
        """

        private const val SELECT_ARTIST_ID_BY_COLLECTION = """
            SELECT a.id
            $ARTISTS_BY_COLLECTION
        """

        private const val ORDER_BY_DATE_NAME = """
            ORDER BY a.begin, a.end, a.name
        """

        private const val FILTERED = """
            AND (
                a.name LIKE :query OR a.disambiguation LIKE :query
                OR a.sort_name LIKE :query OR a.type LIKE :query
                OR a.gender LIKE :query OR a.country_code LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM artist WHERE id IN (
        $SELECT_ARTIST_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteArtistsByCollection(collectionId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $ARTISTS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfArtistsByCollection(collectionId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_ARTIST_BY_COLLECTION
        $ORDER_BY_DATE_NAME
    """
    )
    fun getArtistsByCollection(collectionId: String): PagingSource<Int, ArtistRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_ARTIST_BY_COLLECTION
        $FILTERED
        $ORDER_BY_DATE_NAME
    """
    )
    fun getArtistsByCollectionFiltered(
        collectionId: String,
        query: String,
    ): PagingSource<Int, ArtistRoomModel>
}

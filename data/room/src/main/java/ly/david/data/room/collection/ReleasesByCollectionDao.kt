package ly.david.data.room.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.release.RoomReleaseForListItem

interface ReleasesByCollectionDao {

    // TODO: note it may be possible that a release gets deleted locally, meaning we won't be able to join it...
    //  don't really need to deal with this for now cause we only allow deleting when refreshing
    //  so it should be rare to delete but fail to fetch.
    companion object {
        private const val RELEASES_BY_COLLECTION = """
            FROM `release` r
            INNER JOIN collection_entity ce ON r.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            LEFT JOIN artist_credit_entity acr ON acr.entity_id = r.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE c.id = :collectionId
        """

        private const val SELECT_RELEASES_BY_COLLECTION = """
            SELECT r.*, ac.name AS artist_credit_names
            $RELEASES_BY_COLLECTION
        """

        private const val ORDER_BY_DATE_AND_TITLE = """
            ORDER BY r.date, r.name
        """

        private const val FILTERED = """
            AND (
                r.name LIKE :query OR r.disambiguation LIKE :query
                OR r.date LIKE :query OR r.country_code LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_COLLECTION
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    fun getReleasesByCollection(collectionId: String): PagingSource<Int, RoomReleaseForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_COLLECTION
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    fun getReleasesByCollectionFiltered(
        collectionId: String,
        query: String,
    ): PagingSource<Int, RoomReleaseForListItem>
}

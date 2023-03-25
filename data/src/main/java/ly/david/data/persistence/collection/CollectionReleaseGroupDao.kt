package ly.david.data.persistence.collection

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.releasegroup.ReleaseGroupForListItem
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount

interface CollectionReleaseGroupDao {

    companion object {
        private const val RELEASE_GROUPS_BY_COLLECTION = """
            FROM release_group rg
            INNER JOIN collection_entity ce ON rg.id = ce.entity_id
            INNER JOIN collection c ON c.id = ce.id
            LEFT JOIN artist_credit_resource acr ON acr.resource_id = rg.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE c.id = :collectionId
        """

        private const val SELECT_RELEASE_GROUPS_BY_COLLECTION = """
            SELECT rg.*, ac.name AS artist_credit_names
            $RELEASE_GROUPS_BY_COLLECTION
        """

        private const val SELECT_RELEASE_GROUPS_ID_BY_COLLECTION = """
            SELECT rg.id
            $RELEASE_GROUPS_BY_COLLECTION
        """

        private const val ORDER_BY_COLLECTION_LINKING_TABLE = """
            ORDER BY ce.rowid
        """

        // The date format YYYY-MM-DD can be correctly sorted by SQLite.
        private const val ORDER_BY_TYPES_AND_DATE = """
            ORDER BY rg.primary_type, rg.secondary_types, rg.first_release_date
        """

        private const val FILTERED = """
            AND (
                rg.title LIKE :query OR rg.disambiguation LIKE :query
                OR rg.first_release_date LIKE :query
                OR rg.primary_type LIKE :query OR rg.secondary_types LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM release_group WHERE id IN (
        $SELECT_RELEASE_GROUPS_ID_BY_COLLECTION
        )
        """
    )
    suspend fun deleteReleaseGroupsByCollection(collectionId: String)

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_COLLECTION
        $ORDER_BY_COLLECTION_LINKING_TABLE
        """
    )
    fun getReleaseGroupsByCollection(collectionId: String): PagingSource<Int, ReleaseGroupForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_COLLECTION
        $ORDER_BY_TYPES_AND_DATE
    """
    )
    fun getReleaseGroupsByCollectionSorted(collectionId: String): PagingSource<Int, ReleaseGroupForListItem>

    // Not as fast as FTS but allows searching characters within words
    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_COLLECTION
        $FILTERED
        $ORDER_BY_COLLECTION_LINKING_TABLE
    """
    )
    fun getReleaseGroupsByCollectionFiltered(
        collectionId: String,
        query: String
    ): PagingSource<Int, ReleaseGroupForListItem>

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_COLLECTION
        $FILTERED
        $ORDER_BY_TYPES_AND_DATE
    """
    )
    fun getReleaseGroupsByCollectionFilteredSorted(
        collectionId: String,
        query: String
    ): PagingSource<Int, ReleaseGroupForListItem>

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $RELEASE_GROUPS_BY_COLLECTION
            ),
            0
        ) AS count
    """
    )
    suspend fun getNumberOfReleaseGroupsByCollection(collectionId: String): Int

    @Query(
        """
        SELECT rg.primary_type, rg.secondary_types, COUNT(rg.id) as count
        $RELEASE_GROUPS_BY_COLLECTION
        GROUP BY rg.primary_type, rg.secondary_types
    """
    )
    suspend fun getCountOfEachAlbumType(collectionId: String): List<ReleaseGroupTypeCount>
}

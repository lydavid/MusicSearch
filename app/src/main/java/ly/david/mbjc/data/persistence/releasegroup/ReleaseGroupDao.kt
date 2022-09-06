package ly.david.mbjc.data.persistence.releasegroup

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class ReleaseGroupDao : BaseDao<ReleaseGroupRoomModel> {

    companion object {
        private const val RELEASE_GROUPS_BY_ARTIST = """
            FROM release_groups rg
            INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
            INNER JOIN artists a ON a.id = rga.artist_id
            WHERE a.id = :artistId
        """

        private const val SELECT_RELEASE_GROUPS_BY_ARTIST = """
            SELECT rg.*
            $RELEASE_GROUPS_BY_ARTIST
        """

        private const val SELECT_RELEASE_GROUPS_ID_BY_ARTIST = """
            SELECT rg.id
            $RELEASE_GROUPS_BY_ARTIST
        """

        private const val ORDER_BY_ARTIST_LINKING_TABLE = """
            ORDER BY rga.rowid
        """

        private const val ORDER_BY_TYPES_AND_DATE = """
            ORDER BY rg.primary_type, rg.secondary_types, rg.first_release_date
        """

        // TODO: if something like this gets too slow, then use fts
        // TODO: we're able to filter on date's month/day even though we don't display it. Could be confusing
        private const val FILTERED = """
            AND (
                rg.title LIKE :query OR rg.disambiguation LIKE :query
                OR rg.first_release_date LIKE :query
                OR rg.primary_type LIKE :query OR rg.secondary_types LIKE :query
            )
        """
    }

    // Lookup
    @Query("SELECT * FROM release_groups WHERE id = :releaseGroupId")
    abstract suspend fun getReleaseGroup(releaseGroupId: String): ReleaseGroupRoomModel?

    @Query(
        """
        DELETE FROM release_groups WHERE id IN (
        $SELECT_RELEASE_GROUPS_ID_BY_ARTIST
        )
        """
    )
    abstract suspend fun deleteReleaseGroupsByArtist(artistId: String)

    // Make sure to select from release_groups first, rather than artists.
    // That way, when there are no entries, we return empty rather than 1 entry with null values.
    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_ARTIST
        $ORDER_BY_ARTIST_LINKING_TABLE
        """
    )
    abstract fun getReleaseGroupsByArtist(artistId: String): PagingSource<Int, ReleaseGroupRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_ARTIST
        $ORDER_BY_TYPES_AND_DATE
    """
    )
    abstract fun getReleaseGroupsByArtistSorted(artistId: String): PagingSource<Int, ReleaseGroupRoomModel>

    // Not as fast as FTS but allows searching characters within words
    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_ARTIST
        $FILTERED
        $ORDER_BY_ARTIST_LINKING_TABLE
    """
    )
    abstract fun getReleaseGroupsByArtistFiltered(
        artistId: String,
        query: String
    ): PagingSource<Int, ReleaseGroupRoomModel>

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_ARTIST
        $FILTERED
        $ORDER_BY_TYPES_AND_DATE
    """
    )
    abstract fun getReleaseGroupsByArtistFilteredSorted(
        artistId: String,
        query: String
    ): PagingSource<Int, ReleaseGroupRoomModel>

    @Query(
        """
        UPDATE release_groups 
        SET release_count = :releaseCount
        WHERE id = :releaseGroupId
        """
    )
    abstract suspend fun setReleaseCount(releaseGroupId: String, releaseCount: Int)

    @Query(
        """
        DELETE from release_groups
        WHERE id in
        (SELECT rg.id
        FROM release_groups rg
        INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
        INNER JOIN artists a ON a.id = rga.artist_id
        WHERE a.id = :artistId)
    """
    )
    abstract suspend fun deleteAllReleaseGroupsByArtist(artistId: String)
}

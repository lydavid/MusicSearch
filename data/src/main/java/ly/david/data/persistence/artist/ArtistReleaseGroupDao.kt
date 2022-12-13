package ly.david.data.persistence.artist

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount
import ly.david.data.persistence.releasegroup.ReleaseGroupWithArtistCredits

@Dao
abstract class ArtistReleaseGroupDao : BaseDao<ArtistReleaseGroup>() {

    companion object {
        private const val RELEASE_GROUPS_BY_ARTIST = """
            FROM release_groups rg
            INNER JOIN artists_release_groups arg ON rg.id = arg.release_group_id
            INNER JOIN artists a ON a.id = arg.artist_id
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
            ORDER BY arg.rowid
        """

        // The date format YYYY-MM-DD can be correctly sorted by SQLite.
        private const val ORDER_BY_TYPES_AND_DATE = """
            ORDER BY rg.primary_type, rg.secondary_types, rg.first_release_date
        """

        // TODO: we're able to filter on date's month/day even though we don't display it. Could be confusing
        private const val FILTERED = """
            AND (
                rg.title LIKE :query OR rg.disambiguation LIKE :query
                OR rg.first_release_date LIKE :query
                OR rg.primary_type LIKE :query OR rg.secondary_types LIKE :query
            )
        """
    }

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
    abstract fun getReleaseGroupsByArtist(artistId: String): PagingSource<Int, ReleaseGroupWithArtistCredits>

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_ARTIST
        $ORDER_BY_TYPES_AND_DATE
    """
    )
    abstract fun getReleaseGroupsByArtistSorted(artistId: String): PagingSource<Int, ReleaseGroupWithArtistCredits>

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
    ): PagingSource<Int, ReleaseGroupWithArtistCredits>

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
    ): PagingSource<Int, ReleaseGroupWithArtistCredits>

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            $RELEASE_GROUPS_BY_ARTIST
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleaseGroupsByArtist(artistId: String): Int

    @Query(
        """
        SELECT rg.primary_type, rg.secondary_types, COUNT(rg.id) as count
        $RELEASE_GROUPS_BY_ARTIST
        GROUP BY rg.primary_type, rg.secondary_types
    """
    )
    abstract suspend fun getCountOfEachAlbumType(artistId: String): List<ReleaseGroupTypeCount>
}

package ly.david.mbjc.data.persistence

import androidx.paging.PagingSource
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.ReleaseGroupTypes

@Dao
abstract class ReleaseGroupDao : BaseDao<RoomReleaseGroup> {

    companion object {
        private const val RELEASE_GROUPS_BY_ARTIST = """
            SELECT rg.*
            FROM release_groups rg
            INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
            INNER JOIN artists a ON a.id = rga.artist_id
            WHERE a.id = :artistId
        """

        private const val SORTED = "ORDER BY rg.primary_type, rg.secondary_types, rg.first_release_date"

        private const val FILTERED = """
            AND (rg.title LIKE :query OR rg.disambiguation LIKE :query OR rg.first_release_date LIKE :query
            OR rg.primary_type LIKE :query OR rg.secondary_types LIKE :query)
        """
    }

    // Make sure to select from release_groups first, rather than artists.
    // That way, when there are no entries, we return empty rather than 1 entry with null values.
    @Transaction
    @Query(RELEASE_GROUPS_BY_ARTIST)
    abstract fun getReleaseGroupsByArtist(artistId: String): PagingSource<Int, RoomReleaseGroup>

    @Transaction
    @Query(
        """
        $RELEASE_GROUPS_BY_ARTIST
        $SORTED
    """
    )
    abstract fun getReleaseGroupsByArtistSorted(artistId: String): PagingSource<Int, RoomReleaseGroup>

    // Not as fast as FTS but allows searching characters within words
    @Transaction
    @Query(
        """
        $RELEASE_GROUPS_BY_ARTIST
        $FILTERED
    """
    )
    abstract fun getReleaseGroupsByArtistFiltered(
        artistId: String,
        query: String
    ): PagingSource<Int, RoomReleaseGroup>

    @Transaction
    @Query(
        """
        $RELEASE_GROUPS_BY_ARTIST
        $FILTERED
        $SORTED
    """
    )
    abstract fun getReleaseGroupsByArtistFilteredSorted(
        artistId: String,
        query: String
    ): PagingSource<Int, RoomReleaseGroup>

    @Query("SELECT * FROM release_groups WHERE id = :releaseGroupId")
    abstract suspend fun getReleaseGroup(releaseGroupId: String): RoomReleaseGroup?

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM release_groups rg
            INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
            INNER JOIN artists a ON a.id = rga.artist_id
            WHERE a.id = :artistId
            GROUP BY a.id),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleaseGroupsByArtist(artistId: String): Int

    @Query(
        """
        SELECT rg.primary_type, rg.secondary_types, COUNT(rg.id) as count
        FROM release_groups rg
        INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
        INNER JOIN artists a ON a.id = rga.artist_id
        WHERE a.id = :artistId
        GROUP  BY rg.primary_type, rg.secondary_types
    """
    )
    abstract suspend fun getCountOfEachAlbumType(artistId: String): List<ReleaseGroupTypeCount>

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

data class ReleaseGroupTypeCount(
    @ColumnInfo(name = "primary_type")
    override val primaryType: String? = null,

    @ColumnInfo(name = "secondary_types")
    override val secondaryTypes: List<String>? = null,

    @ColumnInfo(name = "count")
    val count: Int,
): ReleaseGroupTypes

package ly.david.mbjc.data.persistence

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class ReleaseGroupDao : BaseDao<RoomReleaseGroup> {

    // Make sure to select from release_groups first, rather than artists.
    // That way, when there are no entries, we return empty rather than 1 entry with null values.
    @Transaction
    @Query(
        """
        SELECT rg.*
        FROM release_groups rg
        INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
        INNER JOIN artists a ON a.id = rga.artist_id
        WHERE a.id = :artistId
    """
    )
    abstract fun getReleaseGroupsByArtist(artistId: String): PagingSource<Int, RoomReleaseGroup>

    // Not as fast as FTS but allows searching characters within words
    @Transaction
    @Query(
        """
        SELECT rg.*
        FROM release_groups rg
        INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
        INNER JOIN artists a ON a.id = rga.artist_id
        WHERE a.id = :artistId
        AND (rg.title LIKE :query OR rg.disambiguation LIKE :query OR rg.`first-release-date` LIKE :query
        OR rg.`primary-type` LIKE :query OR rg.`secondary-types` LIKE :query)
    """
    )
    abstract fun getReleaseGroupsByArtistFiltered(artistId: String, query: String): PagingSource<Int, RoomReleaseGroup>

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

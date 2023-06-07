package ly.david.data.room.artist.releasegroups

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao
import ly.david.data.room.releasegroup.ReleaseGroupForListItem
import ly.david.data.room.releasegroup.ReleaseGroupTypeCount

@Dao
abstract class ArtistReleaseGroupDao : BaseDao<ArtistReleaseGroup>() {

    companion object {
        private const val RELEASE_GROUPS_BY_ARTIST = """
            FROM release_group rg
            INNER JOIN artist_release_group arg ON rg.id = arg.release_group_id
            INNER JOIN artist a ON a.id = arg.artist_id
            LEFT JOIN artist_credit_resource acr ON acr.resource_id = rg.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE a.id = :artistId
        """

        private const val SELECT_RELEASE_GROUPS_BY_ARTIST = """
            SELECT rg.*, ac.name AS artist_credit_names
            $RELEASE_GROUPS_BY_ARTIST
        """

        private const val SELECT_RELEASE_GROUPS_ID_BY_ARTIST = """
            SELECT rg.id
            $RELEASE_GROUPS_BY_ARTIST
        """

        // TODO: we're able to filter on date's month/day even though we don't display it. Could be confusing
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
        $SELECT_RELEASE_GROUPS_ID_BY_ARTIST
        )
        """
    )
    abstract suspend fun deleteReleaseGroupsByArtist(artistId: String)

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_ARTIST
        $FILTERED
        ORDER BY
          CASE WHEN :sorted THEN rg.primary_type ELSE arg.rowid END,
          CASE WHEN :sorted THEN rg.secondary_types END,
          CASE WHEN :sorted THEN rg.first_release_date END
    """
    )
    abstract fun getReleaseGroupsByArtist(
        artistId: String,
        query: String = "%%",
        sorted: Boolean = false
    ): PagingSource<Int, ReleaseGroupForListItem>

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

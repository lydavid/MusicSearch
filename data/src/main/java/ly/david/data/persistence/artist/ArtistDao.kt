package ly.david.data.persistence.artist

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount

@Dao
abstract class ArtistDao : BaseDao<ArtistRoomModel>() {

    @Query("SELECT * FROM artists WHERE id = :artistId")
    abstract suspend fun getArtist(artistId: String): ArtistRoomModel?

    // TODO: use browse_resources
    @Query(
        """
        UPDATE artists
        SET release_group_count = :releaseGroupCount
        WHERE id = :artistId
        """
    )
    abstract suspend fun setReleaseGroupCount(artistId: String, releaseGroupCount: Int)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM release_groups rg
            INNER JOIN artists_release_groups arg ON rg.id = arg.release_group_id
            INNER JOIN artists a ON a.id = arg.artist_id
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
        INNER JOIN artists_release_groups arg ON rg.id = arg.release_group_id
        INNER JOIN artists a ON a.id = arg.artist_id
        WHERE a.id = :artistId
        GROUP  BY rg.primary_type, rg.secondary_types
    """
    )
    abstract suspend fun getCountOfEachAlbumType(artistId: String): List<ReleaseGroupTypeCount>
}

package ly.david.mbjc.data.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
internal abstract class ArtistDao : BaseDao<ArtistRoomModel> {
    @Query("SELECT * FROM artists WHERE id = :artistId")
    abstract suspend fun getArtist(artistId: String): ArtistRoomModel?


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
        UPDATE artists
        SET has_default_relations = :hasDefaultRelations
        WHERE id = :artistId
        """
    )
    abstract suspend fun setHasDefaultRelations(artistId: String, hasDefaultRelations: Boolean)

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
}

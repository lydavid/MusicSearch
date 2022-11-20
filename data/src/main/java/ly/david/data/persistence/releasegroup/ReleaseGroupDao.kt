package ly.david.data.persistence.releasegroup

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.getDisplayNames
import ly.david.data.network.ReleaseGroupMusicBrainzModel
import ly.david.data.network.toRoomModels
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.artist.ArtistCreditResource
import ly.david.data.persistence.artist.ArtistCreditRoomModel
import ly.david.data.persistence.artist.ArtistReleaseGroup

const val INSERTION_FAILED_DUE_TO_CONFLICT = -1L

@Dao
abstract class ReleaseGroupDao : BaseDao<ReleaseGroupRoomModel>, ArtistCreditInterface {

    companion object {
        private const val RELEASE_GROUPS_BY_ARTIST = """
            FROM release_groups rg
            INNER JOIN artists_release_groups rga ON rg.id = rga.release_group_id
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

        // The date format YYYY-MM-DD can be correctly sorted by SQLite.
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

    // TODO: won't work...
//    @Inject
//    lateinit var artistCreditDao: ArtistCreditDao

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllReleaseGroupsWithArtistCredits(releaseGroups: List<ReleaseGroupMusicBrainzModel>) {
        releaseGroups.forEach { releaseGroup ->
            val artistCreditName = releaseGroup.artistCredits.getDisplayNames()
            var artistCreditId = insertArtistCredit(ArtistCreditRoomModel(name = artistCreditName))
            if (artistCreditId == INSERTION_FAILED_DUE_TO_CONFLICT) {
                artistCreditId = getArtistCreditByName(artistCreditName).id
            } else {
                insertAllArtistCreditNames(releaseGroup.artistCredits.toRoomModels(artistCreditId))
            }

            insertArtistCreditResource(ArtistCreditResource(
                artistCreditId = artistCreditId,
                resourceId = releaseGroup.id
            ))

            insert(releaseGroup.toReleaseGroupRoomModel())
        }
    }

    // TODO: insert a single rg with artist credits
    //  have above use this

    // Lookup
    @Query("SELECT * FROM release_groups WHERE id = :releaseGroupId")
    abstract suspend fun getReleaseGroup(releaseGroupId: String): ReleaseGroupWithArtists?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllArtistReleaseGroup(artistReleaseGroup: List<ArtistReleaseGroup>)

    // TODO: move all by artist to either artist dao or artist_release_group_dao
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
    abstract fun getReleaseGroupsByArtist(artistId: String): PagingSource<Int, ReleaseGroupWithArtists>

    @Transaction
    @Query(
        """
        $SELECT_RELEASE_GROUPS_BY_ARTIST
        $ORDER_BY_TYPES_AND_DATE
    """
    )
    abstract fun getReleaseGroupsByArtistSorted(artistId: String): PagingSource<Int, ReleaseGroupWithArtists>

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
    ): PagingSource<Int, ReleaseGroupWithArtists>

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
    ): PagingSource<Int, ReleaseGroupWithArtists>

    @Query(
        """
        DELETE from release_groups
        WHERE id in
        (SELECT rg.id
        FROM release_groups rg
        INNER JOIN artists_release_groups rga ON rg.id = rga.release_group_id
        INNER JOIN artists a ON a.id = rga.artist_id
        WHERE a.id = :artistId)
    """
    )
    abstract suspend fun deleteAllReleaseGroupsByArtist(artistId: String)
}

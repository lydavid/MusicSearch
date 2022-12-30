package ly.david.data.persistence.artist.release

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.release.ReleaseWithCreditsAndCountries

@Dao
abstract class ArtistReleaseDao : BaseDao<ArtistRelease>() {

    companion object {
        private const val RELEASES_BY_ARTIST = """
            FROM release r
            INNER JOIN artist_release ar ON r.id = ar.release_id
            INNER JOIN artist a ON a.id = ar.artist_id
            LEFT JOIN artist_credit_resource acr ON acr.resource_id = r.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE a.id = :artistId
        """

        private const val SELECT_RELEASES_BY_ARTIST = """
            SELECT r.*, ac.name AS artist_credit_name
            $RELEASES_BY_ARTIST
        """

        private const val SELECT_RELEASES_ID_BY_ARTIST = """
            SELECT r.id
            $RELEASES_BY_ARTIST
        """

        private const val ORDER_BY_DATE_AND_TITLE = """
            ORDER BY r.date, r.name
        """

        private const val FILTERED = """
            AND (
                r.name LIKE :query OR r.disambiguation LIKE :query
                OR r.date LIKE :query OR r.country_code LIKE :query
                OR r.formats LIKE :query OR r.tracks LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    @Query(
        """
        DELETE FROM release WHERE id IN (
        $SELECT_RELEASES_ID_BY_ARTIST
        )
        """
    )
    abstract suspend fun deleteReleasesByArtist(artistId: String)

    @Query("DELETE FROM artist_release WHERE artist_id = :artistId")
    abstract suspend fun deleteArtistReleaseLinks(artistId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*) FROM
                (SELECT DISTINCT ar.release_id, ar.artist_id
                FROM release r
                INNER JOIN artist_release ar ON r.id = ar.release_id
                INNER JOIN artist a ON a.id = ar.artist_id
                WHERE a.id = :artistId)
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfReleasesByArtist(artistId: String): Int

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_ARTIST
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByArtist(artistId: String): PagingSource<Int, ReleaseWithCreditsAndCountries>

    @Transaction
    @Query(
        """
        $SELECT_RELEASES_BY_ARTIST
        $FILTERED
        $ORDER_BY_DATE_AND_TITLE
    """
    )
    abstract fun getReleasesByArtistFiltered(
        artistId: String,
        query: String
    ): PagingSource<Int, ReleaseWithCreditsAndCountries>
}

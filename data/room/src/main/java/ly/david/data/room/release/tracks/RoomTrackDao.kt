package ly.david.data.room.release.tracks

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.musicbrainz.TrackMusicBrainzModel
import ly.david.data.room.BaseDao
import ly.david.data.room.artist.credit.ArtistCreditDao

@Dao
abstract class RoomTrackDao : BaseDao<TrackRoomModel>(), ArtistCreditDao {

    companion object {
        private const val TRACKS_IN_RELEASE = """
            FROM track t
            INNER JOIN medium m ON t.medium_id = m.id
            INNER JOIN release r ON m.release_id = r.id
            LEFT JOIN artist_credit_entity acr ON acr.entity_id = t.id
            LEFT JOIN artist_credit ac ON ac.id = acr.artist_credit_id
            WHERE r.id = :releaseId
        """

        private const val SELECT_TRACKS_IN_RELEASE = """
            SELECT t.*, ac.name AS artist_credit_names
            $TRACKS_IN_RELEASE
        """

        private const val FILTERED = """
            AND (
                t.title LIKE :query OR t.number LIKE :query
                OR ac.name LIKE :query
            )
        """
    }

    @Transaction
    open suspend fun insertTrackWithArtistCredits(track: TrackMusicBrainzModel, mediumId: Long) {
        insertArtistCredits(artistCredits = track.artistCredits, entityId = track.id)
        insertReplace(track.toTrackRoomModel(mediumId))
    }

    @Transaction
    @Query(
        """
        $SELECT_TRACKS_IN_RELEASE
        $FILTERED
    """
    )
    abstract fun getTracksByRelease(
        releaseId: String,
        query: String = "%%",
    ): PagingSource<Int, RoomTrackForListItem>
}

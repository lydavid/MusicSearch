package ly.david.data.room.recording

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.RecordingMusicBrainzModel
import ly.david.data.room.BaseDao
import ly.david.data.room.artist.credit.ArtistCreditDao

@Dao
abstract class RecordingDao : BaseDao<RecordingRoomModel>(), ArtistCreditDao {

    @Transaction
    open suspend fun insertAllRecordingsWithArtistCredits(recordings: List<RecordingMusicBrainzModel>) {
        recordings.forEach { recording ->
            insertRecordingWithArtistCredits(recording)
        }
    }

    @Transaction
    open suspend fun insertRecordingWithArtistCredits(recording: RecordingMusicBrainzModel) {
        insertArtistCredits(artistCredits = recording.artistCredits, entityId = recording.id)
        insert(recording.toRoomModel())
    }

    @Query("SELECT * FROM recording WHERE id = :recordingId")
    abstract suspend fun getRecording(recordingId: String): RecordingRoomModel?

    @Transaction
    @Query("SELECT * FROM recording WHERE id = :recordingId")
    abstract suspend fun getRecordingWithArtistCredits(recordingId: String): RecordingForScaffold?
}

package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.toImmutableList
import ly.david.data.musicbrainz.RecordingMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Recording

class RecordingDao(
    database: Database,
) : EntityDao {
    override val transacter = database.recordingQueries

    fun insert(recording: RecordingMusicBrainzModel) {
        recording.run {
            transacter.insert(
                Recording(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    first_release_date = firstReleaseDate,
                    length = length,
                    video = video ?: false,
                    isrcs = isrcs?.toImmutableList(),
                )
            )
        }
    }

    fun insertAll(recordings: List<RecordingMusicBrainzModel>) {
        transacter.transaction {
            recordings.forEach { recording ->
                insert(recording)
            }
        }
    }

    fun getRecording(recordingId: String): Recording? {
        return transacter.getRecording(recordingId).executeAsOneOrNull()
    }
}

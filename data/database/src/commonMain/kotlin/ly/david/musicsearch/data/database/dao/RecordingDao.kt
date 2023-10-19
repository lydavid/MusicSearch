package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.core.models.recording.RecordingScaffoldModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Recording

class RecordingDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
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
                ),
            )
            artistCreditDao.insertArtistCredits(
                entityId = recording.id,
                artistCredits = artistCredits,
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

    fun getRecordingForDetails(recordingId: String): RecordingScaffoldModel? {
        return transacter.getRecording(
            recordingId,
            mapper = ::toRecordingScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun toRecordingScaffoldModel(
        id: String,
        name: String,
        disambiguation: String,
        firstReleaseDate: String?,
        length: Int?,
        video: Boolean,
        isrcs: List<String>?,
    ) = RecordingScaffoldModel(
        id = id,
        name = name,
        firstReleaseDate = firstReleaseDate,
        disambiguation = disambiguation,
        length = length,
        video = video,
        isrcs = isrcs,
    )
}

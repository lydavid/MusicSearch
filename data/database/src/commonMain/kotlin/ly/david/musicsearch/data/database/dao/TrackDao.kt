package ly.david.musicsearch.data.database.dao

import ly.david.data.musicbrainz.TrackMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Track

class TrackDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
) : EntityDao {
    override val transacter = database.trackQueries

    fun insertAll(
        mediumId: Long,
        tracks: List<TrackMusicBrainzModel>?,
    ) {
        transacter.transaction {
            tracks?.forEach { track ->
                insert(
                    mediumId = mediumId,
                    track = track,
                )
            }
        }
    }

    private fun insert(
        mediumId: Long,
        track: TrackMusicBrainzModel,
    ) {
        track.run {
            transacter.insert(
                Track(
                    id = id,
                    medium_id = mediumId,
                    position = position,
                    number = number,
                    title = title,
                    length = length,
                    recording_id = recording.id
                )
            )
            artistCreditDao.insertArtistCredits(
                entityId = track.id,
                artistCredits = artistCredits,
            )
        }
    }

//    fun getTrack(trackId: String): Track_group? =
//        transacter.getTrack(trackId).executeAsOneOrNull()
}

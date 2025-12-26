package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import lydavidmusicsearchdatadatabase.Medium

class MediumDao(
    database: Database,
    private val trackDao: TrackDao,
) : EntityDao {
    override val transacter = database.mediumQueries

    fun insertAll(
        releaseId: String,
        media: List<MediumMusicBrainzModel>?,
    ) {
        withTransaction {
            media?.forEach { medium ->
                val mediumId = insert(
                    releaseId = releaseId,
                    medium = medium,
                )
                trackDao.insertAll(
                    mediumId = mediumId,
                    tracks = medium.tracks,
                )
            }
        }
    }

    private fun insert(
        releaseId: String,
        medium: MediumMusicBrainzModel,
    ): Long {
        medium.run {
            transacter.insert(
                id = 0,
                releaseId = releaseId,
                position = position,
                name = title,
                trackCount = trackCount,
                format = format,
                formatId = formatId,
            )
        }
        // Release-media is 1-to-many, so there should be no failures
        return transacter.lastInsertRowId().executeAsOne()
    }

    fun getMediumForTrack(trackId: String): Medium? =
        transacter.getMediumForTrack(trackId).executeAsOneOrNull()

    fun deleteMediaByRelease(releaseId: String) {
        transacter.deleteMediaByRelease(releaseId)
    }
}

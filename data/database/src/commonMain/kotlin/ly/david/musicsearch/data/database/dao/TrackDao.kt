package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.TrackForListItem
import ly.david.data.musicbrainz.TrackMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToTrackForListItem
import lydavidmusicsearchdatadatabase.Track

class TrackDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val coroutineDispatchers: CoroutineDispatchers,
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

    fun getNumberOfTracksByRelease(releaseId: String): Int =
        transacter.getNumberOfTracksByRelease(
            releaseId = releaseId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getTracksByRelease(
        releaseId: String,
        query: String,
    ): PagingSource<Int, TrackForListItem> = QueryPagingSource(
        countQuery = transacter.getNumberOfTracksByRelease(
            releaseId = releaseId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getTracksByRelease(
            releaseId = releaseId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToTrackForListItem,
        )
    }
}

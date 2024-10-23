package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.TrackListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToTrackListItemModel
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel
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
                    recording_id = recording.id,
                ),
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
        collapsedMediumIds: Set<Long>,
    ): PagingSource<Int, TrackListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfTracksByRelease(
            releaseId = releaseId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getTracksByRelease(
                releaseId = releaseId,
                collapsedMediumIds = collapsedMediumIds,
                query = query,
                limit = limit,
                offset = offset,
                mapper = ::mapToTrackListItemModel,
            )
        },
    )
}

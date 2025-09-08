package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.combineToAliases
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
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
                    title = name,
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
        username: String,
    ): PagingSource<Int, TrackAndMedium> = QueryPagingSource(
        countQuery = transacter.getNumberOfTracksByRelease(
            releaseId = releaseId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getTracksByRelease(
                releaseId = releaseId,
                query = query,
                username = username,
                limit = limit,
                offset = offset,
                mapper = ::mapToTrackAndMedium,
            )
        },
    )
}

private fun mapToTrackAndMedium(
    id: String,
    mediumId: Long,
    recordingId: String,
    position: Int,
    number: String,
    title: String,
    length: Int?,
    formattedArtistCreditNames: String,
    visited: Boolean?,
    mediumPosition: Int?,
    mediumName: String?,
    trackCount: Int,
    format: String?,
    aliasNames: String?,
    aliasLocales: String?,
    listenCount: Long?,
) = TrackAndMedium(
    id = id,
    position = position,
    number = number,
    title = title,
    length = length,
    mediumId = mediumId,
    recordingId = recordingId,
    formattedArtistCredits = formattedArtistCreditNames,
    visited = visited == true,
    aliases = combineToAliases(aliasNames, aliasLocales),
    listenCount = listenCount,
    mediumPosition = mediumPosition ?: 0,
    mediumName = mediumName,
    trackCount = trackCount,
    format = format,
)

data class TrackAndMedium(
    val id: String,
    val position: Int,
    val number: String,
    val title: String,
    val length: Int? = null,
    val mediumId: Long = 0,
    val recordingId: String = "",
    val formattedArtistCredits: String? = null,
    val visited: Boolean = false,
    val aliases: ImmutableList<BasicAlias>,
    val listenCount: Long? = null,

    val mediumPosition: Int,
    val mediumName: String?,
    val trackCount: Int,
    val format: String?,
)

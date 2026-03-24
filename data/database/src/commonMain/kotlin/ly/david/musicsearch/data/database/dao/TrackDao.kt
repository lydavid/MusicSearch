package ly.david.musicsearch.data.database.dao

import androidx.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.combineToAliases
import ly.david.musicsearch.data.database.mapper.combineToArtistCredits
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.listitem.SelectableId
import lydavidmusicsearchdatadatabase.Track

class TrackDao(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val recordingDao: RecordingDao,
    private val aliasDao: AliasDao,
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

            val recordings = tracks?.map { it.recording }.orEmpty()
            recordingDao.upsertAll(
                recordings = recordings,
            )
            aliasDao.insertAll(
                musicBrainzNetworkModels = recordings,
            )
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

    fun observeCountOfTracksByRelease(releaseId: String): Flow<Int> =
        transacter.getCountOfTracksByRelease(
            releaseId = releaseId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getTracksByRelease(
        releaseId: String,
        query: String,
        username: String,
    ): PagingSource<Int, TrackAndMedium> {
        val queryWithWildcards = "%$query%"
        return QueryPagingSource(
            countQuery = transacter.getCountOfTracksByRelease(
                releaseId = releaseId,
                query = queryWithWildcards,
            ),
            transacter = transacter,
            context = coroutineDispatchers.io,
            queryProvider = { limit, offset ->
                transacter.getTracksByRelease(
                    releaseId = releaseId,
                    query = queryWithWildcards,
                    username = username,
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToTrackAndMedium,
                )
            },
        )
    }

    fun getAllTrackIdsByRelease(releaseId: String): List<SelectableId> {
        return transacter.getAllTrackIdsByRelease(
            releaseId = releaseId,
        ).executeAsList().map {
            SelectableId(
                id = it.id,
                recordingId = it.recording_id,
            )
        }
    }
}

private fun mapToTrackAndMedium(
    id: String,
    mediumId: Long,
    recordingId: String,
    position: Int,
    number: String,
    title: String,
    length: Int?,
    artistCreditNames: String?,
    artistCreditIds: String?,
    artistCreditJoinPhrases: String?,
    visited: Boolean?,
    mediumPosition: Int?,
    mediumName: String?,
    trackCount: Int,
    format: String?,
    collected: Boolean?,
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
    artistCredits = combineToArtistCredits(
        names = artistCreditNames,
        ids = artistCreditIds,
        joinPhrases = artistCreditJoinPhrases,
    ),
    visited = visited == true,
    collected = collected == true,
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
    val artistCredits: ImmutableList<ArtistCreditUiModel>,
    val visited: Boolean = false,
    val collected: Boolean = false,
    val aliases: ImmutableList<BasicAlias>,
    val listenCount: Long? = null,

    val mediumPosition: Int,
    val mediumName: String?,
    val trackCount: Int,
    val format: String?,
)

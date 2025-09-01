package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.combineToAliases
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.listen.Listen
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import kotlin.time.Instant

class ListenDaoImpl(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val imageUrlDao: ImageUrlDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ListenDao {
    private val listenTransacter = database.listenQueries
    private val recordingTransacter = database.recordingQueries
    private val releaseTransacter = database.releaseQueries

    @Suppress("LongMethod")
    override fun insert(
        listens: List<Listen>,
    ) {
        listenTransacter.transaction {
            listens.forEach { listen ->
                val recordingMusicbrainzId = listen.recordingMusicbrainzId
                val durationMs = listen.durationMs
                val coverArtId = listen.caaId
                val coverArtReleaseMbid = listen.caaReleaseMbid
                val releaseName = listen.releaseName
                listenTransacter.insert(
                    listen = lydavidmusicsearchdatadatabase.Listen(
                        inserted_at_ms = listen.insertedAtMs,
                        listened_at_ms = listen.listenedAtMs,
                        recording_messybrainz_id = listen.recordingMessybrainzId,
                        username = listen.username,
                        recording_musicbrainz_id = recordingMusicbrainzId,
                        caa_id = coverArtId,
                        caa_release_mbid = coverArtReleaseMbid,
                        artist_name = listen.artistName,
                        track_name = listen.trackName,
                        release_name = releaseName,
                        duration_ms = durationMs,
                        media_player = listen.mediaPlayer,
                        submission_client = listen.submissionClient,
                        music_service = listen.musicService,
                        music_service_name = listen.musicServiceName,
                        origin_url = listen.originUrl,
                        spotify_album_artist_ids = listen.spotifyAlbumArtistIds,
                        spotify_album_id = listen.spotifyAlbumId,
                        spotify_artist_ids = listen.spotifyArtistIds,
                        spotify_id = listen.spotifyId,
                    ),
                )

                // Add stub recording so that we can link it with artist credits
                val recordingName = listen.recordingName
                if (recordingMusicbrainzId == null || recordingName == null) return@forEach
                recordingTransacter.insert(
                    id = recordingMusicbrainzId,
                    name = recordingName,
                    disambiguation = "",
                    firstReleaseDate = "",
                    length = durationMs?.toInt(),
                    video = false,
                    isrcs = emptyList(),
                )

                artistCreditDao.insertArtistCredits(
                    entityId = recordingMusicbrainzId,
                    artistCredits = listen.artistCredits.map { artist ->
                        ArtistCreditMusicBrainzModel(
                            artist = ArtistMusicBrainzNetworkModel(
                                id = artist.artistId,
                            ),
                            name = artist.name,
                            joinPhrase = artist.joinPhrase,
                        )
                    },
                )

                if (coverArtReleaseMbid == null || coverArtId == null) return@forEach
                val coverArtUrl = "https://coverartarchive.org/release/$coverArtReleaseMbid/$coverArtId"
                imageUrlDao.saveImageMetadata(
                    mbid = coverArtReleaseMbid,
                    imageMetadataList = listOf(
                        ImageMetadata(
                            thumbnailUrl = "$coverArtUrl-250",
                            largeUrl = "$coverArtUrl-1200",
                        ),
                    ),
                )

                // Add stub release so that we can link it with its cover art
                if (releaseName == null) return@forEach
                releaseTransacter.insert(
                    id = coverArtReleaseMbid,
                    name = releaseName,
                    disambiguation = "",
                    date = "",
                    barcode = "",
                    status_id = "",
                    country_code = "",
                    packaging = "",
                    packaging_id = "",
                    asin = "",
                    quality = "",
                    script = "",
                    language = "",
                )
            }
        }
    }

    override fun deleteListensByUser(username: String) {
        listenTransacter.deleteByUser(username = username)
    }

    override fun observeUnfilteredCountOfListensByUser(username: String): Flow<Long?> {
        return listenTransacter
            .getUnfilteredCountOfListensByUser(username = username)
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
    }

    override fun getListensByUser(
        username: String,
        query: String,
    ): PagingSource<Int, ListenListItemModel> {
        val queryWithWildcards = "%$query%"
        return QueryPagingSource(
            countQuery = listenTransacter.getCountOfListensByUser(
                username = username,
                query = queryWithWildcards,
            ),
            transacter = listenTransacter,
            context = coroutineDispatchers.io,
            queryProvider = { limit, offset ->
                listenTransacter
                    .getListensByUser(
                        username = username,
                        query = queryWithWildcards,
                        limit = limit,
                        offset = offset,
                        mapper = ::mapToListenListItemModel,
                    )
            },
        )
    }

    override fun getLatestTimestampMsByUser(username: String): Long? {
        return listenTransacter
            .getLatestTimestampByUser(username = username)
            .executeAsOneOrNull()
            ?.timestamp
    }

    override fun getOldestTimestampMsByUser(username: String): Long? {
        return listenTransacter
            .getOldestTimestampByUser(username = username)
            .executeAsOneOrNull()
            ?.timestamp
    }
}

private fun mapToListenListItemModel(
    listenedAtMs: Long,
    username: String,
    recordingMessybrainzId: String,
    recordingMusicbrainzId: String?,
    recordingName: String?,
    recordingDisambiguation: String?,
    fallbackName: String,
    durationMs: Long?,
    artistCreditNames: String?,
    fallbackArtistCreditNames: String,
    releaseName: String?,
    releaseId: String?,
    imageUrl: String?,
    imageId: Long?,
    visitedRecording: Boolean,
    visitedRelease: Boolean,
//    media_player: String?,
//    submission_client: String?,
//    music_service: String?,
//    music_service_name: String?,
//    origin_url: String?,
//    spotify_album_artist_ids: List<String>?,
//    spotify_album_id: String?,
//    spotify_artist_ids: List<String>?,
//    spotify_id: String?,
    aliasNames: String?,
    aliasLocales: String?,
) = ListenListItemModel(
    id = "${listenedAtMs}_${username}_$recordingMessybrainzId",
    name = recordingName ?: fallbackName,
    disambiguation = recordingDisambiguation.orEmpty(),
    formattedArtistCredits = artistCreditNames ?: fallbackArtistCreditNames,
    listenedAt = Instant.fromEpochMilliseconds(listenedAtMs),
    recordingId = recordingMusicbrainzId,
    durationMs = durationMs?.toInt(),
    imageUrl = imageUrl,
    imageId = imageId?.let { ImageId(it) },
    visited = visitedRecording,
    release = ListenRelease(
        name = releaseName,
        id = releaseId,
        visited = visitedRelease,
    ),
    aliases = combineToAliases(aliasNames, aliasLocales),
)

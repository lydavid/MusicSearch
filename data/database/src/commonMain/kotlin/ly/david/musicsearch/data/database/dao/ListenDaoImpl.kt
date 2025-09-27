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
import ly.david.musicsearch.shared.domain.list.FacetListItem
import ly.david.musicsearch.shared.domain.listen.Listen
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

class ListenDaoImpl(
    database: Database,
    private val artistCreditDao: ArtistCreditDao,
    private val imageUrlDao: ImageUrlDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : ListenDao {
    private val listenTransacter = database.listenQueries
    private val recordingTransacter = database.recordingQueries
    private val releaseTransacter = database.releaseQueries

    override fun insert(
        listens: List<Listen>,
    ) {
        listenTransacter.transaction {
            listens.forEach { listen ->
                listenTransacter.insert(
                    listen = lydavidmusicsearchdatadatabase.Listen(
                        inserted_at_ms = listen.insertedAtMs,
                        listened_at_ms = listen.listenedAtMs,
                        recording_messybrainz_id = listen.recordingMessybrainzId,
                        username = listen.username,
                        recording_musicbrainz_id = listen.entityMapping.recordingMusicbrainzId.orEmpty(),
                        release_mbid = listen.entityMapping.releaseMbid,
                        caa_id = listen.entityMapping.caaId,
                        caa_release_mbid = listen.entityMapping.caaReleaseMbid,
                        artist_name = listen.artistName,
                        track_name = listen.trackName,
                        release_name = listen.entityMapping.releaseName,
                        duration_ms = listen.entityMapping.durationMs,
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
                insertLinkedEntities(entityMapping = listen.entityMapping)
            }
        }
    }

    @Suppress("ReturnCount")
    private fun insertLinkedEntities(
        entityMapping: Listen.EntityMapping,
    ) {
        // Add stub recording so that we can link it with artist credits
        val recordingMusicbrainzId = entityMapping.recordingMusicbrainzId
        val recordingName = entityMapping.recordingName
        if (recordingMusicbrainzId == null || recordingName == null) return
        recordingTransacter.insert(
            id = recordingMusicbrainzId,
            name = recordingName,
            disambiguation = "",
            firstReleaseDate = "",
            length = entityMapping.durationMs?.toInt(),
            video = false,
            isrcs = emptyList(),
        )

        artistCreditDao.insertArtistCredits(
            entityId = recordingMusicbrainzId,
            artistCredits = entityMapping.artistCredits.map { artist ->
                ArtistCreditMusicBrainzModel(
                    artist = ArtistMusicBrainzNetworkModel(
                        id = artist.artistId,
                    ),
                    name = artist.name,
                    joinPhrase = artist.joinPhrase,
                )
            },
        )

        val coverArtId = entityMapping.caaId
        val coverArtReleaseMbid = entityMapping.caaReleaseMbid
        if (coverArtReleaseMbid == null || coverArtId == null) return
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

        // Add stub release so that we can link it with its cover art.
        // We want this release to use the cover art release mbid.
        // The release mbid on the listen may not be the same as this, but when the user navigates to that release
        // for the first time, they will get its cover art if it has any.
        val releaseName = entityMapping.releaseName
        if (releaseName == null) return
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

    override fun deleteListensByUser(username: String) {
        listenTransacter.deleteByUser(username = username)
    }

    override fun updateMetadata(
        recordingMessyBrainzId: String,
        artistName: String?,
        entityMapping: Listen.EntityMapping,
    ) {
        listenTransacter.transaction {
            // Do not update track name or artist name because they are guaranteed fields uploaded by the submission client
            // that can be useful fallbacks when filtering, especially when the recording is in a different language
            // without aliases.
            listenTransacter.updateMetadata(
                recording_musicbrainz_id = entityMapping.recordingMusicbrainzId.orEmpty(),
                release_mbid = entityMapping.releaseMbid,
                caa_id = entityMapping.caaId,
                caa_release_mbid = entityMapping.caaReleaseMbid,
                release_name = entityMapping.releaseName,
                duration_ms = entityMapping.durationMs,
                recording_messybrainz_id = recordingMessyBrainzId,
            )
            insertLinkedEntities(entityMapping = entityMapping)
        }
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
        facetEntity: MusicBrainzEntity?,
    ): PagingSource<Int, ListenListItemModel> {
        val queryWithWildcards = "%$query%"
        val recordingId = facetEntity.takeIf { it?.type == MusicBrainzEntityType.RECORDING }?.id
        val releaseId = facetEntity.takeIf { it?.type == MusicBrainzEntityType.RELEASE }?.id
        return QueryPagingSource(
            countQuery = listenTransacter.getCountOfListensByUser(
                username = username,
                query = queryWithWildcards,
                recordingId = recordingId,
                releaseId = releaseId,
            ),
            transacter = listenTransacter,
            context = coroutineDispatchers.io,
            queryProvider = { limit, offset ->
                listenTransacter.getListensByUser(
                    username = username,
                    query = queryWithWildcards,
                    recordingId = recordingId,
                    releaseId = releaseId,
                    limit = limit,
                    offset = offset,
                    mapper = ::mapToListenListItemModel,
                )
            },
        )
    }

    override fun getFacetsByUser(
        entityType: MusicBrainzEntityType,
        username: String,
        query: String,
    ): PagingSource<Int, FacetListItem> {
        val queryWithWildcards = "%$query%"
        return when (entityType) {
            MusicBrainzEntityType.RECORDING -> {
                QueryPagingSource(
                    countQuery = listenTransacter.getCountOfRecordingFacets(
                        username = username,
                        query = queryWithWildcards,
                    ),
                    transacter = listenTransacter,
                    context = coroutineDispatchers.io,
                    queryProvider = { limit, offset ->
                        listenTransacter.getRecordingFacets(
                            username = username,
                            query = queryWithWildcards,
                            limit = limit,
                            offset = offset,
                            mapper = ::mapToRecordingFacet,
                        )
                    },
                )
            }
            MusicBrainzEntityType.RELEASE -> {
                QueryPagingSource(
                    countQuery = listenTransacter.getCountOfReleaseFacets(
                        username = username,
                        query = queryWithWildcards,
                    ),
                    transacter = listenTransacter,
                    context = coroutineDispatchers.io,
                    queryProvider = { limit, offset ->
                        listenTransacter.getReleaseFacets(
                            username = username,
                            query = queryWithWildcards,
                            limit = limit,
                            offset = offset,
                            mapper = ::mapToReleaseFacet,
                        )
                    },
                )
            }
            else -> error("Unsupported entity type: $entityType")
        }
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
    recordingMessybrainzId: String,
    username: String,
    recordingMusicbrainzId: String,
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
    name = recordingName ?: fallbackName,
    username = username,
    recordingMessybrainzId = recordingMessybrainzId,
    disambiguation = recordingDisambiguation.orEmpty(),
    formattedArtistCredits = artistCreditNames ?: fallbackArtistCreditNames,
    listenedAtMs = listenedAtMs,
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

// TODO: include recording aliases in facets list
private fun mapToRecordingFacet(
    recordingMusicbrainzId: String?,
    recordingName: String?,
    disambiguation: String?,
    artistCreditNames: String?,
    count: Long,
) = FacetListItem(
    id = recordingMusicbrainzId.orEmpty(),
    name = recordingName.orEmpty(),
    disambiguation = disambiguation.orEmpty(),
    formattedArtistCredits = artistCreditNames.orEmpty(),
    count = count.toInt(),
)

private fun mapToReleaseFacet(
    recordingMusicbrainzId: String?,
    recordingName: String?,
    disambiguation: String?,
    artistCreditNames: String?,
    aliasNames: String?,
    aliasLocales: String?,
    count: Long,
) = FacetListItem(
    id = recordingMusicbrainzId.orEmpty(),
    name = recordingName.orEmpty(),
    disambiguation = disambiguation.orEmpty(),
    formattedArtistCredits = artistCreditNames.orEmpty(),
    aliases = combineToAliases(
        aliasNames = aliasNames,
        aliasLocales = aliasLocales,
    ),
    count = count.toInt(),
)

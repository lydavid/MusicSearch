package ly.david.musicsearch.data.listenbrainz.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listen.Listen

@Serializable
data class ListensResponse(
    val payload: Payload,
)

/**
 * [latest_listen_ts] and [oldest_listen_ts] are for all listens of the user.
 */
@Suppress("ConstructorParameterNaming")
@Serializable
data class Payload(
    val latest_listen_ts: Long,
    val oldest_listen_ts: Long,
    val listens: List<ListenBrainzListen>,
)

/**
 * @param insertedAtS When the record was created in ListenBrainz in unix epoch seconds.
 * @param listenedAtS When the user listened to the recording in unix epoch seconds. Could be backdated.
 */
@Suppress("ConstructorParameterNaming")
@Serializable
data class ListenBrainzListen(
    @SerialName("inserted_at")
    val insertedAtS: Long,
    @SerialName("listened_at")
    val listenedAtS: Long,
    val recording_msid: String,
    val user_name: String,
    val track_metadata: TrackMetadata,
)

/**
 * @param artist_name All artist names and join phrases as they appear in the listen submission.
 *  If there's no [mbid_mapping], we can fallback to display this.
 *  May not match [ListenBrainzArtist.artist_credit_name] or [ListenBrainzArtist.join_phrase].
 *  This may include the localized name.
 * @param track_name Fallback to display this if [mbid_mapping] is missing.
 *
 * [artist_name] and [track_name] must exist: https://github.com/metabrainz/listenbrainz-server/blob/e18111661afaf18834dfce4e138e380e674564cd/docs/users/json.rst#payload-json-details
 */
@Suppress("ConstructorParameterNaming")
@Serializable
data class TrackMetadata(
    val artist_name: String,
    val track_name: String,
    val release_name: String? = null,
    val additional_info: AdditionalInfo? = null,
    val mbid_mapping: MbidMapping? = null,
//    val brainzplayer_metadata: BrainzplayerMetadata?,
)

/**
 * @param media_player e.g. BrainzPlayer
 * @param submission_client e.g. listenbrainz, listenbrainz web
 * @param music_service e.g. youtube.com
 * @param music_service_name e.g. youtube
 * @param origin_url May be null if submitted through listenbrainz web.
 * @param artist_names All actual artist names.
 * @param release_artist_names All artist names on the release, may be "Various Artist".
 */
@Suppress("ConstructorParameterNaming")
@Serializable
data class AdditionalInfo(
    val duration_ms: Long? = null,
    val media_player: String? = null,
    val submission_client: String? = null,

    val music_service: String? = null,
    val music_service_name: String? = null,
    val origin_url: String? = null,

    val spotify_album_artist_ids: List<String>? = null,
    val spotify_album_id: String? = null,
    val spotify_artist_ids: List<String>? = null,
    val spotify_id: String? = null,

    // prefer TrackMetadata.artist_name when we're missing MB artist
//    val artist_names: List<String>?,
//    val release_artist_names: List<String>?,

    // let recording be source of truth
//    val isrc: String?,

    // let track be source of truth
//    val discnumber: Int?,

    // let track be source of truth
    // could be a string when submitted through listenbrainz web, otherwise int
//    val tracknumber: String?,

    // jq '.payload.listens[] | select(.track_metadata.additional_info.recording_msid != .recording_msid) | { a: .track_metadata.additional_info.recording_msid, b: .recording_msid }' scratch/listens.json
    // was empty for 2000 of my listens, so I'm assuming they are the same.
//    val recording_msid: String,
)

/**
 *
 * We can build a CAA url with [caa_release_mbid] and [caa_id].
 * e.g. https://coverartarchive.org/release/0f3dacd9-0d10-43c8-9f69-c0663aae111a/35847079672-250
 */
@Suppress("ConstructorParameterNaming")
@Serializable
data class MbidMapping(
    val recording_mbid: String,
    // Although we would expect having a matching recording would give us a recording name and its artists,
    // sometimes we do not have these.
    // It may be because of a bug caused when a recording has been merged into another one.
    val recording_name: String? = null,
    val artists: List<ListenBrainzArtist>? = null,
    val caa_id: Long? = null,
    val caa_release_mbid: String? = null,
    val release_mbid: String? = null,
)

@Suppress("ConstructorParameterNaming")
@Serializable
data class ListenBrainzArtist(
    val artist_credit_name: String,
    val artist_mbid: String,
    val join_phrase: String,
)

// @Suppress("ConstructorParameterNaming")
// data class BrainzplayerMetadata(
//    val artist_name: String,
//    val track_name: String,
//    val release_name: String?,
// )

fun ListensResponse.asListOfListens(): List<Listen> {
    return payload.listens.map { listen ->
        val trackMetadata = listen.track_metadata
        val mbidMapping = trackMetadata.mbid_mapping
        val additionalInfo = trackMetadata.additional_info
        val recordingMusicbrainzId = mbidMapping?.recording_mbid
        val durationMs = additionalInfo?.duration_ms
        Listen(
            insertedAtMs = listen.insertedAtS * MS_IN_SECOND,
            listenedAtMs = listen.listenedAtS * MS_IN_SECOND,
            recordingMessybrainzId = listen.recording_msid,
            username = listen.user_name,
            artistName = trackMetadata.artist_name,
            trackName = trackMetadata.track_name,
            mediaPlayer = additionalInfo?.media_player,
            submissionClient = additionalInfo?.submission_client,
            musicService = additionalInfo?.music_service,
            musicServiceName = additionalInfo?.music_service_name,
            originUrl = additionalInfo?.origin_url,
            spotifyAlbumArtistIds = additionalInfo?.spotify_album_artist_ids,
            spotifyAlbumId = additionalInfo?.spotify_album_id,
            spotifyArtistIds = additionalInfo?.spotify_artist_ids,
            spotifyId = additionalInfo?.spotify_id,
            entityMapping = Listen.EntityMapping(
                recordingMusicbrainzId = recordingMusicbrainzId,
                recordingName = mbidMapping?.recording_name,
                durationMs = durationMs,
                caaId = mbidMapping?.caa_id,
                caaReleaseMbid = mbidMapping?.caa_release_mbid,
                releaseMbid = mbidMapping?.release_mbid,
                releaseName = trackMetadata.release_name,
                artistCredits = mbidMapping?.artists?.map { artist ->
                    ArtistCreditUiModel(
                        artistId = artist.artist_mbid,
                        name = artist.artist_credit_name,
                        joinPhrase = artist.join_phrase,
                    )
                }.orEmpty(),
            ),
        )
    }
}

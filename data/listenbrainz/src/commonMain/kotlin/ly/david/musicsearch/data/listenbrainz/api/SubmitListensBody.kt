package ly.david.musicsearch.data.listenbrainz.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.listen.ListenSubmission

/**
 * https://listenbrainz.readthedocs.io/en/latest/users/json.html#submission-json
 */
@Serializable
data class SubmitListensBody(
    @SerialName("listen_type")
    val listenType: String,
    @SerialName("payload")
    val listenSubmissions: List<ListenBrainzListenSubmission>,
) {
    @Serializable
    data class ListenBrainzListenSubmission(
        @SerialName("listened_at")
        val listenedAtS: Long,
        @SerialName("track_metadata")
        val trackMetadata: TrackMetadata,
    ) {
        @Serializable
        data class TrackMetadata(
            @SerialName("artist_name")
            val artistName: String,
            @SerialName("track_name")
            val trackName: String,
            @SerialName("release_name")
            val releaseName: String? = null,
            @SerialName("additional_info")
            val additionalInfo: AdditionalInfo? = null,
        ) {
            @Serializable
            data class AdditionalInfo(
                @SerialName("release_mbid")
                val releaseMbid: String? = null,
                @SerialName("artist_mbids")
                val artistMbids: List<String>? = null,
                @SerialName("recording_mbid")
                val recordingMbid: String? = null,
                @SerialName("duration_ms")
                val durationMs: Long? = null,
                @SerialName("submission_client")
                val submissionClient: String? = null,
                @SerialName("submission_client_version")
                val submissionClientVersion: String? = null,
            )
        }
    }
}

internal fun ListenSubmission.toListenBrainzListenSubmission() = SubmitListensBody.ListenBrainzListenSubmission(
    listenedAtS = listenedAtS,
    trackMetadata = SubmitListensBody.ListenBrainzListenSubmission.TrackMetadata(
        artistName = artistName,
        trackName = trackName,
        releaseName = releaseName,
        additionalInfo = SubmitListensBody.ListenBrainzListenSubmission.TrackMetadata.AdditionalInfo(
            releaseMbid = releaseMbid,
            artistMbids = artistMbids,
            recordingMbid = recordingMbid,
            durationMs = durationMs,
            // TODO: submit submission client/version if user opts into this option
        )
    )
)

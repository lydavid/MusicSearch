package ly.david.musicsearch.data.listenbrainz.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordingMetadata(
    @SerialName("artist")
    val artistCredit: ArtistCredit,
    @SerialName("recording")
    val recording: Recording,
    @SerialName("release")
    val release: Release?,
) {
    @Serializable
    data class ArtistCredit(
        @SerialName("name")
        val name: String,
        @SerialName("artists")
        val artists: List<Artist>,
    ) {
        @Serializable
        data class Artist(
            @SerialName("artist_mbid")
            val artistMbid: String,
            @SerialName("join_phrase")
            val joinPhrase: String,
            @SerialName("name")
            val name: String,
        )
    }

    @Serializable
    data class Recording(
        @SerialName("length")
        val length: Long? = null,
        @SerialName("name")
        val name: String,
    )

    @Serializable
    data class Release(
        @SerialName("caa_id")
        val caaId: Long? = null,
        @SerialName("caa_release_mbid")
        val caaReleaseMbid: String? = null,
        @SerialName("mbid")
        val mbid: String? = null,
        @SerialName("name")
        val name: String? = null,
    )
}

package ly.david.musicsearch.data.listenbrainz.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManualMappingResponse(
    val mapping: Mapping,
) {
    @Serializable
    data class Mapping(
        @SerialName("recording_mbid")
        val recordingMbid: String,
        @SerialName("recording_msid")
        val recordingMsid: String,
    )
}

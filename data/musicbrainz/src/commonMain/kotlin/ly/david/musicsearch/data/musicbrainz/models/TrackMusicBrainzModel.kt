package ly.david.musicsearch.data.musicbrainz.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.release.Track
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel

/**
 * A [Track] in [MediumMusicBrainzModel].
 */
@Serializable
data class TrackMusicBrainzModel(
    @SerialName("id") override val id: String,
    @SerialName("position") override val position: Int,
    @SerialName("number") override val number: String, // Usually a string of `position`, but could be things like `A1`
    @SerialName("title") override val title: String,
    @SerialName("length") override val length: Int? = null,

    @SerialName("artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,
    @SerialName("recording") val recording: RecordingMusicBrainzNetworkModel,
//    @SerialName("relations") val relations: List<Relation>? = null,
) : Track

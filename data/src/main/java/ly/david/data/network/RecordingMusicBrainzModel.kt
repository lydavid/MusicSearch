package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.Recording
import ly.david.data.persistence.recording.RecordingArtistCreditRoomModel

data class RecordingMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "title") override val name: String,
    @Json(name = "first-release-date") override val date: String? = null,
    @Json(name = "disambiguation") override val disambiguation: String = "",
    @Json(name = "length") override val length: Int? = null,
    @Json(name = "video") override val video: Boolean? = false,

    @Json(name = "artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Recording

// TODO: unit test
//  and look into generalizing
/**
 * Returns all artist credits for this release for caching in database.
 */
internal fun RecordingMusicBrainzModel.getReleaseArtistCreditRoomModels(): List<RecordingArtistCreditRoomModel> =
    artistCredits?.mapIndexed { index, artistCredit ->
        RecordingArtistCreditRoomModel(
            recordingId = id,
            artistId = artistCredit.artist.id,
            name = artistCredit.name,
            joinPhrase = artistCredit.joinPhrase,
            order = index
        )
    }.orEmpty()

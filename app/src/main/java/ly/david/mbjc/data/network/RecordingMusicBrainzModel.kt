package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Recording

internal data class RecordingMusicBrainzModel(
    @Json(name = "id") override val id: String,
    @Json(name = "title") override val name: String,
    @Json(name = "first-release-date") override val date: String? = null,
    @Json(name = "disambiguation") override val disambiguation: String = "",
    @Json(name = "length") override val length: Int? = null,
    @Json(name = "video") override val video: Boolean? = false,

    @Json(name = "artist-credit") val artistCredits: List<ArtistCreditMusicBrainzModel>? = null,
    @Json(name = "relations") val relations: List<RelationMusicBrainzModel>? = null
) : MusicBrainzModel(), Recording

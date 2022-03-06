package ly.david.mbjc.data

import com.squareup.moshi.Json
import ly.david.mbjc.data.network.MusicBrainzArtistCredit

data class Recording(
    @Json(name = "id") val id: String,
    @Json(name = "title") override val name: String,
    @Json(name = "first-release-date") val date: String? = null,
    @Json(name = "disambiguation") override val disambiguation: String = "",
    @Json(name = "length") val length: Int? = null,
    @Json(name = "video") val video: Boolean = false,
    @Json(name = "artist-credit") val artistCredits: List<MusicBrainzArtistCredit>? = null,
): NameWithDisambiguation

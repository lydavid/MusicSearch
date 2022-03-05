package ly.david.mbjc.data

import com.squareup.moshi.Json

data class ArtistCredit(
    @Json(name = "artist") val artist: Artist,
    @Json(name = "joinphrase") val joinPhrase: String, // How to join the artist names: "", " / "
    @Json(name = "name") val name: String,
)

/**
 * Get all artists in the credit, joined by their [ArtistCredit.joinPhrase].
 */
fun List<ArtistCredit>?.getDisplayNames(): String {
    var displayName = ""
    this?.forEach {
        displayName += "${it.name}${it.joinPhrase}"
    }
    return displayName
}

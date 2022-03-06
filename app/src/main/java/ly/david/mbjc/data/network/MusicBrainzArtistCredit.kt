package ly.david.mbjc.data.network

import com.squareup.moshi.Json
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.ArtistCredit

data class MusicBrainzArtistCredit(

    @Json(name = "artist")
    val artist: Artist,

    @Json(name = "name")
    override val name: String,

    @Json(name = "joinphrase")
    override val joinPhrase: String,
) : ArtistCredit

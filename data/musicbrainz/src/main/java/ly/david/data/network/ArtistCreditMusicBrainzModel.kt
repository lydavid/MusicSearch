package ly.david.data.network

import com.squareup.moshi.Json
import ly.david.data.ArtistCreditName

data class ArtistCreditMusicBrainzModel(

    @Json(name = "artist")
    val artist: ArtistMusicBrainzModel,

    @Json(name = "name")
    override val name: String,

    // At least returns "" for browse, but could be null for query
    @Json(name = "joinphrase")
    override val joinPhrase: String? = null,
) : ArtistCreditName

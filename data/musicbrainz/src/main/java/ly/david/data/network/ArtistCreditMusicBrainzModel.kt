package ly.david.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.data.ArtistCreditName

@Serializable
data class ArtistCreditMusicBrainzModel(

    @SerialName("artist")
    val artist: ArtistMusicBrainzModel,

    @SerialName("name")
    override val name: String,

    // At least returns "" for browse, but could be null for query
    @SerialName("joinphrase")
    override val joinPhrase: String? = null,
) : ArtistCreditName

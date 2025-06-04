package ly.david.musicsearch.data.musicbrainz.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.artist.ArtistCreditName
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel

@Serializable
data class ArtistCreditMusicBrainzModel(

    @SerialName("artist")
    val artist: ArtistMusicBrainzNetworkModel,

    @SerialName("name")
    override val name: String,

    // At least returns "" for browse, but could be null for query
    @SerialName("joinphrase")
    override val joinPhrase: String? = null,
) : ArtistCreditName

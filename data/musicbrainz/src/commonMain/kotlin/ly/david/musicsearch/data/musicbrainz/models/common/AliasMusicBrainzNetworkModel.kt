package ly.david.musicsearch.data.musicbrainz.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ly.david.musicsearch.shared.domain.alias.Alias

@Serializable
data class AliasMusicBrainzNetworkModel(
    @SerialName("name") override val name: String,
    @SerialName("primary") override val isPrimary: Boolean? = null,
    @SerialName("locale") override val locale: String? = null,
    @SerialName("type-id") val typeId: String? = null,
    @SerialName("begin") val beginDate: String? = null,
    @SerialName("end") val endDate: String? = null,
    @SerialName("ended") val ended: Boolean? = null,
) : Alias

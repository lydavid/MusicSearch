package ly.david.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LabelInfo(
    @SerialName("catalog-number") val catalogNumber: String? = null,
    @SerialName("label") val label: LabelMusicBrainzModel? = null,
)

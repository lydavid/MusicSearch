package ly.david.data.musicbrainz

import kotlinx.serialization.Serializable
import ly.david.data.core.Coordinates

@Serializable
data class CoordinatesMusicBrainzModel(
    override val longitude: Double? = null,
    override val latitude: Double? = null,
) : Coordinates

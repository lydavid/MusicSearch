package ly.david.data.musicbrainz

import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.core.place.Coordinates

@Serializable
data class CoordinatesMusicBrainzModel(
    override val longitude: Double? = null,
    override val latitude: Double? = null,
) : Coordinates

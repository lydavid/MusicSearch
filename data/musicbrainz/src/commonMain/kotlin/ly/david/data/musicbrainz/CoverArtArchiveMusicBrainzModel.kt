package ly.david.data.musicbrainz

import kotlinx.serialization.Serializable
import ly.david.musicsearch.data.core.release.CoverArtArchive

@Serializable
data class CoverArtArchiveMusicBrainzModel(
    //    @SerialName("darkened") val darkened: Boolean = false,
//    @SerialName("artwork") val artwork: Boolean = false,
//    @SerialName("back") val back: Boolean = false,
//    @SerialName("front") val front: Boolean = false,
    override val count: Int = 0,
) : CoverArtArchive

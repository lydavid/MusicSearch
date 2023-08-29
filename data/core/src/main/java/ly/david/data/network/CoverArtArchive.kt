package ly.david.data.network

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

interface CoverArtArchive {
//    @SerialName("darkened") val darkened: Boolean = false,
//    @SerialName("artwork") val artwork: Boolean = false,
//    @SerialName("back") val back: Boolean = false,
//    @SerialName("front") val front: Boolean = false,
    val count: Int
}

data class CoverArtArchiveUiModel(
    override val count: Int = 0,
): CoverArtArchive

data class CoverArtArchiveRoomModel(
    @ColumnInfo(name = "cover_art_count")
    override val count: Int = 0,
): CoverArtArchive

@Serializable
data class CoverArtArchiveMusicBrainzModel(
//    @SerialName("count")
    override val count: Int = 0,
): CoverArtArchive

fun CoverArtArchiveMusicBrainzModel.toCoverArtArchiveRoomModel() = CoverArtArchiveRoomModel(
    count = count,
)

fun CoverArtArchiveMusicBrainzModel.toCoverArtArchiveUiModel() = CoverArtArchiveUiModel(
    count = count,
)

fun CoverArtArchiveRoomModel.toCoverArtArchiveUiModel() = CoverArtArchiveUiModel(
    count = count,
)

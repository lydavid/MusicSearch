package ly.david.data.room.release

import androidx.room.ColumnInfo
import ly.david.data.core.CoverArtArchive
import ly.david.data.musicbrainz.CoverArtArchiveMusicBrainzModel

data class CoverArtArchiveRoomModel(
    @ColumnInfo(name = "cover_art_count")
    override val count: Int = 0,
) : CoverArtArchive

fun CoverArtArchiveMusicBrainzModel.toCoverArtArchiveRoomModel() = CoverArtArchiveRoomModel(
    count = count,
)

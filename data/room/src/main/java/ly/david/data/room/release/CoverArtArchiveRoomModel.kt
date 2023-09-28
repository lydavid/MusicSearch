package ly.david.data.room.release

import androidx.room.ColumnInfo
import ly.david.data.core.CoverArtArchive

data class CoverArtArchiveRoomModel(
    @ColumnInfo(name = "cover_art_count")
    override val count: Int = 0,
) : CoverArtArchive

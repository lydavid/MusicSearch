package ly.david.data.domain.release

import ly.david.data.core.CoverArtArchive
import ly.david.data.musicbrainz.CoverArtArchiveMusicBrainzModel
import ly.david.data.room.release.CoverArtArchiveRoomModel

data class CoverArtArchiveUiModel(
    override val count: Int = 0,
) : CoverArtArchive

fun CoverArtArchiveMusicBrainzModel.toCoverArtArchiveUiModel() = CoverArtArchiveUiModel(
    count = count,
)

fun CoverArtArchiveRoomModel.toCoverArtArchiveUiModel() = CoverArtArchiveUiModel(
    count = count,
)

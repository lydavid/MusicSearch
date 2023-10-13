package ly.david.musicsearch.domain.release

import ly.david.musicsearch.data.core.CoverArtArchive
import ly.david.data.musicbrainz.CoverArtArchiveMusicBrainzModel

data class CoverArtArchiveUiModel(
    override val count: Int = 0,
) : CoverArtArchive

fun CoverArtArchiveMusicBrainzModel.toCoverArtArchiveUiModel() = CoverArtArchiveUiModel(
    count = count,
)

package ly.david.musicsearch.domain.release

import ly.david.data.musicbrainz.CoverArtArchiveMusicBrainzModel
import ly.david.musicsearch.data.core.release.CoverArtArchiveUiModel

fun CoverArtArchiveMusicBrainzModel.toCoverArtArchiveUiModel() = CoverArtArchiveUiModel(
    count = count,
)

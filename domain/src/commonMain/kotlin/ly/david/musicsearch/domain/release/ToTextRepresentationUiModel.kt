package ly.david.musicsearch.domain.release

import ly.david.data.musicbrainz.TextRepresentationMusicBrainzModel
import ly.david.musicsearch.data.core.release.TextRepresentationUiModel

fun TextRepresentationMusicBrainzModel.toTextRepresentationUiModel() = TextRepresentationUiModel(
    script = script,
    language = language,
)

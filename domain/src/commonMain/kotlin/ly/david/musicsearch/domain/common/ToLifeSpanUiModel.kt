package ly.david.musicsearch.domain.common

import ly.david.data.musicbrainz.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.core.LifeSpanUiModel

fun LifeSpanMusicBrainzModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin,
    end = end,
    ended = ended
)

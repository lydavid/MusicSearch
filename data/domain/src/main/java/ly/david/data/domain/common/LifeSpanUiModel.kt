package ly.david.data.domain.common

import ly.david.data.core.LifeSpan
import ly.david.data.musicbrainz.LifeSpanMusicBrainzModel

data class LifeSpanUiModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : LifeSpan

fun LifeSpanMusicBrainzModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin,
    end = end,
    ended = ended
)

package ly.david.data.domain

import ly.david.data.core.LifeSpan
import ly.david.data.musicbrainz.LifeSpanMusicBrainzModel
import ly.david.data.room.LifeSpanRoomModel

data class LifeSpanUiModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : LifeSpan

fun LifeSpanRoomModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin,
    end = end,
    ended = ended
)

fun LifeSpanMusicBrainzModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin,
    end = end,
    ended = ended
)

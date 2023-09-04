package ly.david.data.room

import ly.david.data.core.LifeSpan
import ly.david.data.musicbrainz.LifeSpanMusicBrainzModel

data class LifeSpanRoomModel(
    override val begin: String?,
    override val end: String?,
    override val ended: Boolean?,
) : LifeSpan

fun LifeSpanMusicBrainzModel.toLifeSpanRoomModel() = LifeSpanRoomModel(
    begin = begin,
    end = end,
    ended = ended
)

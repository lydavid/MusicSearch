package ly.david.data.core

import kotlinx.serialization.Serializable
import ly.david.data.core.common.transformThisIfNotNullOrEmpty

interface LifeSpan {
    val begin: String?
    val end: String?
    val ended: Boolean?
}

@Serializable
data class LifeSpanMusicBrainzModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : LifeSpan

data class LifeSpanRoomModel(
    override val begin: String?,
    override val end: String?,
    override val ended: Boolean?,
) : LifeSpan

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

fun LifeSpanMusicBrainzModel.toLifeSpanRoomModel() = LifeSpanRoomModel(
    begin = begin,
    end = end,
    ended = ended
)

fun LifeSpanMusicBrainzModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin,
    end = end,
    ended = ended
)

fun LifeSpan?.getLifeSpanForDisplay(): String {
    if (this == null) return ""
    val begin = begin.orEmpty()
    val end = if (begin == end) {
        ""
    } else {
        end.transformThisIfNotNullOrEmpty { " to $it" }
    }
    return begin + end
}

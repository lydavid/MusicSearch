package ly.david.data

import kotlinx.serialization.Serializable
import ly.david.data.common.transformThisIfNotNullOrEmpty

interface ILifeSpan {
    val begin: String?
    val end: String?
    val ended: Boolean?
}

/**
 * Used by both network and persistence models.
 */
@Serializable
data class LifeSpanMusicBrainzModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : ILifeSpan

data class LifeSpanRoomModel(
    override val begin: String?,
    override val end: String?,
    override val ended: Boolean?,
) : ILifeSpan

data class LifeSpanUiModel(
    override val begin: String? = null,
    override val end: String? = null,
    override val ended: Boolean? = null,
) : ILifeSpan

fun LifeSpanRoomModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin, end = end, ended = ended
)

fun LifeSpanMusicBrainzModel.toLifeSpanRoomModel() = LifeSpanRoomModel(
    begin = begin, end = end, ended = ended
)

fun LifeSpanMusicBrainzModel.toLifeSpanUiModel() = LifeSpanUiModel(
    begin = begin, end = end, ended = ended
)

fun ILifeSpan?.getLifeSpanForDisplay(): String {
    if (this == null) return ""
    val begin = begin.orEmpty()
    val end = if (begin == end) {
        ""
    } else {
        end.transformThisIfNotNullOrEmpty { " to $it" }
    }
    return begin + end
}

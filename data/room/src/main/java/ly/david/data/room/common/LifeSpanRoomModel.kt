package ly.david.data.room.common

import ly.david.data.core.LifeSpan

data class LifeSpanRoomModel(
    override val begin: String?,
    override val end: String?,
    override val ended: Boolean?,
) : LifeSpan

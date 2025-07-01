package ly.david.musicsearch.shared.domain.listitem

import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.event.Event
import ly.david.musicsearch.shared.domain.image.ImageId

data class EventListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel = LifeSpanUiModel(),
    val imageUrl: String? = null,
    val imageId: ImageId? = null,
    override val visited: Boolean = false,
    override val collected: Boolean = false,
) : EntityListItemModel, Event

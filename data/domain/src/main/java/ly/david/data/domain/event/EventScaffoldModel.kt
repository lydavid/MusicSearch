package ly.david.data.domain.event

import ly.david.data.Event
import ly.david.data.LifeSpan
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import ly.david.data.room.event.EventWithAllData

data class EventScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpan? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : Event

internal fun EventWithAllData.toEventScaffoldModel() =
    EventScaffoldModel(
        id = event.id,
        name = event.name,
        disambiguation = event.disambiguation,
        type = event.type,
        time = event.time,
        cancelled = event.cancelled,
        lifeSpan = event.lifeSpan,
        urls = urls.map { it.relation.toRelationListItemModel() },
    )

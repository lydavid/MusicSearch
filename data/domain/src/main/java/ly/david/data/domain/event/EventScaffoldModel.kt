package ly.david.data.domain.event

import ly.david.data.domain.common.LifeSpanUiModel
import ly.david.data.domain.listitem.RelationListItemModel
import ly.david.data.domain.listitem.toRelationListItemModel
import lydavidmusicsearchdatadatabase.Event
import lydavidmusicsearchdatadatabase.Mb_relation

data class EventScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.data.core.Event

internal fun Event.toEventScaffoldModel(
    urls: List<Mb_relation>,
) = EventScaffoldModel(
    id = id,
    name = name,
    disambiguation = disambiguation,
    type = type,
    time = time,
    cancelled = cancelled,
    lifeSpan = LifeSpanUiModel(
        begin = begin,
        end = end,
        ended = ended,
    ),
    urls = urls.map { it.toRelationListItemModel() },
)

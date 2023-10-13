package ly.david.musicsearch.domain.event

import ly.david.musicsearch.data.core.LifeSpanUiModel
import ly.david.musicsearch.data.core.listitem.RelationListItemModel
import lydavidmusicsearchdatadatabase.Event

data class EventScaffoldModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val time: String? = null,
    override val cancelled: Boolean? = null,
    override val lifeSpan: LifeSpanUiModel? = null,
    val urls: List<RelationListItemModel> = listOf(),
) : ly.david.musicsearch.data.core.Event

internal fun Event.toEventScaffoldModel(
    urls: List<RelationListItemModel>,
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
    urls = urls,
)

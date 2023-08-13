package ly.david.data.domain.listitem

import ly.david.data.Work
import ly.david.data.domain.work.WorkAttributeUiModel
import ly.david.data.domain.work.toWorkAttributeUiModel
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.room.work.WorkRoomModel

// TODO: map "qaa" to Artificial (Other), and rest from 3 letter code to full language name
data class WorkListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
) : Work, ListItemModel()

internal fun WorkMusicBrainzModel.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = attributes?.map { it.toWorkAttributeUiModel() }.orEmpty()
    )

fun WorkRoomModel.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = listOf()
    )

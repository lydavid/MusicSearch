package ly.david.musicsearch.domain.listitem

import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.musicsearch.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.domain.work.toWorkAttributeUiModel
import lydavidmusicsearchdatadatabase.Work

// TODO: map "qaa" to Artificial (Other), and rest from 3 letter code to full language name
data class WorkListItemModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
    override val language: String? = null,
    override val iswcs: List<String>? = null,
    val attributes: List<WorkAttributeUiModel> = listOf(),
) : ly.david.data.core.Work, ListItemModel()

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

fun Work.toWorkListItemModel() =
    WorkListItemModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
        attributes = listOf()
    )

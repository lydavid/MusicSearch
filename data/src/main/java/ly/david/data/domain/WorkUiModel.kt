package ly.david.data.domain

import ly.david.data.Work
import ly.david.data.network.WorkMusicBrainzModel
import ly.david.data.persistence.work.WorkRoomModel

// TODO: map "qaa" to Artificial (Other), and rest from 3 letter code to full language name
data class WorkUiModel(
    override val id: String,
    override val name: String,
    override val disambiguation: String? = null,
    override val type: String? = null,
//    override val typeId: String? = null,
    override val language: String? = null,
//    override val languages: List<String>? = null,
) : Work, UiModel()

internal fun WorkMusicBrainzModel.toWorkUiModel() =
    WorkUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
//        typeId = typeId,
        language = language,
//        languages = languages
    )

internal fun WorkRoomModel.toWorkUiModel() =
    WorkUiModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
//        typeId = typeId,
        language = language,
//        languages = languages
    )

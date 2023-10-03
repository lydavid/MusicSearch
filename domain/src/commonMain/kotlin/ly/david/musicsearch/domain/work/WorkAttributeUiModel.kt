package ly.david.musicsearch.domain.work

import ly.david.data.core.WorkAttribute
import ly.david.data.musicbrainz.WorkAttributeMusicBrainzModel
import lydavidmusicsearchdatadatabase.Work_attribute

data class WorkAttributeUiModel(
    override val type: String,
    override val typeId: String,
    override val value: String,
) : WorkAttribute

internal fun WorkAttributeMusicBrainzModel.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = typeId,
        value = value
    )

internal fun Work_attribute.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = type_id,
        value = value_,
    )

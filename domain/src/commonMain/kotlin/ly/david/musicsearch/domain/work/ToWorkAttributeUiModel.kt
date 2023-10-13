package ly.david.musicsearch.domain.work

import ly.david.musicsearch.data.core.work.WorkAttributeUiModel
import lydavidmusicsearchdatadatabase.Work_attribute

internal fun Work_attribute.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = type_id,
        value = value_,
    )

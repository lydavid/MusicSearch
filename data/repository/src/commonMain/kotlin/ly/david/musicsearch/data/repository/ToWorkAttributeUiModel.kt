package ly.david.musicsearch.data.repository

import ly.david.musicsearch.data.core.work.WorkAttributeUiModel
import lydavidmusicsearchdatadatabase.Work_attribute

// TODO: remove
fun Work_attribute.toWorkAttributeUiModel() =
    WorkAttributeUiModel(
        type = type,
        typeId = type_id,
        value = value_,
    )

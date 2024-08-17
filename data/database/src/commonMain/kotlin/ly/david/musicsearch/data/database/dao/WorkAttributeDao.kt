package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.core.WorkAttributeMusicBrainzModel
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Work_attribute
import lydavidmusicsearchdatadatabase.Work_attributeQueries

class WorkAttributeDao(
    database: Database,
) : EntityDao {
    override val transacter: Work_attributeQueries = database.work_attributeQueries

    private fun insert(workAttribute: Work_attribute) {
        transacter.insert(workAttribute)
    }

    fun insertAttributesForWork(
        workId: String,
        workAttributes: List<WorkAttributeMusicBrainzModel>?,
    ) {
        withTransaction {
            workAttributes?.forEach { workAttribute ->
                insert(
                    Work_attribute(
                        work_id = workId,
                        type = workAttribute.type,
                        type_id = workAttribute.typeId,
                        value_ = workAttribute.value,
                    ),
                )
            }
        }
    }

    fun getWorkAttributesForWork(workId: String): List<WorkAttributeUiModel> =
        transacter.getWorkAttributesForWork(
            workId,
            mapper = { type: String, typeId: String, value: String ->
                WorkAttributeUiModel(
                    type = type,
                    typeId = typeId,
                    value = value,
                )
            },
        ).executeAsList()
}

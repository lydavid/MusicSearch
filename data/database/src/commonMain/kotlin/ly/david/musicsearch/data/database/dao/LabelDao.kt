package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.core.models.LifeSpanUiModel
import ly.david.musicsearch.core.models.label.LabelDetailsModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Label

class LabelDao(
    database: Database,
) : EntityDao {
    override val transacter = database.labelQueries

    fun insert(label: LabelMusicBrainzModel) {
        label.run {
            transacter.insert(
                Label(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    label_code = labelCode,
                    begin = label.lifeSpan?.begin,
                    end = label.lifeSpan?.end,
                    ended = label.lifeSpan?.ended,
                ),
            )
        }
    }

    fun insertAll(labels: List<LabelMusicBrainzModel>?) {
        transacter.transaction {
            labels?.forEach { label ->
                insert(label)
            }
        }
    }

    fun getLabelForDetails(labelId: String): LabelDetailsModel? {
        return transacter.getLabel(
            id = labelId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        labelCode: Int?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = LabelDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )
}

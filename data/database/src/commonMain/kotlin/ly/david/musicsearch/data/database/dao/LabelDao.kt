package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.label.LabelDetailsModel
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
                    ipis = ipis,
                    isnis = isnis,
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
        ipis: List<String>?,
        isnis: List<String>?,
        begin: String?,
        end: String?,
        ended: Boolean?,
    ) = LabelDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        labelCode = labelCode,
        ipis = ipis,
        isnis = isnis,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
    )

    fun delete(id: String) {
        transacter.delete(id)
    }
}

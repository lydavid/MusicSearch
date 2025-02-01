package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel
import ly.david.musicsearch.shared.domain.work.WorkDetailsModel
import lydavidmusicsearchdatadatabase.Work
import lydavidmusicsearchdatadatabase.WorkQueries

class WorkDao(
    database: Database,
) : EntityDao {
    override val transacter: WorkQueries = database.workQueries

    fun insert(work: WorkMusicBrainzModel) {
        work.run {
            transacter.insert(
                Work(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    language = language,
                    iswcs = iswcs?.toImmutableList(),
                ),
            )
        }
    }

    fun insertAll(works: List<WorkMusicBrainzModel>) {
        transacter.transaction {
            works.forEach { work ->
                insert(work)
            }
        }
    }

    fun getWorkForDetails(workId: String): WorkDetailsModel? {
        return transacter.getWork(
            workId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        language: String?,
        iswcs: List<String>?,
    ) = WorkDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
    )

    fun delete(id: String) {
        transacter.delete(id)
    }
}

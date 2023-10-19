package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.data.musicbrainz.WorkMusicBrainzModel
import ly.david.musicsearch.core.models.work.WorkScaffoldModel
import ly.david.musicsearch.data.database.Database
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

    fun getWorkForDetails(workId: String): WorkScaffoldModel? {
        return transacter.getWork(
            workId,
            mapper = ::toWorkScaffoldModel,
        ).executeAsOneOrNull()
    }

    private fun toWorkScaffoldModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        language: String?,
        iswcs: List<String>?,
    ) = WorkScaffoldModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        language = language,
        iswcs = iswcs,
    )
}

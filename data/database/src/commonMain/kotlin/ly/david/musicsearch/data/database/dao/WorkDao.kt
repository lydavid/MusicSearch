package ly.david.musicsearch.data.database.dao

import kotlinx.collections.immutable.toImmutableList
import ly.david.data.musicbrainz.WorkMusicBrainzModel
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Work

class WorkDao(
    database: Database,
) {
    private val transacter = database.workQueries

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
                )
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

    fun getWork(workId: String): Work? {
        return transacter.getWork(workId).executeAsOneOrNull()
    }
}

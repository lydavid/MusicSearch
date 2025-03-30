package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Release_release_group

class ReleaseReleaseGroupDao(
    database: Database,
) : EntityDao {
    override val transacter = database.release_release_groupQueries

    @Suppress("SwallowedException")
    fun insert(
        releaseId: String,
        releaseGroupId: String,
    ): Int {
        return try {
            transacter.insertOrFail(
                Release_release_group(
                    release_id = releaseId,
                    release_group_id = releaseGroupId,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    fun deleteReleaseGroupByReleaseLink(
        releaseId: String,
    ) {
        transacter.deleteReleaseReleaseGroupLink(releaseId = releaseId)
    }
}

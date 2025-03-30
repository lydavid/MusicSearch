package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Release_release_group

class ReleaseReleaseGroupDao(
    database: Database,
) : EntityDao {
    override val transacter = database.release_release_groupQueries

    fun insert(
        releaseId: String,
        releaseGroupId: String,
    ) {
        transacter.insertOrIgnore(
            Release_release_group(
                release_id = releaseId,
                release_group_id = releaseGroupId,
            ),
        )
    }

    fun deleteReleaseGroupByReleaseLink(
        releaseId: String,
    ) {
        transacter.deleteReleaseReleaseGroupLink(releaseId = releaseId)
    }
}

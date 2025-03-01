package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Release_label

/**
 * This currently fulfills the role of both l_label_release (page releases by label)
 * and release_label (allows multiple catalog numbers for a release to be shown in release details screen).
 */
class ReleaseLabelDao(
    database: Database,
) : EntityDao {
    override val transacter = database.release_labelQueries

    @Suppress("SwallowedException")
    fun insert(
        labelId: String,
        releaseId: String,
        catalogNumber: String,
    ): Int {
        return try {
            transacter.insert(
                Release_label(
                    release_id = releaseId,
                    label_id = labelId,
                    catalog_number = catalogNumber,
                ),
            )
            1
        } catch (ex: Exception) {
            0
        }
    }

    fun deleteReleaseLabelLinks(releaseId: String) {
        withTransaction {
            transacter.deleteReleaseLabelLinks(releaseId = releaseId)
        }
    }
}

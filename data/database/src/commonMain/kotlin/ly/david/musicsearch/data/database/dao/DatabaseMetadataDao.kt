package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database

class DatabaseMetadataDao(
    database: Database,
) : EntityDao {
    override val transacter = database.database_metadataQueries

    fun getUserVersion() =
        transacter.getUserVersion().executeAsOne()
}

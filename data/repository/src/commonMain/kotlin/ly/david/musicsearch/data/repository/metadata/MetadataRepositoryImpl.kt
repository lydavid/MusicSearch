package ly.david.musicsearch.data.repository.metadata

import ly.david.musicsearch.data.database.dao.DatabaseMetadataDao
import ly.david.musicsearch.shared.domain.metadata.MetadataRepository

class MetadataRepositoryImpl(
    private val databaseMetadataDao: DatabaseMetadataDao,
) : MetadataRepository {
    override fun getAppDatabaseVersion(): String {
        return databaseMetadataDao.getUserVersion()
    }

    override fun getSQLiteVersion(): String {
        return databaseMetadataDao.getSQLiteVersion()
    }
}
